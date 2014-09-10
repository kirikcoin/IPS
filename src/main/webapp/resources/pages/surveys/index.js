var page = {

  filterKeyDown: function (event) {
    if (event.keyCode == 13) {
      jsfc('table').update(true);
      return false;
    } else {
      return true;
    }
  },

  onCreateSurveyClick: function() {
    jsfc('newSurveyDialog').show();

    return false;
  },

  onCreateSurveyCancel: function () {
    jsfc('newSurveyDialog').hide();

    $("#newSurveyStartDate").val($("[id$=newSurveyStartDateOrig]").val());
    $("#newSurveyEndDate").val($("[id$=newSurveyEndDateOrig]").val());

    ips.message.hideAll();
    return false;
  },

  init: function () {
    ips.$byId("search").focus();
  }

};