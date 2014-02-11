package mobi.eyeline.ips.model;

import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;

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
    @NotEmpty
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
}
