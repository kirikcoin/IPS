package mobi.eyeline.ips.model;

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
import java.util.Date;

/**
 * Абонент любого оператора сотовой связи, который решил принять участие в опросе.
 */
@Entity
@Table(name = "respondents")
@Proxy(lazy = false)
public class Respondent implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Опрос, в котором зарегистрирован данный респондент.
   * <br/>
   * В случае, если респондент принимает участие в нескольких опросах, для него будут различные
   * записи в таблице с разным идентификатором опроса.
   */
  @JoinColumn(name = "survey_id", nullable = false)
  @ManyToOne(optional = false)
  private Survey survey;

  /**
   * Идентификатор абонента (телефонный номер).
   */
  @Column(name = "MSISDN", nullable = false)
  @NotEmpty
  private String msisdn;

  private RespondentSource source;

  @Column(name = "registered", nullable = false)
  private Date startDate = new Date();

  /**
   * Флаг, указывает, закончил ли респондент прохождение опроса.
   * <br/>
   * Может означать либо отсутствие неотвеченных вопросов,
   * либо отказ от продолжения (aka "терминальный" ответ).
   */
  @Column(name = "finished", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean finished;

  /**
   * Число ответов. Сбрасывается, если опрос начат сначала.
   */
  @Column(name = "answer_count")
  private int answersCount;

  @Column(name = "coupon")
  private String coupon;

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

  public String getMsisdn() {
    return msisdn;
  }

  public void setMsisdn(String msisdn) {
    this.msisdn = msisdn;
  }

  public RespondentSource getSource() {
    return source;
  }

  public void setSource(RespondentSource source) {
    this.source = source;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean answered) {
    this.finished = answered;
  }

  public int getAnswersCount() {
    return answersCount;
  }

  public void setAnswersCount(int answersCount) {
    this.answersCount = answersCount;
  }

  public String getCoupon() {
    return coupon;
  }

  public void setCoupon(String coupon) {
    this.coupon = coupon;
  }

  @Override
  public String toString() {
    return "Respondent{" +
        "id=" + id +
        ", msisdn='" + msisdn + '\'' +
        ", surveyId=" + getSurvey().getId() +
        '}';
  }
}
