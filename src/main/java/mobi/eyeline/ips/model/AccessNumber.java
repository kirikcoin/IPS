package mobi.eyeline.ips.model;

import com.google.common.base.Function;
import mobi.eyeline.ips.validation.MaxSize;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
  @NotNull(message = "{survey.validation.access.number.invalid}")
  @Pattern(regexp = "[1-9\\.\\+\\*][0-9\\.\\+\\*#]+", message = "{survey.validation.access.number.invalid}")
  private String number;

  @JoinColumn(name = "survey_id")
  @ManyToOne
  private SurveyStats surveyStats;

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

  public SurveyStats getSurveyStats() {
    return surveyStats;
  }

  public void setSurveyStats(SurveyStats survey) {
    this.surveyStats = survey;
  }

  @Override
  public boolean equals(Object o) {
    return this == o ||
        (o instanceof AccessNumber && getNumber().equals(((AccessNumber) o).getNumber()));
  }

  @Override
  public int hashCode() {
    return getNumber() == null ? 0 : getNumber().hashCode();
  }

  @Override
  public String toString() {
    return "AccessNumber{" +
        "id=" + id +
        ", number='" + number + '\'' +
        '}';
  }

  /** Number entity -> callable number. */
  public static final Function<AccessNumber, String> AS_NUMBER =
      new Function<AccessNumber, String>() {
        @Override
        public String apply(AccessNumber number) {
          return number.getNumber();
        }
      };

}
