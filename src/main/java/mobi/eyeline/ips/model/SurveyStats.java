package mobi.eyeline.ips.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Статистика опроса.
 */
@Entity
@Table(name = "survey_stats")
@Proxy(lazy = false)
//@IdClass(Survey.class)
public class SurveyStats implements Serializable {

  @Id
  @Column(name = "survey_id")
  @GeneratedValue(generator = "SharedPrimaryKeyGenerator")
  @GenericGenerator(name = "SharedPrimaryKeyGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "survey"))
  private Integer id;

  @OneToOne
  @PrimaryKeyJoinColumn
  private Survey survey;

  /**
   * Число отправленных приглашений.
   */
  @Column(name = "sent")
  private int sentCount;

  /**
   * Произвольный идентификатор кампании.
   */
  @Column(name = "campaign")
  private String campaign;

  @Column(name = "telegram_token")
  private String telegramToken;

  @Column(name = "telegram_username")
  private String telegramUsername;

  /**
   * Дата последнего обновления количества показов (поля sent).
   */
  @Column(name = "last_update")
  private Date lastUpdate;

  @Column(name = "update_status")
  @Type(type = "mobi.eyeline.ips.model.GenericEnumUserType", parameters = {
      @Parameter(name = "enumClass", value = "mobi.eyeline.ips.model.InvitationUpdateStatus"),
      @Parameter(name = "identifierMethod", value = "getName"),
      @Parameter(name = "valueOfMethod", value = "fromName")

  })
  private InvitationUpdateStatus updateStatus = InvitationUpdateStatus.UNDEFINED;


  public SurveyStats() {
  }

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

  public int getSentCount() {
    return sentCount;
  }

  public void setSentCount(int sent) {
    this.sentCount = sent;
  }

  public String getCampaign() {
    return campaign;
  }

  public void setCampaign(String campaign) {
    this.campaign = campaign;
  }

  public String getTelegramToken() {
    return telegramToken;
  }

  public void setTelegramToken(String telegramToken) {
    this.telegramToken = telegramToken;
  }

  public String getTelegramUsername() {
    return telegramUsername;
  }

  public void setTelegramUsername(String telegramUsername) {
    this.telegramUsername = telegramUsername;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public InvitationUpdateStatus getUpdateStatus() {
    return updateStatus;
  }

  public void setUpdateStatus(InvitationUpdateStatus updateStatus) {
    this.updateStatus = updateStatus;
  }

  public String toTraceString() {
    return "SurveyStats{" +
        "surveyId=" + getSurvey().getId() +
        ", sentCount=" + sentCount +
        ", campaign='" + campaign + '\'' +
        ", lastUpdate=" + lastUpdate +
        ", updateStatus=" + updateStatus +
        '}';
  }
}
