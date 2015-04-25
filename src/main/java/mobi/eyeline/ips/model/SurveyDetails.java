package mobi.eyeline.ips.model;

import mobi.eyeline.ips.validation.MaxSize;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.AssertTrue;
import java.io.Serializable;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

// TODO: Take localization into account.
//       For now we consider only one localization option for each survey.
@Entity
@Table(name = "surveys_text")
@Cache(usage = READ_WRITE)
public class SurveyDetails implements Serializable {

  /**
   * Название опроса.
   */
  @Column(name = "title", columnDefinition = "varchar(200)", nullable = false)
  @NotEmpty(message = "{survey.validation.title.empty}")
  @MaxSize(45)
  private String title;

  /**
   * Сообщение, отображаемое про завершении опроса - после ответа на последний вопрос.
   */
  @Column(name = "end_text", columnDefinition = "TEXT")
  @MaxSize(70)
  private String endText;

  @Column(name = "end_sms_enabled", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean endSmsEnabled;

  @Column(name = "end_sms_text", columnDefinition = "varchar(255)")
  @MaxSize(255)
  private String endSmsText;

  @Column(name = "end_sms_from", columnDefinition = "varchar(255)")
  @MaxSize(70)
  private String endSmsFrom;

  @Id
  @JoinColumn(name = "survey_id")
  @GeneratedValue(generator = "gen")
  @GenericGenerator(
      name = "gen",
      strategy = "foreign",
      parameters = @Parameter(name = "property", value = "survey"))
  @OneToOne(optional = false, cascade = CascadeType.ALL)
  private Survey survey;

  public SurveyDetails() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getEndText() {
    return endText;
  }

  public void setEndText(String endText) {
    this.endText = endText;
  }

  public boolean isEndSmsEnabled() {
    return endSmsEnabled;
  }

  public void setEndSmsEnabled(boolean endSmsEnabled) {
    this.endSmsEnabled = endSmsEnabled;
  }

  public String getEndSmsText() {
    return endSmsText;
  }

  public void setEndSmsText(String endSmsText) {
    this.endSmsText = endSmsText;
  }

  public String getEndSmsFrom() {
    return endSmsFrom;
  }

  public void setEndSmsFrom(String endSmsFrom) {
    this.endSmsFrom = endSmsFrom;
  }

  public Survey getSurvey() {
    return survey;
  }

  public void setSurvey(Survey survey) {
    this.survey = survey;
  }

  public String toTraceString() {
    return "SurveyDetails{" +
        "title='" + title + '\'' +
        ", endText='" + endText + '\'' +
        ", surveyId=" + survey.getId() +
        '}';
  }

  @SuppressWarnings("UnusedDeclaration")
  @AssertTrue(message = "{survey.settings.end.message.sms.required}")
  private boolean isEndSmsTextSet() {
    return !endSmsEnabled || StringUtils.isNotEmpty(endSmsText);
  }

}