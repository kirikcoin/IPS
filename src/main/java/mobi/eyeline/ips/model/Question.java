package mobi.eyeline.ips.model;

import com.google.common.base.Predicate;
import com.sun.istack.internal.Nullable;
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

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static javax.persistence.CascadeType.ALL;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Table(name = "questions")
@Proxy(lazy = false)
@Cache(usage = READ_WRITE)
public class Question implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    /**
     * Текст вопроса, отображается для респондентов.
     */
    @Column(name = "title", nullable = false)
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
     * Порядок отображения вопроса в опросе.
     */
    @Column(name = "question_order")
    private int order;

    /**
     * При удалении вопрос помечается флагом {@code active = false} в БД и
     * перестает отображаться в веб-интерфейсе.
     */
    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active = true;

    /**
     * Количество отправок данного вопроса респондентам.
     */
    @Column(name = "sent_count")
    private int sentCount;

    /**
     *  Разрешить ответ по-умолчанию.
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
        if (getSurvey() != null) {
            order = getSurvey().getQuestions().indexOf(this);
        }

        for (QuestionOption option : getOptions()) {
            option.prepareIndex();
        }
    }

    public Question() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    @NotEmpty(message = "{question.validation.options.empty}")
    public List<QuestionOption> getActiveOptions() {
        return newArrayList(filter(getOptions(), not(QuestionOption.SKIP_INACTIVE)));
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }

    public int getOrder() {
        return order;
    }

    public int getActiveIndex() {
        return getSurvey().getActiveQuestions().indexOf(this);
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public Question getNext() {
        return ListUtils.getNext(getSurvey().getQuestions(), this, SKIP_INACTIVE);
    }

    public boolean isFirst() {
        return ListUtils.isFirst(getSurvey().getQuestions(), this, SKIP_INACTIVE);
    }

    public boolean isLast() {
        return ListUtils.isLast(getSurvey().getQuestions(), this, SKIP_INACTIVE);
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
    @AssertTrue(message = "Incorrect default question")
    private boolean isCorrectDefaultQuestion() {
         return isEnabledDefaultAnswer() || (!isEnabledDefaultAnswer() && getDefaultQuestion() == null);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;

        Question question = (Question) o;

        return !(id != null ? !id.equals(question.id) : question.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

//
    //
    //

    public static final Predicate<Question> SKIP_INACTIVE = new Predicate<Question>() {
        @Override
        public boolean apply(Question question) {
            return !question.isActive();
        }
    };
}
