var page = {

  login: function () {
    var l = $('#j_username');
    l.val($.trim(l.val()));
    document.getElementById('opForm').submit();
  },

  init: function () {
    var loginFunc = this.login;
    $('#j_password').keypress(function (e) {
      if (e.which == 10 || e.which == 13) //Enter pressed
        loginFunc();
    });
    $('#j_username').focus();
  }



};
