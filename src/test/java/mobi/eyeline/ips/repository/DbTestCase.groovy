package mobi.eyeline.ips.repository

import mobi.eyeline.ips.utils.HashUtilsSupport

abstract class DbTestCase extends GroovyTestCase {

    protected DB db

    void setUp() {
        db = new DB(new Properties())

        HashUtilsSupport.init()
    }

    void tearDown() {
        db.sessionFactory.close()
    }
}
