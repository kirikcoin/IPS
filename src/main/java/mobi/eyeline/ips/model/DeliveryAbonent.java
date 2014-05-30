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

@Entity
@Proxy(lazy = false)
@Table(name = "delivery_abonents")
public class DeliveryAbonent implements Serializable {

    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "delivery_id")
    @ManyToOne(optional = false)
    private InvitationDelivery invitationDelivery;

    @NotNull
    @Column(name = "msisdn",length = 15)
    @Pattern(regexp = PhoneValidator.PHONE_REGEXP,
            message = "{invalid.phone.number}")
    private String msisdn;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DeliveryAbonentStatus status = DeliveryAbonentStatus.NEW;

    public DeliveryAbonent() {
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

    public DeliveryAbonentStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryAbonentStatus status) {
        this.status = status;
    }
}
