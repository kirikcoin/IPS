var page = {

  init: function () {
    jsfc('resultsTable').bind('update', function () {
      var hasResults = jsfc('resultsTable').getRowsCount() != 0;
      $('#downloadButtonsPanel').toggle(hasResults);
    });
  }


};