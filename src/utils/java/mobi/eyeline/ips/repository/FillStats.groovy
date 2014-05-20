package mobi.eyeline.ips.repository

import groovy.sql.Sql
import groovyx.gpars.GParsPool

sid = 31537
count = 100_000
threads = 2
batch = 1000

db = Sql.newInstance(
        'jdbc:mysql://localhost/ips_migration_test2?useUnicode=true&amp;characterEncoding=UTF-8&rewriteBatchedStatements=true',
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
    int size = range.size() / threads
    (0..<threads).collect { range.subList(it * size, (it+1) * size) }
}(7_999_000_00_00..7_999_000_00_00+(count-1))


//
//  Clean all respondents and answers.
//

db.withTransaction {
    db.execute 'delete from answers'
    db.execute 'delete from respondents'
}


//
//  Insert respondents and answers.
//

def toId(i) { i % count as int }

def insertRespondents(range) {
    db.withTransaction {
        db.withBatch(batch,
                """INSERT IGNORE INTO respondents (id, msisdn, survey_id, answer_count, finished)
                   VALUES (?, ?, ?, ?, ?)""") { ps ->
            range.each { i -> ps.addBatch toId(i), i as String, sid, questions.size(), 1 }
        }
    }
}

def insertAnswers(range) {
    db.withTransaction {
        db.execute 'SET FOREIGN_KEY_CHECKS = 0;'
        db.withBatch(batch,
                """INSERT IGNORE INTO answers (respondent_id, question_id, option_id)
                   VALUES (?, ?, ?)""") { ps ->

            range.each { i ->
                questions.each { k, v -> ps.addBatch toId(i), k as int, v.first() }
            }
        }
        db.execute 'SET FOREIGN_KEY_CHECKS = 1;'
    }
}

def bench = { closure ->
    start = System.currentTimeMillis()
    closure.call()
    System.currentTimeMillis() - start
}

def time = bench {
    GParsPool.withPool(threads) {
        ranges.eachParallel { insertRespondents(it) }
        ranges.eachParallel { insertAnswers(it) }
    }
}

println "$time ms";
