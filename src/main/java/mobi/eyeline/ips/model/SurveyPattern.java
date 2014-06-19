package mobi.eyeline.ips.model;

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
import java.io.Serializable;

@Entity
@Proxy(lazy = false)
@Table(name = "survey_pattern")
public class SurveyPattern implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "survey_id")
    @ManyToOne(optional = false)
    private Survey survey;

    @Column(name = "position" , columnDefinition = "INT")
    private long position = 0;

    @Column(name = "length")
    private Integer length;

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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
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