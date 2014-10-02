package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic

import javax.faces.bean.ManagedBean

@CompileStatic
@ManagedBean(name = "dateController")
class DateController extends BaseController {

    @SuppressWarnings("GrMethodMayBeStatic")
    Date getNow() { new Date() }
}
