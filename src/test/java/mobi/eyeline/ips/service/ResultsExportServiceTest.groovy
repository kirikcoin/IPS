package mobi.eyeline.ips.service

import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.RepositoryMock
import mobi.eyeline.ips.repository.SampleAnswers

@Mixin([SampleAnswers, RepositoryMock])
class ResultsExportServiceTest extends DbTestCase {

  private ResultsExportService exportService

  void setUp() {
    super.setUp()

    initRepository(db)
    fillTestData()

    exportService = new ResultsExportService(answerRepository, 2)
  }

  private void checkCsv(List<String> expected,
                        List<String> header, Survey survey, Date from, Date to, String filter) {

    String actual = new ByteArrayOutputStream().with {
      exportService.writeResultsCsv it, header, survey, from, to, filter, null, TimeZone.getTimeZone('UTC'), Locale.forLanguageTag('ru')
      toString 'UTF-8'
    }

    assertEquals fixEOLs(expected.join('\r\n') + '\r\n'), fixEOLs(actual)
  }

  void test1() {
    checkCsv([
        '"a";"b"',
        '"79130000002";;"1";"First \\one";"1";"Option ',
        '',
        '1";"04.06.2014 13:32:30"',
        '"79130000002";;"2";"Second ',
        'one";"3";"Option 3";"07.06.2014 13:32:30"',
        '"79130000002";;"2";"Second ',
        'one";"-1";"someText";"09.06.2014 13:32:30"',
        '"79130000002";;"1";"Third one";"2";"Option 2";"09.06.2014 13:32:30"',
        '"79130000002";;"1";"Third one";"-1";"someText";"11.06.2014 13:32:30"'
    ], ['a', 'b'], survey(1), now + 2, now + 4, null)
  }

  void test2() {
    exportService = new ResultsExportService(answerRepository, 10)

    checkCsv([
        '"a',
        '";" \\ b  "',
        '"79130000001";;"1";"First \\one";"1";"Option ',
        '',
        '1";"03.06.2014 13:32:30"',
        '"79130000001";;"2";"Second ',
        'one";"1";"Option 1";"06.06.2014 13:32:30"',
        '"79130000001";;"1";"Third one";"3";"Option 3";"08.06.2014 13:32:30"',
        '"79130000002";;"1";"First \\one";"1";"Option ',
        '',
        '1";"04.06.2014 13:32:30"',
        '"79130000002";;"2";"Second ',
        'one";"3";"Option 3";"07.06.2014 13:32:30"',
        '"79130000002";;"2";"Second ',
        'one";"-1";"someText";"09.06.2014 13:32:30"',
        '"79130000002";;"1";"Third one";"2";"Option 2";"09.06.2014 13:32:30"',
        '"79130000002";;"1";"Third one";"-1";"someText";"11.06.2014 13:32:30"'
    ], ['a\r\n', ' \\ b  '], survey(1), now - 1, now + 10, '00')
  }

  void test3() {
    checkCsv([
        '"Кириллица";"Урр"" ""урр"""',
        '"79130000003";;"1";"First \\one";"2";"Option 2";"05.06.2014 13:32:30"'
    ], ['Кириллица', 'Урр" "урр"'], survey(2), now - 1, now + 10, null)
  }

  void test4() {
    checkCsv([
        '"Кириллица";"Урр"" ""урр"""',
    ], ['Кириллица', 'Урр" "урр"'], survey(2), now + 10, now + 20, null)
  }

}
