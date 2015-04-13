package mobi.eyeline.ips.model;

import com.google.common.base.Predicate;
import mobi.eyeline.ips.service.SegmentationService;
import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.util.ListUtils;
import mobi.eyeline.ips.validation.MaxSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static javax.persistence.CascadeType.ALL;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Proxy(lazy = false)
@Cache(usage = READ_WRITE)
@DiscriminatorValue("question")
public class Question extends Page {

  /**
   * Текст вопроса, отображается для респондентов.
   */
  @Column(name = "title", nullable = true)
  @NotEmpty(message = "{question.validation.title.empty}")
  @MaxSize(70)
  private String title;

  /**
   * Варианты ответа на вопрос.
   */
  @OneToMany(mappedBy = "question", cascade = ALL, orphanRemoval = true)
  @OrderColumn(name = "option_order")
  @LazyCollection(LazyCollectionOption.FALSE)
  @Valid
  @Cache(usage = READ_WRITE)
  private List<QuestionOption> options = new ArrayList<>();

  /**
   * Разрешить ответ по-умолчанию.
   */
  @Column(name = "enabled_default_answer", columnDefinition = "BIT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean enabledDefaultAnswer;

  /**
   * Опция для вопроса по умолчанию. Если введён некорректный вариант ответа, следующим будет данный вопрос.
   */
  @ManyToOne(optional = true)
  @JoinColumn(name = "default_question_id")
  private Question defaultQuestion;

  @PrePersist
  @PreUpdate
  void prepareIndex() {
    for (QuestionOption option : getOptions()) {
      option.prepareIndex();
    }
  }

  public Question() { }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<QuestionOption> getOptions() {
    return options;
  }

  public List<QuestionOption> getActiveOptions() {
    return newArrayList(filter(getOptions(), not(QuestionOption.SKIP_INACTIVE)));
  }

  public void setOptions(List<QuestionOption> options) {
    this.options = options;
  }

  public int getActiveIndex() {
    return getSurvey().getActiveQuestions().indexOf(this);
  }

  public Question getNext() {
    return ListUtils.getNext(getSurvey().getQuestions(), this, Page.<Question>skipInactive());
  }

  public boolean isFirst() {
    return ListUtils.isFirst(getSurvey().getQuestions(), this, Page.<Question>skipInactive());
  }

  public boolean isLast() {
    return ListUtils.isLast(getSurvey().getQuestions(), this, Page.<Question>skipInactive());
  }

  public Question getDefaultQuestion() {
    return defaultQuestion;
  }

  public void setDefaultQuestion(Question defaultQuestion) {
    this.defaultQuestion = defaultQuestion;
  }

  public boolean isEnabledDefaultAnswer() {
    return enabledDefaultAnswer;
  }

  public void setEnabledDefaultAnswer(boolean enabledDefaultAnswer) {
    this.enabledDefaultAnswer = enabledDefaultAnswer;
  }

  // TODO: seems to be the only way to access this data from page markup.
  // Is there another option?
  public SegmentationService.SegmentationInfo getSegmentationInfo() {
    return Services.instance().getSegmentationService().getSegmentationInfo(this);
  }

  @SuppressWarnings("UnusedDeclaration")
  @AssertTrue(message = "{question.validation.default}")
  private boolean isCorrectDefaultQuestion() {
    return isEnabledDefaultAnswer() || (getDefaultQuestion() == null);
  }

  @SuppressWarnings("UnusedDeclaration")
  @AssertTrue(message = "{question.validation.options.empty}")
  public boolean isValidOptions() {
    return enabledDefaultAnswer || !getActiveOptions().isEmpty();
  }

  @Override
  public String toString() {
    return "Question{" +
        "id=" + getId() +
        ", title='" + title + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Question)) return false;

    Question question = (Question) o;

    return !(getId() != null ? !getId().equals(question.getId()) : question.getId() != null);
  }

  @Override
  public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }

  public static final Predicate<Page> PAGE_IS_QUESTION = new Predicate<Page>() {
    @Override
    public boolean apply(Page page) {
      return page instanceof Question;
    }
  };
}
