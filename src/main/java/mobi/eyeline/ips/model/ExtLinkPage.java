package mobi.eyeline.ips.model;

import com.google.common.base.Predicate;
import mobi.eyeline.ips.validation.MaxSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Proxy;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

/**
 * USSD service page performing a redirection to an external service URL.
 */
@Entity
@Proxy(lazy = false)
@Cache(usage = READ_WRITE)
@DiscriminatorValue("ext_link")
public class ExtLinkPage extends Page {

  @Column(name = "ext_link_name", nullable = true)
  @NotEmpty(message = "{ext.link.validation.name.empty}")
  @MaxSize(value = 70, message = "{ext.link.validation.name.size}")
  private String serviceName;

  @Column(name = "ext_link_url", nullable = true)
  @NotEmpty(message = "{ext.link.validation.url.empty}")
  @URL(message = "{ext.link.validation.url.invalid}")
  @MaxSize(value = 255, message = "{ext.link.validation.url.size}")
  private String serviceUrl;

  public ExtLinkPage() { }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getServiceUrl() {
    return serviceUrl;
  }

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  @Override
  public String getTitle() {
    return getServiceName();
  }

  @Override
  public int getActiveIndex() {
    return getSurvey().getActiveExtLinkPages().indexOf(this);
  }

  public static final Predicate<Page> PAGE_IS_EXT_LINK = new Predicate<Page>() {
    @Override
    public boolean apply(Page page) {
      return page instanceof ExtLinkPage;
    }
  };
}
