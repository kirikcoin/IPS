package mobi.eyeline.ips.model;


import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "deliveries")
public class InvitationDelivery implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "survey_id")
    @ManyToOne(optional = false)
    private Survey survey;

    @Column(name = "date")
    private Date date;

    @Column(name = "type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private InvitationDeliveryType type;

    @Column(name = "active", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "speed")
    private int speed;

    @Column(name = "errors_count")
    private int errors_count;


    public InvitationDelivery() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public InvitationDeliveryType getType() {
        return type;
    }

    public void setType(InvitationDeliveryType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getErrors_count() {
        return errors_count;
    }

    public void setErrors_count(int errors_count) {
        this.errors_count = errors_count;
    }
}
