package mobi.eyeline.ips.model;

import com.google.common.base.Predicate;
import mobi.eyeline.ips.util.ListUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "questions")
@Proxy(lazy = false)
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
    private String title;

    /**
     * Варианты ответа на вопрос.
     */
    @OneToMany(mappedBy = "question", cascade = ALL, orphanRemoval = true)
    @OrderColumn(name = "option_order")
    @LazyCollection(LazyCollectionOption.FALSE)
    @NotEmpty(message = "{question.validation.options.empty}")
    @Valid
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
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (survey != null ? !survey.equals(question.survey) : question.survey != null)
            return false;
        if (title != null ? !title.equals(question.title) : question.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = survey != null ? survey.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
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
