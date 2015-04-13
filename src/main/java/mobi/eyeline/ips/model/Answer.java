package mobi.eyeline.ips.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
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
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Proxy(lazy = false)
public abstract class Answer implements Serializable {

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

  @Column(name = "timestamp")
  private Date date;

  public abstract String getAnswer();

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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
