package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.QuestionOption;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The exact code extract from Mobilizer segmentation algorithm.
 *
 * @see com.eyelinecom.whoisd.sads2.adaptor.USSDAutoSegmentation
 */
@SuppressWarnings("JavadocReference")
public class MobilizerSegmentation {

  private static final String CONF_PART_MENU = "menu";
  private static final String CONF_PART_LINK_TO_START = "toStart";
  private static final String CONF_PART_LINK_TO_END = "toEnd";
  private static final String CONF_PART_LINK_NEXT = "next";
  private static final String CONF_PART_LINK_PREV = "prev";

  private static final String SEGMENT_DELIM = "...";

  private Pattern menuLinkPattern = Pattern.compile("0.*");
  private int lineFeedLength = 1;

  private int sizeReserve = 2;

  private String customDelim = ">";

  private String toStartName = "To start";
  private String toEndName = "To end";
  private String nextName = "Next";
  private String prevName = "Prev";

  private SegmentType typeStart = new SegmentType("Start", "next menu");
  private SegmentType typeEnd = new SegmentType("End", "prev");
  private SegmentType typeList = new SegmentType("List", "next prev");

  private static final String LF = "\n";

  private static final String COMMAND_DELIM = ">";

  public static boolean isMenuItem(String text) {
    return isMenuItem(text, COMMAND_DELIM);
  }

  public static boolean isMenuItem(String text, String customDelimeter) {
    return getLinkPattern("[0-9]+", ".*", customDelimeter).matcher(text).matches();
  }

