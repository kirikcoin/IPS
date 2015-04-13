package mobi.eyeline.ips.service

import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption

class SegmentationServiceTest extends GroovyTestCase {
  SegmentationService segmentationService

  void setUp() {
    super.setUp()
    segmentationService = new SegmentationService()
  }

  void test1() {
    def q = new Question(title: 'Вопрос 1')
    [
        new QuestionOption(answer: 'Ответ 1', active: true),
        new QuestionOption(answer: 'Ответ 2', active: true)
    ].each { it.question = q; q.options << it }

    assertEquals new SegmentationService.SegmentationInfo(1, 28, 70),
        segmentationService.getSegmentationInfo(q)
  }

  void test2() {
    def q = new Question(title: 'В')
    [
        new QuestionOption(answer: 'О', active: true),
    ].each { it.question = q; q.options << it }

    assertEquals new SegmentationService.SegmentationInfo(1, 5, 70),
        segmentationService.getSegmentationInfo(q)
  }

  void test3() {
    def q = new Question(title: 'Q')
    [
        new QuestionOption(answer: 'A', active: true),
    ].each { it.question = q; q.options << it }

    assertEquals new SegmentationService.SegmentationInfo(1, 5, 160),
        segmentationService.getSegmentationInfo(q)
  }

  void test4() {
    def q = new Question(title: 'Билд упал. Как так????')
    [
        new QuestionOption(answer: 'А вот так', active: true),
        new QuestionOption(answer: 'Этого ответа нет', active: false),
        new QuestionOption(answer: 'Я ёжик', active: true),
    ].each { it.question = q; q.options << it }

    assertEquals new SegmentationService.SegmentationInfo(1, 43, 70),
        segmentationService.getSegmentationInfo(q)
  }

  void test5() {
    def q = new Question(title: '-')

    assertEquals new SegmentationService.SegmentationInfo(1, 1, 160),
        segmentationService.getSegmentationInfo(q)
  }

  void test6() {
    def q = new Question(title: 'Билд упал. Как так????')

    (1..30)
        .collect { new QuestionOption(answer: "Option number $it", active: true) }
        .each { it.question = q; q.options << it }

    assertEquals new SegmentationService.SegmentationInfo(9, 604, 70),
        segmentationService.getSegmentationInfo(q)
  }

  void test7() {
    def q = new Question(title: 'Question title')

    (1..30)
        .collect { new QuestionOption(answer: "Option number $it", active: true) }
        .each { it.question = q; q.options << it }

    assertEquals new SegmentationService.SegmentationInfo(4, 596, 160),
        segmentationService.getSegmentationInfo(q)
  }

  void test8() {
    def q = new Question(title: 'ô… Keď ?')
    [
        new QuestionOption(answer: 'môže', active: true),
        new QuestionOption(answer: 'ním tiež siať', active: true)
    ].each { it.question = q; q.options << it }

    assertEquals new SegmentationService.SegmentationInfo(1, 31, 70),
        segmentationService.getSegmentationInfo(q)
  }

  void test9() {
    def q = new Question(title: 'Spôsob sejby… Keď sa ďatelina seje medzi ovos?')
    [
        new QuestionOption(answer: 'môže sa s ním tiež siať aj naraz', active: true),
        new QuestionOption(answer: 'keby ale túto spoločnú', active: true)
    ].each { it.question = q; q.options << it }

    assertEquals new SegmentationService.SegmentationInfo(2, 106, 70),
        segmentationService.getSegmentationInfo(q)
  }
}
