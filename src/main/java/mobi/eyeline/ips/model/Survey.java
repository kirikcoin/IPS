package mobi.eyeline.ips.model;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import mobi.eyeline.ips.util.ListUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
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

import static com.beust.jcommander.internal.Lists.newArrayList;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Iterables.tryFind;
import static com.google.common.collect.Lists.newArrayList;
import static javax.persistence.CascadeType.ALL;
import static mobi.eyeline.ips.model.Question.SKIP_INACTIVE;

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
    private boolean active = true;

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

    /**
     * Пользователь, создавший данный опрос.
     * В данный момент ролью этого пользоателя может быть только "Менеджер".
     */
    @JoinColumn(name = "owner_id")
    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    private User owner;

    @OneToMany(mappedBy = "survey", cascade = ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SurveyPattern> patterns;

    public Survey() {
    }

    @PrePersist
    @PreUpdate
    void prepareIndex() {
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

    public List<Question> getActiveQuestions() {
        return com.google.common.collect.Lists.newArrayList(filter(getQuestions(), not(SKIP_INACTIVE)));
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void moveUp(Question question) {
        ListUtils.moveUp(getQuestions(), question, SKIP_INACTIVE);
    }

    public void moveDown(Question question) {
        ListUtils.moveDown(getQuestions(), question, SKIP_INACTIVE);
    }

    public Question getFirstQuestion() {
        return tryFind(getQuestions(), not(SKIP_INACTIVE)).orNull();
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

    public int getActiveQuestionsCount() {
        return getActiveQuestions().size();
    }

    public boolean isRunningNow() {
        final Date now = new Date();
        return getStartDate().before(now) && getEndDate().after(now);
    }

    public List<SurveyPattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<SurveyPattern> patterns) {
        this.patterns = patterns;
    }

    public SurveyPattern getActivePattern() {
        for (SurveyPattern pattern : getPatterns()) {
            if (pattern.isActive()) {
                return pattern;
            }
        }
        return null;
    }

    public List<SurveyPattern> getInactivePatterns() {
        return newArrayList(filter(getPatterns(), new Predicate<SurveyPattern>() {
            @Override
            public boolean apply(SurveyPattern input) {
                return !input.isActive();
            }
        }));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Survey survey = (Survey) o;

        return !(id != null ? !id.equals(survey.id) : survey.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String toTraceString() {
        // Note that we don't dump questions here.
        return "Survey{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", active=" + active +
                ", statistics=" + ((statistics == null) ? null : statistics.toTraceString()) +
                ", details=" + ((details == null) ? null : details.toTraceString()) +
                ", questions=" + questions +
                ", client=" + ((client == null) ? null : client.toTraceString()) +
                ", owner=" + ((owner == null) ? null : owner.toTraceString()) +
                '}';
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id=" + id +
                '}';
    }
}
