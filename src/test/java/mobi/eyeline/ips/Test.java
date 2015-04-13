package mobi.eyeline.ips;

public class Test {
  @org.junit.Test
  public void test() {

    String s = "abc'";

    String jsValue = s;
    jsValue = jsValue.replaceAll("\n", "\\\\n'");
    jsValue = jsValue.replaceAll("\\\\", "\\\\\\\\");
    jsValue = jsValue.replaceAll("'", "\\\\'");


    System.out.println("jsValue = '" + jsValue + "'");
  }
}
