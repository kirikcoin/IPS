package mobi.eyeline.ips.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

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
     * Число зарегистрированных респондентов.
     */
    @Column(name = "registeredCount")
    private int registeredRespondentsCount;

    /**
     * Число респндентов, зарегистрированных и ответивших хотя бы на один вопрос.
     */
    @Column(name = "answeredCount")
    private int answeredUser;

    /**
     * Число отправленных приглашений.
     */
    @Column(name = "sent")
    private int sentCount;

    /**
     * Канал распространения - способ информирования потенциальных респондентов о проведении опроса.
     */
    @Column(name = "channel")
    @Enumerated(EnumType.STRING)
    private DistributionChannel channel = DistributionChannel.CLIENT_BASE;

    /**
     * Произвольный идентификатор кампании.
     */
    @Column(name = "campaign")
    private String campaign;

    /**
     * C2S/USSD-номер опроса.
     */
    @Column(name = "accessNumber")
    private String accessNumber;

    public SurveyStats() {
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public int getRegisteredRespondentsCount() {
        return registeredRespondentsCount;
    }

    public void setRegisteredRespondentsCount(int registerUser) {
        this.registeredRespondentsCount = registerUser;
    }

    public int getAnsweredUser() {
        return answeredUser;
    }

    public void setAnsweredUser(int answeredUser) {
        this.answeredUser = answeredUser;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sent) {
        this.sentCount = sent;
    }

    public DistributionChannel getChannel() {
        return channel;
    }

    public void setChannel(DistributionChannel channel) {
        this.channel = channel;
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

}
