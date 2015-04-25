package mobi.eyeline.ips.model;

import org.hibernate.annotations.Cache;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Proxy(lazy = false)
@Table(name = "survey_pattern")
@Cache(usage = READ_WRITE)
public class SurveyPattern implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @JoinColumn(name = "survey_id")
  @ManyToOne(optional = false)
  private Survey survey;

  @Min(0)
  @Column(name = "position", columnDefinition = "INT")
  private long position = 0;

  @Min(0)
  @NotNull
  @Column(name = "length")
  private int length;

  @NotNull
  @Column(name = "mode")
  @Enumerated(EnumType.STRING)
  private Mode mode;

  @Column(name = "active", columnDefinition = "BIT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean active;

  public SurveyPattern() {
  }

  public Survey getSurvey() {
    return survey;
  }

  public void setSurvey(Survey survey) {
    this.survey = survey;
  }

  public long getPosition() {
    return position;
  }

  public void setPosition(long position) {
    this.position = position;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public String toString() {
    return "SurveyPattern{" +
        "id=" + id +
        ", survey=" + survey +
        ", position=" + position +
        ", length=" + length +
        ", mode=" + mode +
        ", active=" + active +
        '}';
  }

  /**
   * @see mobi.eyeline.ips.util.PatternUtil Pattern mode to regular expression correspondence.
   */
  public static enum Mode {
    DIGITS,
    DIGITS_AND_LATIN
  }
}