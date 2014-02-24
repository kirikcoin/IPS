package mobi.eyeline.ips.model;

import com.google.common.base.Predicate;
import mobi.eyeline.ips.util.ListUtils;
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
import javax.persistence.Table;
import java.io.Serializable;

import static mobi.eyeline.ips.model.Question.SKIP_INACTIVE;

/**
 * Вариант ответа на вопрос; для вопросов, предполагающих варианты ответов.
 */
@Entity
@Table(name = "question_options")
@Proxy(lazy = false)
public class QuestionOption implements Serializable {

    /**
     * Идентификатор ответа.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Текст ответа.
     */
    @Column(name = "answer", columnDefinition = "TEXT", nullable = false)
    @NotEmpty(message = "{question.option.answer.empty}")
    private String answer;

    /**
     * Порядок отображения вопроса в опросе.
     */
    @Column(name = "option_order")
    private int order;

    /**
     * Индикатор прекращения опроса. <br/>
     * В случае, если (1) флаг выставлен и (2) выбирается именно эта опция,
     * опрос завершается.
     */
    @Column(name = "terminal", columnDefinition = "BIT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean terminal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * При удалении вариант ответа помечается флагом {@code active = false} в БД и
     * перестает отображаться в веб-интерфейсе.
     */
    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active = true;

    public QuestionOption() {
    }

    void prepareIndex() {
        if (getQuestion() != null) {
            order = getQuestion().getOptions().indexOf(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getOrder() {
        return order;
    }

    public int getActiveIndex() {
        return getQuestion().getActiveOptions().indexOf(this);
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void moveTo(int idx) {
        ListUtils.moveTo(getQuestion().getOptions(), this, idx, SKIP_INACTIVE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionOption)) return false;

        QuestionOption that = (QuestionOption) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "QuestionOption{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                '}';
    }


    //
    //
    //

    public static final Predicate<QuestionOption> SKIP_INACTIVE = new Predicate<QuestionOption>() {
        @Override
        public boolean apply(QuestionOption option) {
            return !option.isActive();
        }
    };

}
