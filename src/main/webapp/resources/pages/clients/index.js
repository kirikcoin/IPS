var page = {

  nameFilterKeyDown : function (event) {
    if (event.keyCode == 13) {
      jsfc('table').update(true);
      return false;
    } else {
      return true;
    }
  },

  init: function () {
    ips.$byId("search").focus();
  },

  replaceName: function($element, login, fullName) {
    var $content = $element.find('.confirmDialog_content');
    var text = $content.text();
    $content.text(text.replace('{username}', login).replace('{fullName}', fullName));
  },

  showBlockDialog: function (id, login, fullName) {
    page.replaceName($('#clientBlockDialog_div'), login, fullName);

    ips.$byId('userId').val(id);
    jsfc('clientBlockDialog').show();
    return false;
  },

  showUnblockDialog: function (id, login, fullName) {
    page.replaceName($('#clientUnblockDialog_div'), login, fullName);

    ips.$byId('userId').val(id);
    jsfc('clientUnblockDialog').show();
    return false;
  },

  showSettingsDialog: function () {
    jsfc('clientSettingsDialog').show();
    return false;
  },

  hideSettingsDialog: function() {
    jsfc('clientSettingsDialog').hide();
    ips.message.hideAll();
    return false;
  },

  showPasswordResetDialog: function (id, email, login, fullName) {
    page.replaceName($('#clientPasswordResetDialog_div'), login, fullName);

    ips.$byId('userId').val(id);
    jsfc('clientPasswordResetDialog').show();
    return false;
  }
};
