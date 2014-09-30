var page = {

  /**
   * @param {string} authUrl Authentication servlet URL.
   */
  init: function (authUrl) {
    $('#j_password').keypress(function (e) {
      if (e.which == 10 || e.which == 13) {
        // Enter pressed.
        e.preventDefault();
        page.login(authUrl);
      }
    });
    $('#j_username').focus();
  },

  /**
   * @param {string} authUrl Authentication servlet URL.
   */
  login: function (authUrl) {
    var $form = $('<form>', {
      name:     'opForm',
      method:   'post',
      action:   authUrl,
      enctype:  'application/x-www-form-urlencoded',
      style:    'display: none'
    });

    $('<input>', {
      name: "j_username",
      value: $.trim($('#j_username').val())
    }).appendTo($form);

    $('<input>', {
      name: "j_password",
      value: $('#j_password').val()
    }).appendTo($form);

    $form.appendTo($('body'));

    $form.submit();
  }

};
