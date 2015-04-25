//
// Generates test code for all surveys found in the DB,
//

package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Survey

def props = new Properties()
props['hibernate.connection.url'] = 'jdbc:mysql://localhost/ips_20141016?useUnicode=true&amp;characterEncoding=UTF-8'
props['hibernate.connection.username'] = 'user'
props['hibernate.connection.password'] = 'password'

def session = new DB(props).sessionFactory.openSession()

def surveys = session.createQuery('from Survey where active = true').list() as List<Survey>
surveys.each { s ->
  def tab = '    '

  println '\n'
  println tab + "void test$s.id() {"
  println tab * 2 + "def survey = survey(id: ${s.id}) {"
  println tab * 3 + "questions {"
  s.activeQuestions.each { q ->
    println tab * 4 + "question(title: '${q.title.replace('\n', '\\n')}') {"
    q.activeOptions.each { opt ->
      println tab * 5 + "option(answer: '${opt.answer}')"
    }
    println tab * 4 + "}" + '\n'
  }

  println tab * 3 + "}" // END questions
  println tab * 2 + "}" // END def survey
  println '\n'

  s.activeQuestions.eachWithIndex { q, idx ->
    println tab * 2 + "assertEquals null, checkOptionLength(survey.questions[$idx])"
  }

  println tab + '}'
}