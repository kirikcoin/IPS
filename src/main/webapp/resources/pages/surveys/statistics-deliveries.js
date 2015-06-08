var page = {

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

    })(jsfc('respondentsRatio'));
  }

};
