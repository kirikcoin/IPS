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

  replaceName: function($element, dialogId, login, fullName) {
    var $content = $element.find('.confirmDialog_content');
    var text = ips.$byId(dialogId+'_hiddenText').text();
    $content.text(text.replace('{username}', login).replace('{fullName}', fullName));
  },

  showBlockDialog: function (dialogId, id, login, fullName) {
    page.replaceName($('#clientBlockDialog_div'),dialogId, login, fullName);

    ips.$byId('userId').val(id);
    jsfc('clientBlockDialog').show();
    return false;
  },

  showUnblockDialog: function (dialogId, id, login, fullName) {
    page.replaceName($('#clientUnblockDialog_div'),dialogId, login, fullName);

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

  showPasswordResetDialog: function (dialogId, id, email, login, fullName) {
    page.replaceName($('#clientPasswordResetDialog_div'), dialogId, login, fullName);

    ips.$byId('userId').val(id);
    jsfc('clientPasswordResetDialog').show();
    return false;
  }
};
