var page = {

  init: function () {
    /* Nothing here. */

    (function (_) {
      // And add some plot options.
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
