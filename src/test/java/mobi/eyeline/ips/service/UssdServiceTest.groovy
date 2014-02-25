package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.messages.MessageHandler
import mobi.eyeline.ips.messages.UssdModel
import mobi.eyeline.ips.messages.UssdOption
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.messages.UssdOption.UssdOptionType;
import mobi.eyeline.ips.messages.UssdOption;
/**
 * Created by dizan on 21.02.14.
 */
class UssdServiceTest extends DbTestCase {
//    private final UssdService ussdService
//    def configClass
//    Config config
//
//    class UssdOptionImpl extends UssdOption{
//
//        protected UssdOptionImpl(int key, String text, boolean skipValidation, int surveyId, UssdOptionType type) {
//            super(key, text, skipValidation, surveyId, type)
//        }
//
//        @Override
//        UssdModel handle(String msisdn, MessageHandler handler) {
//            return null
//        }
//    }
//    void setUp(){
//        super.setUp()
//        configClass = new MockFor(Config).with {
//            demand.getLoginUrl() {'http://localhost:39932'}
//            it
//        }
//    }

    void test(){
        assertTrue(true)
    }
//
//    void testSurveyUrl() {
//
//    }
//
//    void testHandle1(){
//        UssdOption ussdOption = new UssdOptionImpl(1,"",true,1,UssdOptionType.ANSWER)
//        shouldFail(AssertionError){
//            ussdService.handle("89131234567", ussdOption)
//        }
//    }


}
