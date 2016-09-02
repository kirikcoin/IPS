package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Constants

import javax.enterprise.context.ApplicationScoped
import javax.inject.Named

@ApplicationScoped
@Named('forms')
@CompileStatic
class Forms implements Serializable {

  int surveyTitleLength         = Constants.SURVEY_TITLE_LENGTH
  int surveyEndTextLength       = Constants.SURVEY_END_TEXT_LENGTH
  int surveyCouponLength        = Constants.SURVEY_COUPON_LENGTH
  int surveyOALength            = Constants.SURVEY_OA_LENGTH

  int questionTitleLength       = Constants.QUESTION_TITLE_LENGTH
  int questionOptionLength      = Constants.QUESTION_OPTION_LENGTH

  int questionExtLinkNameLength = Constants.QUESTION_EXT_LINK_NAME_LENGTH
  int questionExtLinkLength     = Constants.QUESTION_EXT_LINK_LENGTH

  int invitationTextSize        = Constants.INVITATION_TEXT_LENGTH

}
