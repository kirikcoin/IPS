package mobi.eyeline.ips.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Table(name = "survey_invitations")
@Cache(usage = READ_WRITE)
public class SurveyInvitation implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Количество разосланных приглашений.
   */
  @Column(name = "value", nullable = false)
  @Min(value = 1, message = "{invitations.value.invalid}")
  private int value;

  /**
   * Дата отправки приглашений.
   */
  @Column(name = "date", nullable = false)
  @NotNull(message = "{invitations.date.null}")
  private Date date;

  @JoinColumn(name = "survey_id")
  @GeneratedValue(generator = "gen")
  @GenericGenerator(
      name = "gen",
      strategy = "foreign",
      parameters = @Parameter(name = "property", value = "survey"))
  @ManyToOne(optional = false)
  private Survey survey;

  public SurveyInvitation() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Survey getSurvey() {
    return survey;
  }

  public void setSurvey(Survey survey) {
    this.survey = survey;
  }

  @Override
  public String toString() {
    return "SurveyInvitation{" +
        "id=" + id +
        ", value=" + value +
        ", date=" + date +
        '}';
  }
}
