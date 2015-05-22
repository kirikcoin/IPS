var page = {

  INVALID_DATE_MSG: 'Invalid date format',

  init: function () {

    (function (_) {
      _.clientOptions = {
        enablePadding: true,
        plotOverrides: {
          legend: {
            show: true,
            location: 'ne'
          },
          highlighter: { show: false }
        }
      }

    })(jsfc('barRatio'));
  },

  onShow: function () {
    var $periodStart = $('#periodStart'),
        $periodEnd = $('#periodEnd'),

        checkFormat = function ($input) {
          $input.removeClass('validationError');

          if (ips.utils.isDate($input.val())) return true;

          $input.addClass('validationError');
          ips.message.error(page.INVALID_DATE_MSG);
          return false;
        },

        error = false;

    if (!checkFormat($periodStart)) error = true;
    if (!checkFormat($periodEnd))   error = true;

    return !error;
  }

};
