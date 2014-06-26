var page = {

  nameFilterKeyDown: function (event) {
    if (event.keyCode == 13) {
      jsfc('table').update(true);
      return false;
    } else {
      return true;
    }
  },

  init: function () {
    ips.$byId('search').focus();
  },

  onAddNumberClick: function () {
    $('#newNumberValue').val('');
    jsfc('newAccessNumberDialog').show();

    return false;
  },

  showDeleteDialog: function (id, number) {
    var $content = $('#deleteNumberDialog_div').find('.confirmDialog_content');
    var text = $content.text();
    $content.text(text.replace('{number}', number));

    ips.$byId('numberId').val(id);
    jsfc('deleteNumberDialog').show();
    return false;
  }
};