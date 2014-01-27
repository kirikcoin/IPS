package mobi.eyeline.ips.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Связка между поьзователями и опросами для указания доступных опросов
 * для тех пользователей, права которых не предусматривают доступ ко <b>всем</b> опросам.
 */
@Entity
@Table(name = "surveys_users")
@Proxy(lazy = false)
@IdClass(SurveyAndUserKey.class)
public class UserSurveyPermissions implements Serializable {

    @Id
    @JoinColumn(name = "survey_id")
    @ManyToOne(optional = false)
    private Survey survey;

    @Id
    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private User user;

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
