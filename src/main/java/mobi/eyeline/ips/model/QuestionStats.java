package mobi.eyeline.ips.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Статистика ответов на вопрос.
 */
@Entity
@Table(name = "question_stats")
@Proxy(lazy = false)
public class QuestionStats implements Serializable {

    @Id
    @Column(name = "question_id")
    private Integer id;

    /**
     * Счетчик количества отправок данного вопроса респондентам.
     */
    @Column(name = "sentCount")
    @Min(0)
    private int sentCount;

    /**
     * Счетчик количества полученных ответов на вопрос.
     */
    @Column(name = "answerCount")
    @Min(0)
    private int answerCount;

    /**
     * Счетчик количества переотправок данного вопроса респондентам.
     */
    // TODO: seems unneeded in the new version. Should we remove it?
    @Column(name = "resentCount")
    @Min(0)
    private int resentCount;

    public QuestionStats() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer questionId) {
        this.id = questionId;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getResentCount() {
        return resentCount;
    }

    public void setResentCount(int resentCount) {
        this.resentCount = resentCount;
    }
}
