var page = {

  init: function () {

  }

};

function PieChart(contentId, updatePeriod, options) {

  var bodyElement = $("#"+contentId);
  var divElement = $("#"+contentId+"_div");
  var closestForm = bodyElement.parents("form");
  var requestUrl = closestForm.attr("action");

  var immediatlyRender = options['immediatly_render'];

  var checkUpdate = function() {
    window.setTimeout(
        callUpdate, updatePeriod * 1000)
  };

  var g = new PieGraph();

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

  function PieGraph() {

    var element = $('#'+contentId);


    var plot1;

    this.replot = function() {
      if(plot1 != null) {
        plot1.replot();
      }
    };

    this.drawPie = function(response) {
      if(response == null) {
        return;
      }

      var responseObject = !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
          response.replace(/"(\\.|[^"\\])*"/g, ''))) &&
          eval('(' + response + ')');

      element.empty();
      var pie = responseObject.pie;

      var points = [];
      var color = [];


      for(var i=0; i<pie.length; i++) {

        points[i] = [pie[i].legend, pie[i].value];
        color[i] = pie[i].color;

      }

      if(points.length == 0) {
        return;
      }

      plot1 = $.jqplot(contentId, [points], {
        seriesColors: color,
        seriesDefaults: {
          // Make this a pie chart.
          renderer: jQuery.jqplot.PieRenderer,
          rendererOptions: {
            // Put data labels on the pie slices.
            // By default, labels show the percentage of the slice.
            showDataLabels: true
          }
        },
        legend: { show:true, location: 'e' }
      });

    }
  }

  var callUpdate = function () {

    var onResponse = function(text, status, resp) {
      g.drawPie(text);
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
      g.drawPie(immediatlyRender);
    }else {
      callUpdate();
    }
  });


}
