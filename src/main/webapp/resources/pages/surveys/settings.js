var page = {

  init: function () {

    function wireModificationLink(groupId) {
      var $header = ips.$byId(groupId + '_header');
      $header.click(function (e) {
        if (e.target.id == $header.attr('id')) {
          var $link = $header.find('a');
          var $body = ips.$byId(groupId + '_body');

          if ($body.is(':visible')) {
            $link.show();
          } else {
            $link.hide();
          }
        }
      });
    }

    $(function () {
      $.each(['groupEndMessage', 'groupSettings', 'questionsList'], function(i, e) {
        wireModificationLink(e);
      });

      $(".eyeline_addbutton").click(function() {
        $("#scrollableVariants").scrollTop(10000);
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
  },

  onQuestionModificationDialog: function() {
    jsfc('questionModificationDialog').show();
  },

  onPreviewClick: function() {
    jsfc('surveyPreviewDialog').show();

    return false;
  },

  onQuestionSave: function (errorMessage) {

    var $inputs = $('[id^=questionModificationDialog]')
        .find('input[id^=questionOptions][id$=answer][id!=questionOptions_newcell_answer]');

    var isInputValid = function($e) { return $e.value.length };
    var isFormValid = true;

    $inputs
        .filter(function (i) { return isInputValid(this) })
        .each(function (i, e) { ips.message.hide(e.id); });


    $inputs
        .filter(function (i) { return !isInputValid(this) })
        .each(function (i, e) {
          ips.message.error(errorMessage, e.id, true);
          isFormValid = false;
        });

    return isFormValid;
  }

};