package mobi.eyeline.ips.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Вариант ответа на вопрос; для вопросов, предполагающих варианты ответов.
 */
@Entity
@Table(name = "question_options")
public class QuestionOption implements Serializable {

    @Id
    @Column(name = "question_id")
    private Integer id;

    /**
     * Идентификатор ответа.
     */
    @Id
    @Column(name = "code", nullable = false)
    @NotEmpty
    private String code;

    /**
     * Текст ответа.
     */
    @Column(name = "answer", columnDefinition = "TEXT", nullable = false)
    @NotEmpty
    private String answer;

    @ManyToOne
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private Question question;

    public QuestionOption() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer question) {
        this.id = question;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getOrder() {
        return getQuestion().getOptions().indexOf(this);
    }
}
