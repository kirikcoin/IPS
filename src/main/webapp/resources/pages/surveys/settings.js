var page = {

  init: function () {

    function wireModificationLink(groupId) {
      var $header = ips.$byId(groupId + '_header');
      $header.click(function () {
        var $link = $header.find('a');
        var $body = ips.$byId(groupId + '_body');

        if ($body.is(':visible')) {
          $link.show();
        } else {
          $link.hide();
        }
      });
    }

    $(function () {
      $.each(['groupEndMessage', 'groupSettings', 'questionsList'], function(i, e) {
        wireModificationLink(e);
      });
    });

  },

  showQuestionDeleteDialog: function (id) {
    ips.$byId('questionId').val(id);
    jsfc('questionDeleteDialog').show();
    return false;
  },

  lookup: function(id) {
    var elem = jsfc(id);
    if (!elem) {
      elem = $('#' + id);
    }

    return elem;
  },

  onDialogCancel: function (id) {
    var elem = page.lookup(id);

    elem.hide();
    ips.message.hideAll();
    return false;
  },

  disableEditables: function() {
    $('#deleteButton, a.modify, a.modify-left, #questionsList_body .eyeline_buttons').hide();
  },

  enableEditables: function() {
    $('#deleteButton, a.modify, a.modify-left, #questionsList_body .eyeline_buttons').show();
  },

  onEditEndMessageClick: function() {
    $('span[id^=surveyEndText]').hide();
    $('#endMessageDialog').show();

    page.disableEditables();

    return false;
  },

  onEditEndMessageCancel: function() {
    $('span[id^=surveyEndText]').show();
    $('#endMessageDialog').hide();

    $('#endText').val($('#surveyEndText').text());

    page.enableEditables();

    return false;
  },

  onEditSettingsClick: function() {
    $('#settingsDisplay').hide();
    $('#settingsDialog').show();

    page.disableEditables();

    return false;
  },

  onEditSettingsCancel: function() {
    $('#settingsDialog').hide();
    $('#settingsDisplay').show();

    page.enableEditables();
    ips.message.hideAll();

    return false;
  }

};