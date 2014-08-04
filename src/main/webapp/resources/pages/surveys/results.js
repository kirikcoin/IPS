var page = {


  filterKeyDown: function (event) {
    if (event.keyCode == 13) {
      jsfc('table').update(true);
      return false;
    } else {
      return true;
    }
  },

  init: function () {
    jsfc('table').bind('update', function () {
      var hasResults = jsfc('table').getRowsCount() != 0;
      $('#downloadButtonsPanel').toggle(hasResults);
    });
  }
};
