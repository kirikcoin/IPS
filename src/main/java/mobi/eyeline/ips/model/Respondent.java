package mobi.eyeline.ips.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Абонент любого оператора сотовой связи, который решил принять участие в опросе.
 */
@Entity
@Table(name = "respondents")
public class Respondent {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Опрос, в котором зарегистрирован данный респондент.
     * <br/>
     * В случае, если респондент принимает участие в нескольких опросах, для него будут различные
     * записи в таблице с разным идентификатором опроса.
     */
    // TODO: use join table here?
    // TODO: use model class instead of raw ID.
    @Column(name = "survey_id", nullable = false)
    private Integer sid;

    /**
     * Идентификатор строки, соответствующей данному респонденту, в таблице статистики опроса.
     */
    // Row ID in lime_survey_SID.
    @Column(name = "aid", nullable = false)
    private Integer aid;

    /**
     * Идентификатор абонента (телефонный номер).
     */
    @Column(name = "MSISDN", nullable = false)
    @NotEmpty
    private String msisdn;

    /**
     * Флаг, указывает, ответил ли данный респондент на (хотя бы один) вопрос указанного вопроса.
     */
    // TODO: seems to hold a boolean value. Should we remove integer type?
    @Column(name = "answered", columnDefinition = "BIT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean answered;

    public Respondent() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
