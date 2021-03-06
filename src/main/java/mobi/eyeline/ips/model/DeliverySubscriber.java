package mobi.eyeline.ips.model;

import mobi.eyeline.ips.web.validators.PhoneValidator;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Comparator;

@Entity
@Proxy(lazy = false)
@Table(name = "delivery_subscribers")
public class DeliverySubscriber implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @JoinColumn(name = "delivery_id")
  @ManyToOne(optional = false)
  private InvitationDelivery invitationDelivery;

  @NotNull
  @Column(name = "msisdn", length = 15)
  @Pattern(regexp = PhoneValidator.PHONE_REGEXP, message = "{invalid.phone.number}")
  private String msisdn;

  @NotNull
  @Column(name = "state", columnDefinition = "VARCHAR(255) NOT NULL DEFAULT 'NEW'")
  @Enumerated(EnumType.STRING)
  private State state = State.NEW;

  public DeliverySubscriber() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public InvitationDelivery getInvitationDelivery() {
    return invitationDelivery;
  }

  public void setInvitationDelivery(InvitationDelivery invitationDelivery) {
    this.invitationDelivery = invitationDelivery;
  }

  public String getMsisdn() {
    return msisdn;
  }

  public void setMsisdn(String msisdn) {
    this.msisdn = msisdn;
  }

  public State getState() {
    return state;
  }

  public void setState(State status) {
    this.state = status;
  }

  @Override
  public String toString() {
    return "DeliverySubscriber{" +
        "id=" + getId() +
        ", delivery=" + getInvitationDelivery().getId() +
        ", msisdn='" + getMsisdn() + '\'' +
        ", state=" + getState() +
        '}';
  }

  public enum State {

    /**
     * Initial state.
     * <p/>
     * From this we get to {@linkplain #FETCHED} once an entry is read from the DB.
     */
    NEW,

    /**
     * Fetched from DB to push.
     * <p/>
     * From this we can get either to {@linkplain #SENT} or {@linkplain #UNDELIVERED}
     */
    FETCHED,

    /**
     * Assigned once an entry has been successfully sent.
     * <p/>
     * From this we can get to either {@linkplain #DELIVERED} or {@linkplain #UNDELIVERED}
     * or {@linkplain #NEW}.
     */
    SENT,

    /**
     * Assigned once an entry has been successfully delivered (i.e. we've got a confirmation).
     * <p/>
     * Terminal state.
     */
    DELIVERED,

    /**
     * Delivery failure. Message gets this state either on negative notification
     * or sending error.
     * <p/>
     * Terminal state.
     */
    UNDELIVERED
  }

  public static final Comparator<DeliverySubscriber> ID_COMPARATOR =
      new Comparator<DeliverySubscriber>() {
        @Override
        public int compare(DeliverySubscriber o1, DeliverySubscriber o2) {
          return Integer.compare(o1.getId(), o2.getId());
        }
      };
}
