var page = {

  init: function () {
    $('#j_password').keypress(function (e) {
      if (e.which == 10 || e.which == 13) //Enter pressed
        page.login();
    });
    $('#j_username').focus();
  },

  login: function () {
    var l = $('#j_username');
    l.val($.trim(l.val()));
    document.getElementById('opForm').submit();
  }

};
