var page = {


  filterKeyDown: function (event) {
    if (event.keyCode == 13) {
      jsfc('resultsTable').update(true);
      return false;
    } else {
      return true;
    }
  },

  init: function () {
    jsfc('resultsTable').bind('update', function () {
      var hasResults = jsfc('resultsTable').getRowsCount() != 0;
      $('#downloadButtonsPanel').toggle(hasResults);
    });
  }
};
