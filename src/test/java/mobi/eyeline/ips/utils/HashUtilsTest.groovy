package mobi.eyeline.ips.utils

class HashUtilsTest extends GroovyTestCase {

  void setUp() {
    HashUtilsSupport.init()
  }

  void test1() {
    assertEquals '576852EC76F385E165AEF0811AD063BCE443706AFE5B680F03C832135350B62D',
        'bagel'.pw()
    assertEquals '9697F98138CF60AFD5ED5C9EB92F0D622161BCE4AD17BEB531F770DB2B0C817F',
        'abc_def!ghi'.pw()
    assertEquals 'E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855',
        ''.pw()
    assertEquals 'F1F511654EB60320FDA47F1B66D5D71706518373BDCD4515212ED993BB0EF72F',
        'foo12 bar 34%_#()*&^'.pw()
    assertEquals '4FDDAE2A9D6EF78988AC4BEC56A852F699EBF07694483BEF7155B4ADA4E09ABA',
        'текст кириллицей'.pw()
    assertEquals '78CF50ECEA54AACF72E4EC717E33928EE8D34FD8298890F517C27BFE0FA79112',
        'КаКоЙ-т0,PaSSwOrD.;'.pw()
  }
}
