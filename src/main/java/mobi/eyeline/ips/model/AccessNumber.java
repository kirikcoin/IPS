package mobi.eyeline.ips.model;

import mobi.eyeline.ips.validation.MaxSize;
import mobi.eyeline.ips.web.validators.PhoneValidator;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "access_numbers")
@Proxy(lazy = false)
public class AccessNumber {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number")
    @MaxSize(30)
    @NotNull
    @Pattern(regexp = "[0-9 \\-\\.\\+\\*#]+", message = "{survey.validation.access.number.invalid}")
    private String number;

    @OneToOne(mappedBy = "accessNumber", optional = true)
    private SurveyStats survey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public SurveyStats getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyStats survey) {
        this.survey = survey;
    }
}
