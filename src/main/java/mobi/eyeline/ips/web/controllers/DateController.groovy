package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic

import javax.enterprise.inject.Model

@CompileStatic
@Model
class DateController extends BaseController {

  @SuppressWarnings("GrMethodMayBeStatic")
  Date getNow() { new Date() }
}
