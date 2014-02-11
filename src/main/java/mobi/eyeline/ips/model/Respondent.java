package mobi.eyeline.ips.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @JoinColumn(name = "survey_id", nullable = false)
    @ManyToOne(optional = false)
    private Survey survey;

    /**
     * Идентификатор абонента (телефонный номер).
     */
    @Column(name = "MSISDN", nullable = false)
    @NotEmpty
    private String msisdn;

    /**
     * Флаг, указывает, закончил ли респондент прохождение опроса.
     */
    @Column(name = "answered", columnDefinition = "BIT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean finished;

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

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean answered) {
        this.finished = answered;
    }
}
