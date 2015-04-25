package mobi.eyeline.ips.web.controllers

import javax.enterprise.context.SessionScoped
import javax.inject.Named

/**
 * Logotype for displaying in upload preview.
 */
@SessionScoped
@Named
class LogoBean implements Serializable {
  byte[] bytes

}
