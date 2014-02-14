package mobi.eyeline.ips.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "answers")
@Proxy(lazy = false)
public class Answer implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "respondent_id", nullable = false)
    @ManyToOne(optional = false)
    private Respondent respondent;

    @JoinColumn(name = "question_id", nullable = false)
    @ManyToOne(optional = false)
    private Question question;

    @JoinColumn(name = "option_id", nullable = false)
    @ManyToOne(optional = false)
    private QuestionOption option;

    @Column(name = "timestamp")
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Respondent getRespondent() {
        return respondent;
    }

    public void setRespondent(Respondent respondent) {
        this.respondent = respondent;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionOption getOption() {
        return option;
    }

    public void setOption(QuestionOption option) {
        this.option = option;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
