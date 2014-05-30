package mobi.eyeline.ips.repository

import groovy.sql.Sql

sid = 31537
count = 2_000_000
parts = 2
batch = 1000

db = Sql.newInstance(
        'jdbc:mysql://localhost/ips_migration_test3?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&allowMultiQueries=true',
        'user',
        'password',
        'com.mysql.jdbc.Driver')

//
//  Get questions/options for the survey.
//

questions = {
    def questions = [:]
    db.eachRow("SELECT id FROM questions WHERE survey_id = $sid AND active = TRUE") {
        questions[it.id] = db.rows("""
            SELECT id
            FROM question_options
            WHERE question_id = ${it.id} AND active = TRUE
            """).collect { it.id }
    }
    questions
}() as Map
println "Questions -> options: $questions"


//
//  Determine MSISDN range.
//

def ranges = { range ->
    int size = range.size() / parts
    (0..<parts).collect { range.subList it * size, (it+1) * size }
}(7_999_000_00_00..7_999_000_00_00+(count-1))


//
//  Clean all respondents and answers.
//

def bench = { closure ->
    start = System.currentTimeMillis()
    closure.call()
    System.currentTimeMillis() - start
}

def time = bench {
    db.withTransaction {
        db.execute '''
            SET FOREIGN_KEY_CHECKS = 0;
            TRUNCATE answers;
            TRUNCATE respondents;
            SET FOREIGN_KEY_CHECKS = 1;
        '''
    }
}
println "Cleaning up: $time ms"


//
//  Insert respondents and answers.
//

def toId(i) { i % count as int }

def inBatch(statement, closure) {
    db.withTransaction {
        db.execute 'SET FOREIGN_KEY_CHECKS = 0;'
        db.execute 'SET UNIQUE_CHECKS = 0;'
        db.withBatch(batch, statement) { ps -> closure(ps) }
        db.execute 'SET UNIQUE_CHECKS = 1;'
        db.execute 'SET FOREIGN_KEY_CHECKS = 1;'
    }
}

def insertRespondents(range) {
    nAnswers = questions.size()
    inBatch("""INSERT IGNORE INTO respondents (id, msisdn, survey_id, answer_count, finished)
               VALUES (?, ?, ?, ?, ?)""") { ps ->
        range.each { i -> ps.addBatch toId(i), i as String, sid, nAnswers, 1 }
    }
}

def insertAnswers(range) {
    inBatch("""INSERT IGNORE INTO answers (respondent_id, question_id, option_id)
               VALUES (?, ?, ?)""") { ps ->
        range.each { i -> questions.each { k, v -> ps.addBatch toId(i), k as int, v.first() } }
    }
}

time = bench {
    ranges.each { insertRespondents(it) }
    ranges.each { insertAnswers(it) }
}

println "Inserting data: $time ms";
