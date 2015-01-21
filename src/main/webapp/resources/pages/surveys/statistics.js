var page = {

  init: function () {
    ips.forEachPageComponent(
        // Find all the `responseRatio_<id>' charts,
        function (_) { return _.id.indexOf('responseRatio') == 0; },
        function (_) {
          // And add some plot options.
          _.clientOptions = {
            enablePadding: true,
            plotOverrides: {
              legend: {
                show: true,
                location: 's'
              },
              seriesDefaults: {
                rendererOptions: { barWidth: 25 },
                pointLabels: {
                  show: true,
                  location: 'e',
                  edgeTolerance: -15
                }
              },
              axes: {
                yaxis: {
                  tickOptions: { show: false }
                }
              },
              highlighter: { show: false }
            }
          }
        }
    );
  }

};
