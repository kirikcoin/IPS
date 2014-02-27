package mobi.eyeline.ips.model;

import mobi.eyeline.ips.validation.MaxSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

// TODO: Take localization into account.
//       For now we consider only one localization option for each survey.
@Entity
@Table(name = "surveys_text")
public class SurveyDetails implements Serializable {

    /**
     * Название опроса.
     */
    @Column(name = "title", columnDefinition = "varchar(200)", nullable = false)
    @NotEmpty(message = "{survey.validation.title.empty}")
    @MaxSize(70)
    private String title;

    /**
     * Сообщение, отображаемое про завершении опроса - после ответа на последний вопрос.
     */
    @Column(name = "end_text", columnDefinition = "TEXT")
    @MaxSize(70)
    private String endText;

    @Id
    @JoinColumn(name = "survey_id")
    @GeneratedValue(generator = "gen")
    @GenericGenerator(
            name = "gen",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "survey"))
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Survey survey;

    public SurveyDetails() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndText() {
        return endText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}