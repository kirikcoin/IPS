package mobi.eyeline.ips.model;

import mobi.eyeline.ips.validation.MaxSize;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Статистика опроса.
 */
@Entity
@Table(name = "survey_stats")
@Proxy(lazy = false)
@IdClass(Survey.class)
public class SurveyStats implements Serializable {

    @Id
    @JoinColumn(name = "survey_id")
    @OneToOne(optional = false, cascade = CascadeType.ALL)
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

    /**
     * C2S/USSD-номер опроса.
     */
    @Column(name = "accessNumber")
    @MaxSize(70)
    @Pattern(regexp = "[0-9 \\-\\.\\+\\*#]*",
            message = "{survey.validation.access.number.invalid}")
    private String accessNumber;

    /**
     * Дата последнего обновления количества показов (поля sent).
     */
    @Column(name = "last_update")
    private Date lastUpdate;

    @Column(name = "update_status")
    @Type(type = "mobi.eyeline.ips.model.GenericEnumUserType", parameters = {
            @Parameter(name = "enumClass", value= "mobi.eyeline.ips.model.InvitationUpdateStatus"),
            @Parameter(name = "identifierMethod", value= "getName"),
            @Parameter(name = "valueOfMethod", value= "fromName")

    })
    private InvitationUpdateStatus updateStatus = InvitationUpdateStatus.UNDEFINED;


    public SurveyStats() {
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

    public String getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber;
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
}
