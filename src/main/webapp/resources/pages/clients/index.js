var page = {

  nameFilterKeyDown : function (event) {
    if(event.keyCode == 13) {
      jsfc('table').update(true);
      return false;
    }
    else {
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

  showBlockDialog: function (login, fullName) {
    page.replaceName($('#clientBlockDialog_div'), login, fullName);

    ips.$byId('userLogin').val(login);
    jsfc('clientBlockDialog').show();
    return false;
  },

  showUnblockDialog: function (login, fullName) {
    page.replaceName($('#clientUnblockDialog_div'), login, fullName);

    ips.$byId('userLogin').val(login);
    jsfc('clientUnblockDialog').show();
    return false;
  },

  showSettingsDialog: function (fullname,company,login,email) {
    ips.$byId('userLoginForEdit').val(login);
    ips.$byId('clientSettingsFullName').val(fullname);
    ips.$byId('clientSettingsCompany').val(company);
    ips.$byId('clientSettingsLogin').val(login);
    ips.$byId('clientSettingsEmail').val(email);
    jsfc('clientSettingsDialog').show();
    return false;
  },

  showPasswordResetDialog: function (email, login, fullName) {
    page.replaceName($('#clientPasswordResetDialog_div'), login, fullName);

    ips.$byId('userEmail').val(email);
    jsfc('clientPasswordResetDialog').show();
    return false;
  }
};