  public static Pattern getLinkPattern(String accessKey, String text, String customDelim) {
    String linkStrPattern = "(" + accessKey + customDelim + text + "|" + accessKey + StringEscapeUtils.escapeXml(customDelim) + text + ")";
    return Pattern.compile(linkStrPattern, Pattern.DOTALL | Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
  }

  private static List<String> getMenuTextLinks(String ussdPage) {

    String[] strings = ussdPage.split(LF);
    List<String> links = new ArrayList<>(strings.length);
    for (String str : strings) {
      if (isMenuItem(str, COMMAND_DELIM)) {
        links.add(str);
      }
    }
    return links;
  }

  /**
   * @return {@linkplain mobi.eyeline.ips.model.QuestionOption#getActiveIndex() Index}
   * of the failing option, or {@code -1} in case of an error in question title,
   * or {@code null} if all is OK.
   */
  public static Integer checkOptionLength(Question question) {

    String text = question.getTitle() + "\n";

    final List<QuestionOption> activeOptions = question.getActiveOptions();
    for (int i = 0; i < activeOptions.size(); i++) {
      QuestionOption opt = activeOptions.get(i);
      text += (opt.getActiveIndex() + 1) + "> " + opt.getAnswer();
      if (i != activeOptions.size() - 1) {
        text += "\n";
      }
    }

    try {
      new MobilizerSegmentation().transform(text);
      return null;

    } catch (OptionTooLongException e) {
      return e.getOptionIdx() - 1;    // Actual `activeIndex'.

    } catch (Exception e) {
      return -1;  // Consider it question title.
    }
  }

  private List<Segment> transform(String ussdPage) throws Exception {
    return this.getSegments(ussdPage);
  }

  private List<String> getWordList(String ussdPage) {
    String[] strings = ussdPage.split(LF);
    List<String> wordList = new ArrayList<>(strings.length);
    for (String text : strings) {
      if (!isMenuItem(text)) {
        String[] words = text.split("\\s+");
        words[words.length - 1] = words[words.length - 1] + LF;
        wordList.addAll(Arrays.asList(words));
      } else {
        // if this link isn't a menu link, we'll use it as a single word
        if (!menuLinkPattern.matcher(text).matches())
          wordList.add(text + LF);
      }
    }
    return wordList;
  }

  private List<Segment> getSegments(String ussdPage) throws OptionTooLongException {
    List<String> wordList = this.getWordList(ussdPage);
    SegmentList list = new SegmentList(ussdPage);
    for (String word : wordList) {
      try {
        list.add(word);
      } catch (RuntimeException e) {
        if (isMenuItem(word)) {
          Matcher m = Pattern.compile("^\\d").matcher(word);
          if (m.find()) throw new OptionTooLongException(Integer.parseInt(m.group()));
        }
        throw e;
      }
    }
    return list.getSegments();
  }


  private String getMenuText(String ussdPage) {
    StringBuilder temp = new StringBuilder();
    List<String> menuLinks = getMenuTextLinks(ussdPage);
    for (String link : menuLinks) {
      //if link satisfies pattern condition
      if (menuLinkPattern.matcher(link).matches())
        temp.append(link).append(LF);
    }
    String rez = temp.toString();
    return StringUtils.trim(rez);
  }

  private int getMenuLength(String ussdPage) {
    return getMenuText(ussdPage).length();
  }

  public static String buildCommandText(String command, String name, String customDelim) {
    return command + customDelim + name;
  }

  private int getNextLength() {
    String nextKey = "01";
    return buildCommandText(nextKey, StringEscapeUtils.unescapeXml(nextName), customDelim).length() + lineFeedLength;
  }

  private int getPrevLength() {
    String prevKey = "00";
    return buildCommandText(prevKey, StringEscapeUtils.unescapeXml(prevName), customDelim).length() + lineFeedLength;
  }

  private int getToStartLength() {
    String toStartKey = "08";
    return buildCommandText(toStartKey, StringEscapeUtils.unescapeXml(toStartName), customDelim).length() + lineFeedLength;
  }

  private int getToEndLength() {
    String toEndKey = "09";
    return buildCommandText(toEndKey, StringEscapeUtils.unescapeXml(toEndName), customDelim).length() + lineFeedLength;
  }

  private class SegmentType {
    private String name;

    private boolean hasMenu;
    private boolean hasNextLink;
    private boolean hasPrevLink;
    private boolean hasToStartLink;
    private boolean hasToEndLink;

    public String getName() {
      return name;
    }

    private SegmentType(String name, String config) {
      this(name, StringUtils.containsIgnoreCase(config, CONF_PART_MENU),
          StringUtils.containsIgnoreCase(config, CONF_PART_LINK_NEXT),
          StringUtils.containsIgnoreCase(config, CONF_PART_LINK_PREV),
          StringUtils.containsIgnoreCase(config, CONF_PART_LINK_TO_START),
          StringUtils.containsIgnoreCase(config, CONF_PART_LINK_TO_END)
      );
    }

    private SegmentType(String name, boolean hasMenu, boolean hasNextLink, boolean hasPrevLink, boolean hasToStartLink, boolean hasToEndLink) {
      this.name = name;
      this.hasMenu = hasMenu;
      this.hasNextLink = hasNextLink;
      this.hasPrevLink = hasPrevLink;
      this.hasToStartLink = hasToStartLink;
      this.hasToEndLink = hasToEndLink;
    }

    public int getFreeLength(String ussdPage, boolean smppStartPage) {

      return getSegmentLength(ussdPage) - countFreeLength(ussdPage, smppStartPage);
    }


    public int getFreeLength(String ussdPage) {

      return getSegmentLength(ussdPage) - countFreeLength(ussdPage);
    }

    private int countFreeLength(String ussdPage) {
      int counter = 0;
      if (hasMenu) counter += getMenuLength(ussdPage);

      if (hasNextLink) counter += getNextLength();
      if (hasPrevLink) counter += getPrevLength();
      if (hasToEndLink) counter += getToEndLength();
      if (hasToStartLink) counter += getToStartLength();
      return counter;
    }

    private int countFreeLength(String ussdPage, boolean smppStartPage) {
      if (smppStartPage)
        return countFreeLength(ussdPage);
      else return (countFreeLength(ussdPage) + sizeReserve);
    }

    public int getFreeLength(String ussdPage, String word) {
      int wordEncodingLength = SegmentationService.getSegmentLength(word);
      return wordEncodingLength - countFreeLength(ussdPage);
    }

    public int getFreeLength(String ussdPage, String word, boolean smppStartPage) {
      int wordEncodingLength = SegmentationService.getSegmentLength(word);
      return wordEncodingLength - countFreeLength(ussdPage, smppStartPage);
    }

    public int getSegmentLength(String ussdPage) {

      StringBuilder str = new StringBuilder();

      if (hasMenu) str.append(getMenuText(ussdPage));
      if (hasNextLink) str.append(nextName);
      if (hasPrevLink) str.append(prevName);
      if (hasToEndLink) str.append(toEndName);
      if (hasToStartLink) str.append(toStartName);
      return SegmentationService.getSegmentLength(str.toString());
    }

    public int getSegmentLength(String ussdPage, boolean smppStartPage) {
      if (smppStartPage)
        return getSegmentLength(ussdPage);
      else return getSegmentLength(ussdPage) - sizeReserve;
    }

    @Override
    public String toString() {
      return "SegmentType{" +
          "name='" + name + '\'' +
          ", hasMenu=" + hasMenu +
          ", hasNextLink=" + hasNextLink +
          ", hasPrevLink=" + hasPrevLink +
          ", hasToStartLink=" + hasToStartLink +
          ", hasToEndLink=" + hasToEndLink +
          '}';
    }
  }


  private static class Segment {
    private LinkedList<String> words = new LinkedList<>();

    private SegmentType type;

    private int segmentLength;
    private int freeLength;

    private Segment(SegmentType type) {
      this.type = type;
      segmentLength = 0;
      freeLength = 0;
    }


    public void setType(SegmentType type) {
      this.type = type;

    }

    public SegmentType getType() {
      return type;
    }

    public void add(String ussdPage, String word) throws SegmentFullException {
      if (segmentLength == 0) {
        if (type.getName().equals("Start"))
          segmentLength = type.getSegmentLength(ussdPage, false);
        else segmentLength = type.getSegmentLength(ussdPage);
      }
      if (freeLength == 0) {
        if (type.getName().equals("Start"))
          freeLength = type.getFreeLength(ussdPage, false);
        else freeLength = type.getFreeLength(ussdPage);

      }

      int wordEncodingSize = SegmentationService.getSegmentLength(word);

      //если  количество знаков в сегменте не больше
      if (segmentLength <= wordEncodingSize) {
        if (getFreeLength(freeLength) - SEGMENT_DELIM.length() >= word.length()) {
          words.add(word);
          //log.debug("segmentLength= " + segmentLength + " freeLength= " + getFreeLength(freeLength) + " word: " + word);
        } else {
          lastWord();
        }
      } else {
        //хватит ли вообще места, если использовать новую длину сегмента
        if (!isEnoughSpace(wordEncodingSize)) {
          lastWord();
        } else {
          //иначе пытаемся вставить слова в кодировке >бит, проверяем влезут ли все наши слова в новую длину

          int newFreeLength;
          if (type.getName().equals("Start"))
            newFreeLength = type.getFreeLength(ussdPage, word, false);
          else newFreeLength = type.getFreeLength(ussdPage, word);
          segmentLength = wordEncodingSize;
          freeLength = newFreeLength;

          if (getFreeLength(newFreeLength) - SEGMENT_DELIM.length() >= word.length()) {
            //если слово нормально добавилось с использованием новой длины сегмента, меняем длину сегмента на новую
            words.add(word);
            //log.debug("segmentLength= " + segmentLength + " new freeLength= " + freeLength + " word: " + word);
          } else {

            lastWord();
          }

        }
      }
    }

    private void lastWord() throws SegmentFullException {
      String lastWord = words.getLast();
      if (isMenuItem(lastWord))
        throw new SegmentFullException();
      words.removeLast();
      words.add(lastWord + SEGMENT_DELIM);
      throw new SegmentFullException();
    }

    private boolean isEnoughSpace(int newSegmentSize) {
      return newSegmentSize > (segmentLength - getFreeLength(freeLength));

    }

    private int getFreeLength(int length) {
      return length - getTextLength();
    }

    private int getTextLength() {
      return getText().length();
    }

    private String getText() {
      StringBuilder str = new StringBuilder();
      for (String word : words) {
        str.append(word);
        if (!word.endsWith(LF)) str.append(" ");
      }
      return str.toString();
    }
  }


  private class SegmentList {
    private LinkedList<Segment> segmentList = new LinkedList<>();
    private String ussdPage;


    private SegmentList(String ussdPage) {
      this.ussdPage = ussdPage;
      segmentList.add(new Segment(typeStart));
    }


    public void add(String word) {
      try {
        segmentList.getLast().add(ussdPage, word);
      } catch (SegmentFullException e) {
        segmentList.add(new Segment(typeList));
        try {
          if (isMenuItem(word))
            segmentList.getLast().add(ussdPage, word);
          else segmentList.getLast().add(ussdPage, SEGMENT_DELIM + word);
        } catch (SegmentFullException e1) {
          // Ignored.
        }
      }
    }

    public List<Segment> getSegments() {
      Segment lastSegment = segmentList.getLast();
      if (lastSegment.getTextLength() <= typeEnd.getFreeLength(ussdPage)) {
        lastSegment.setType(typeEnd);
      } else {
        segmentList.add(new Segment(typeEnd));
      }

      return segmentList;
    }
  }

  private static class SegmentFullException extends Exception {
  }

  private static class OptionTooLongException extends Exception {
    private final int optionIdx;

    private OptionTooLongException(int optionIdx) {
      this.optionIdx = optionIdx;
    }

    public int getOptionIdx() {
      return optionIdx;
    }
  }
}
