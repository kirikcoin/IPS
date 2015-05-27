var page = {

  init: function () {
    var resultsTable = jsfc('resultsTable');

    $('#downloadButtonsPanel').hide();

    resultsTable.bind('update', function () {
      var hasResults = jsfc('resultsTable').getRowsCount() != 0;
      $('#downloadButtonsPanel').toggle(hasResults);
    });

    // Update on checkbox click.
    $('#showEmptyRows').on('change', function () {
      return page.doUpdate();
    });
  },

  doUpdate: function () {
    $('.resultsTablePlaceholder').hide();

    jsfc('resultsTable').setVisible(true);
    jsfc('resultsTable').update(true);
    return false;
  }

};