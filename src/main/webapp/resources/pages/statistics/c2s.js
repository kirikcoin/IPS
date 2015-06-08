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
    var resultsTable = jsfc('resultsTable');

    if (page._checkDatesValid()) {
      $('.resultsTablePlaceholder').hide();

      resultsTable.setVisible(true);
      resultsTable.update(true);
    }

    return false;
  },

  /**
   * @return {boolean}  True iff date inputs contain valid values.
   * @private
   */
  _checkDatesValid: function () {
    var $periodStart = $('#periodStart'),
        $periodEnd = $('#periodEnd'),

        checkFormat = function ($input) {
          if (ips.utils.isDate($input.val())) return true;

          $input.addClass('validationError');
          ips.message.error(page.INVALID_DATE_MSG);
          return false;
        },

        error = false;

    ips.message.hideAll();
    if (!checkFormat($periodStart)) error = true;
    if (!checkFormat($periodEnd))   error = true;

    return !error;
  }

};