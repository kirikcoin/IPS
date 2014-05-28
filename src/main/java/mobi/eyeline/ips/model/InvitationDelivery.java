package mobi.eyeline.ips.model;



import mobi.eyeline.ips.validation.MaxSize;
import org.hibernate.annotations.Proxy;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Entity
@Proxy(lazy = false)
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

    @MaxSize(value = 100, message ="{invitations.deliveries.dialog.invitationtext.size}" )
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "speed")
    @Max(value = 100, message = "{invitations.deliveries.dialog.speed.max}")
    @Min(value=1, message="{invitations.deliveries.dialog.speed.max}")
    private int speed;

    @Column(name = "errors_count")
    private int errorsCount;

    @NotNull(message = "{invitations.deliveries.dialog.receiversfile.required}")
    @Column(name = "input_file_name")
    private String inputFile;

    @Column(name = "current_position")
    private int currentPosition;


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

    public int getErrorsCount() {
        return errorsCount;
    }

    public void setErrorsCount(int errors_count) {
        this.errorsCount = errors_count;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
