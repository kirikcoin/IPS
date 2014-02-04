var page = {

  onCreateSurveyClick: function() {
    jsfc('newSurveyDialog').show();

    return false;
  },

  init: function () {
    ips.$byId("search").focus();
  },

  onDialogCancel: function(id) {
    jsfc(id).hide();
    ips.message.hideAll();
    return false;
  }
};