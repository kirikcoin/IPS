package mobi.eyeline.ips.repository

import mobi.eyeline.ips.utils.HashUtilsSupport
import org.hibernate.Session
import org.hibernate.Transaction
import org.hibernate.context.internal.ThreadLocalSessionContext

abstract class DbTestCase extends GroovyTestCase {

  protected DB db

  private Transaction tx

  void setUp() {
    db = new DB(new Properties())

    final Session session = db.sessionFactory.openSession()
    ThreadLocalSessionContext.bind(session)
    tx = session.beginTransaction()

    HashUtilsSupport.init()
  }

  void tearDown() {
    tx.commit()
    db.sessionFactory.close()
  }
}
