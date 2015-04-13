package mobi.eyeline.ips.model;

import com.google.common.base.Predicate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

/**
 * Page of an USSD service.
 */
@Entity
@Table(name = "pages")
@Proxy(lazy = false)
@Cache(usage = READ_WRITE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Page implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "survey_id")
  private Survey survey;

  /**
   * При удалении страница помечается флагом {@code deleted = true} в БД и
   * перестает отображаться в веб-интерфейсе.
   */
  @Column(name = "deleted", columnDefinition = "BIT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean deleted = false;

  /**
   * Количество отправок данной страницы респондентам.
   */
  @Column(name = "sent_count")
  private int sentCount;

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

  public boolean isActive() {
    return !deleted;
  }

  public void setActive(boolean active) {
    this.deleted = !active;
  }

  public int getSentCount() {
    return sentCount;
  }

  public void setSentCount(int sentCount) {
    this.sentCount = sentCount;
  }

  /**
   * Page name.
   */
  public abstract String getTitle();

  public static <T extends Page> Predicate<T> skipInactive() {
    return new Predicate<T>() {
      @Override
      public boolean apply(T page) {
        return !page.isActive();
      }
    };
  }
}
