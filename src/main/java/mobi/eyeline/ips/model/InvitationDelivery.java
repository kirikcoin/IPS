package mobi.eyeline.ips.model;


import mobi.eyeline.ips.validation.MaxSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Proxy;

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
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Proxy(lazy = false)
@Table(name = "deliveries")
@Cache(usage = READ_WRITE)
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

  @NotNull
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private Type type;

  @NotNull
  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private State state = State.INACTIVE;

  @MaxSize(value = 100, message = "{invitations.deliveries.dialog.invitationtext.size}")
  @Column(name = "text", columnDefinition = "TEXT")
  private String text;

  @Column(name = "speed")
  @Max(value = 100, message = "{invitations.deliveries.dialog.speed.max}")
  @Min(value = 1, message = "{invitations.deliveries.dialog.speed.max}")
  private int speed;

  @NotNull(message = "{invitations.deliveries.dialog.receiversfile.required}")
  @Column(name = "input_file_name")
  private String inputFile;

  @Formula("(select count(*) from delivery_subscribers d where d.delivery_id = id and d.state in ('DELIVERED', 'UNDELIVERED'))")
  private Integer processedCount;

  @Formula("(select count(*) from delivery_subscribers d where d.delivery_id = id)")
  private Integer totalCount;

  @Formula("(select count(*) from delivery_subscribers d where d.delivery_id = id and d.state in ('UNDELIVERED'))")
  private Integer errorsCount;

  @Column(name = "retriesEnabled", columnDefinition = "BIT")
  @org.hibernate.annotations.Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean retriesEnabled = true;

  @Max(value = 50, message = "{invitations.deliveries.retries.number.interval}")
  @Min(value = 1, message = "{invitations.deliveries.retries.number.interval}")
  @Column(name = "retriesNumber")
  private Integer retriesNumber = 1;

  @Max(value = 60, message = "{invitations.deliveries.retries.interval.interval}")
  @Min(value = 1, message = "{invitations.deliveries.retries.interval.interval}")
  @Column(name = "retriesInterval")
  private Integer retriesIntervalMinutes = 1;

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

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public State getState() {
    return state;
  }

  public void setState(State status) {
    this.state = status;
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

  public String getInputFile() {
    return inputFile;
  }

  public void setInputFile(String inputFile) {
    this.inputFile = inputFile;
  }

  public Integer getProcessedCount() {
    return processedCount;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public Integer getErrorsCount() {
    return errorsCount;
  }

  public boolean getRetriesEnabled() {
    return retriesEnabled;
  }

  public void setRetriesEnabled(boolean retriesEnabled) {
    this.retriesEnabled = retriesEnabled;
  }

  public Integer getRetriesNumber() {
    return retriesNumber;
  }

  public void setRetriesNumber(Integer retriesNumber) {
    this.retriesNumber = retriesNumber;
  }

  public Integer getRetriesIntervalMinutes() {
    return retriesIntervalMinutes;
  }

  public void setRetriesIntervalMinutes(Integer retriesInterval) {
    this.retriesIntervalMinutes = retriesInterval;
  }

  @AssertTrue(message = "Interval and number of retries must be not empty.")
  private boolean isValidRetriesFields() {
    return (retriesEnabled) ? (retriesNumber != null && retriesIntervalMinutes != null) : true;
  }

  public static enum State {

    /**
     * Valid, not running.
     * <p/>
     * Initial state, can be switched to {@linkplain #ACTIVE}.
     */
    INACTIVE,

    /**
     * Terminal state: no unprocessed subscribers remain.
     */
    COMPLETED,

    /**
     * Is allowed to proceed.
     * From this we can either get {@linkplain #COMPLETED} or {@linkplain #INACTIVE}.
     */
    ACTIVE;

    public boolean isEditable() {
      return this == INACTIVE;
    }
  }

  public static enum Type {

    /**
     * Subscriber gets USSD message.
     */
    USSD_PUSH,

    /**
     * Subscriber gets SMS message.
     */
    SMS,

    /**
     * Subscriber gets the first page of an associated survey.
     */
    NI_DIALOG
  }
}
