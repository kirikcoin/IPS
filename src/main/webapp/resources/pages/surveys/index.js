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

  init: function () {
    ips.$byId("search").focus();
  }

};