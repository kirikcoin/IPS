package mobi.eyeline.ips.web.controllers

class LogoBean extends BaseController{
    byte[] logo;

    LogoBean() {
        request.session.setAttribute("logoBean",this)
    }
}
