package mobi.eyeline.ips.web.controllers

import javax.faces.bean.ManagedBean
import javax.faces.bean.SessionScoped

/**
 * Logotype for displaying in upload preview.
 */
@ManagedBean(name = "logoBean", eager = true)
@SessionScoped
class LogoBean implements Serializable {
  byte[] bytes

}
