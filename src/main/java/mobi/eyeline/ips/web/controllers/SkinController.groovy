package mobi.eyeline.ips.web.controllers

class SkinController extends BaseController{
    byte[] logo;
    String skin;

    SkinController() {
        request.session.setAttribute("skinController",this)
    }
}
