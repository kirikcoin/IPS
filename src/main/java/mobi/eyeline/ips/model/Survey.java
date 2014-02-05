package mobi.eyeline.ips.model;

import mobi.eyeline.ips.util.Utils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

/**
 * Опрос - это последовательность взаимосвязанных вопросов.
 * <br/>
 * Каждый опрос проходит в фиксированный период времени,
 * задаваемый датами {@link #startDate начала} и {@link #endDate окончания}.
 */
@Entity
@Table(name = "surveys")
@Proxy(lazy = false)
public class Survey implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Дата начала опроса.
     */
    @Column(name = "startdate", nullable = false)
    @NotNull(message = "{survey.validation.start.date.empty}")
    private Date startDate;

    /**
     * Дата окончания опроса.
     */
    @Column(name = "expires", nullable = false)
    @NotNull(message = "{survey.validation.end.date.empty}")
    private Date endDate;

    /**
     * При удалении опрос помечается флагом {@code active = false} в БД и
     * перестает отображаться в веб-интерфейсе.
     */
    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    /**
     * Статистика результатов опроса,
     */
    @Valid
    @OneToOne(mappedBy = "survey", cascade = ALL)
    private SurveyStats statistics;

    /**
     * Дополнительная информация об опросе.
     */
    @Valid
    @OneToOne(mappedBy = "survey", cascade = ALL)
    private SurveyDetails details;

    @OneToMany(mappedBy = "survey", cascade = ALL, orphanRemoval = true)
    @OrderColumn(name = "question_order")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Question> questions = new ArrayList<>();

    @Valid
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "surveys_users",
            joinColumns = {
                    @JoinColumn(name = "survey_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id")
            })
    private User client;

    public Survey() {
    }

    @PrePersist
    @PreUpdate
    public void prepareIndex() {
        for (Question question : getQuestions()) {
            question.prepareIndex();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SurveyStats getStatistics() {
        return statistics;
    }

    public void setStatistics(SurveyStats statistics) {
        this.statistics = statistics;
    }

    public SurveyDetails getDetails() {
        return details;
    }

    public void setDetails(SurveyDetails surveyDetails) {
        this.details = surveyDetails;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public void moveUp(Question question) {
        Utils.moveUp(getQuestions(), question);
    }

    public void moveDown(Question question) {
        Utils.moveDown(getQuestions(), question);
    }

    @SuppressWarnings("UnusedDeclaration")
    @AssertTrue(message = "{survey.validation.end.must.be.after.start}")
    private boolean isEndDate() {
        return (getStartDate() != null) &&
               (getEndDate() != null) &&
               getStartDate().compareTo(getEndDate()) <= 0;
    }

    public int getQuestionsCount() {
        return getQuestions().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Survey survey = (Survey) o;

        if (id != null ? !id.equals(survey.id) : survey.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
