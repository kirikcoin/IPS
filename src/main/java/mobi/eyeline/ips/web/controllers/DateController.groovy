package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic;

@CompileStatic
public class DateController extends BaseController {

    @SuppressWarnings("GrMethodMayBeStatic")
    Date getNow() { new Date() }
}
