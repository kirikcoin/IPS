var page = {

  init: function () {

  }

};

function BarChart(contentId, updatePeriod, options) {

  var bodyElement = $("#"+contentId);
  var divElement = $("#"+contentId+"_div");
  var closestForm = bodyElement.parents("form");
  var requestUrl = closestForm.attr("action");
  var horizontal = options['horizontal'];
  var stackMode = options['stackMode'];
  var number_ticks = options['ticks'];
  var intValues = options['intValues'];
  var immediatlyRender = options['immediatly_render'];

  var checkUpdate = function() {
    window.setTimeout(
        callUpdate, updatePeriod * 1000)
  };

  var g = new BarGraph();

  this.setVisible = function(visible) {
    if(visible) {
      divElement.show();
      g.replot();
    }else {
      divElement.hide();
    }
    $('#' + contentId + "_visible").val(visible);
  };

  this.update = function() {
    callUpdate();
  };


  this.replot = function() {
    g.replot();
  };

  var _setVisible = this.setVisible;

  this.show = function() {
    _setVisible(true);
  };

  this.hide = function() {
    _setVisible(false);
  };

  function BarGraph() {

    var element = $('#'+contentId);


    var plot1;

    this.replot = function() {
      if(plot1 != null) {
        plot1.replot();
      }
    };

    function get_ticks_numb(userInt, userTicks, points_number) {
      var ticks_numb = null;
      if(userInt != null && userInt) {
        ticks_numb = 10;
      }
      if(userTicks != null) {
        ticks_numb = userTicks;
      }

      if(ticks_numb != null) {
        ticks_numb = Math.min(ticks_numb, points_number);
      }
      return ticks_numb;
    }


    function get_ticks(ticks_numb, userInt, minP, maxP) {
      var ticks = [];
      if(minP>0) {
        minP = 0;
      }
      if(ticks_numb != null) {
        var interval;
        if(userInt != null && userInt) {
          var tmp = parseInt(minP);
          if(tmp > minP) {
            tmp--;
          }
          minP = tmp;
          tmp = parseInt(maxP);
          if(tmp < maxP) {
            tmp++;
          }
          maxP = tmp;

          interval = Math.max(parseInt((maxP - minP)/ticks_numb),1);
        }else {
          interval = (maxP - minP) / ticks_numb;
        }
        if(interval>0) {
          var t = -1;
          do {
            t++;
            ticks[t] = minP+(t*interval);
          }while(ticks[t]<maxP);
        }else {
          ticks[0] = minP;
        }
      }
      return ticks;
    }

    this.drawBars = function(response) {
      if(response == null) {
        return;
      }

      var responseObject = !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
          response.replace(/"(\\.|[^"\\])*"/g, ''))) &&
          eval('(' + response + ')');

      element.empty();
      var bars = responseObject.bars;
      var ticks = responseObject.ticks;

      var points = [];
      var legend = [];
      var color = [];

      var minY, maxY;

      var y_points_number = 0;
      var value_set = new Object();
      for(var i=0; i<bars.length; i++) {
        var bar = [];
        var j = 0;

        for(var key in bars[i].values) {
          var value = bars[i].values[key];
          var valueD = parseFloat(value);
          bar[j++] =  horizontal ? [valueD, key] : [key, valueD];

          minY = minY == null ? valueD : Math.min(minY, valueD);
          maxY = maxY == null ? valueD : Math.max(maxY, valueD);

          var value_str = value+'';
          if(!(value_str in value_set)) {
            value_set[value_str] = 1;
            y_points_number++;
          }
        }

        points[i] = bar;
        legend[i] = {label:bars[i].legend};
        color[i] = bars[i].color;
      }

      if(points.length == 0) {
        return;
      }

      var y_ticks_numb = get_ticks_numb(intValues, number_ticks, y_points_number);

      var y_ticks = get_ticks(y_ticks_numb, intValues, minY, maxY);

      plot1 = $.jqplot(contentId, points, {
        stackSeries : stackMode,
        seriesColors: color,
        series:legend,
        legend: {
          show: true,
          location: horizontal ? 's' : 'w'
        },
        seriesDefaults:{
          renderer:$.jqplot.BarRenderer,
          rendererOptions: {
            fillToZero: true,
            barDirection: horizontal ? 'horizontal' : 'vertical',
            barWidth: 70
          }
        },
        axes:{
          xaxis:
              !horizontal ? {
                renderer:$.jqplot.CategoryAxisRenderer,
                ticks: ticks
              } :
                  y_ticks_numb == null ? (
                      intValues ? {tickOptions:{formatString: '%d'}}
                          : {}
                      ) : (
                      intValues ?  {ticks : y_ticks, tickOptions:{formatString: '%d'}}
                          : {ticks : y_ticks}
                      ),
          yaxis:
              horizontal ? {
                renderer:$.jqplot.CategoryAxisRenderer,
                ticks: ticks,
                tickOptions: {
                  show: false
                }
              } :
                  y_ticks_numb == null ? (
                      intValues ? {tickOptions:{formatString: '%d'}}
                          : {}
                      ) : (
                      intValues ?  {ticks : y_ticks, tickOptions:{formatString: '%d'}}
                          : {ticks : y_ticks}
                      )
        },
        highlighter: {
          show: true,
          tooltipAxes : horizontal ? "x" : "y",
          tooltipFormatString: '%s',
          useAxesFormatters: false
        }
      });

    }
  }

  var callUpdate = function () {

    var onResponse = function(text, status, resp) {
      g.drawBars(text);
      if(updatePeriod>0) {
        checkUpdate();
      }
    };


    var params = serializeValues(closestForm);
    params["eyelineComponentUpdate"] = contentId;

    $.ajaxSetup({cache: false});
    $.post(requestUrl, params, onResponse);
  };

  $(function() {
    if(immediatlyRender != null) {
      g.drawBars(immediatlyRender);
    }else {
      callUpdate();
    }
  });


}