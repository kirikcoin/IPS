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

      jsfc('questionOptions').addListener(function (event) {
        if (event.type == 'added') {
          var $outer = $("#scrollableVariants");
          var $inner = $outer.find('table');
          $outer.scrollTop($inner.height() - $outer.height());
        }
      });

      page.onEndSmsEnabledChange($('#endSmsEnabled')[0]);
    });

    var treeInitialized = false;
    $(function () {
      $('#tabs > div').show();
      if ($('.ui-tabs-nav li:nth-child(2)').hasClass('ui-state-active')) {
        treeInitialized = true;
        jsfc('questionsTree').init(true);
      }
      $('#tabs').on('tabsshow', function (event, ui) {
        if ((ui.index == 1) && !treeInitialized) {
          treeInitialized = true;
          jsfc('questionsTree').init(true);
        }
      });
    });

  },

  showSurveyDeleteDialog: function (id) {
    jsfc('deleteDialog').show();
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
    $('#endMessageView').hide();
    $('#endMessageDialog').show();

    page.disableEditables();

    return false;
  },

  onEditEndMessageCancel: function() {
    $('#endMessageView').show();
    $('#endMessageDialog').hide();

    $('#endText').val($('#surveyEndText').text());

    page.enableEditables();

    return false;
  },

  onEditSettingsClick: function() {
    $('.settingsDisplay').hide();
    $('#settingsDialog').show();

    page.disableEditables();

    return false;
  },

  onEditSettingsCancel: function() {
    $('#settingsDialog').hide();
    $('.settingsDisplay').show();

    $("#newSurveyStartDate").val($("#settingsStartDate").val());
    $("#newSurveyEndDate").val($("#settingsEndDate").val());

    page.enableEditables();
    ips.message.hideAll();

    return false;
  },


  onQuestionModificationDialog: function() {
    jsfc('questionModificationDialog').show();
  },

  onExtLinkModificationDialog: function () {
    jsfc('extLinkModificationDialog').show();
  },

  onQuestionDeleteDialog: function() {
    jsfc('questionDeleteDialog').show();
  },

  onExtLinkDeleteDialog: function() {
    jsfc('extLinkDeleteDialog').show();
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

    if (!isFormValid) {
      var $btn = $('#saveQuestionLink');
      $btn.attr('onclick', $btn.data('oldclick'));
    }

    return isFormValid;
  },

  onEndSmsEnabledChange: function (self) {
    $('#endSmsDetails, .endSmsDetails').toggle(self.value == 'SMS' || self.value == 'COUPON');
    $('#couponDetails, #couponHint').toggle(self.value == 'COUPON');
    return false;
  },

  // XXX: In case tree component is extracted to the component library,
  // this should be done in the renderer.
  setTreeLabels: function (zoomIn, zoomOut, zoomReset) {
    var $toolbar = $('.eyeline_tree_toolbar');
    $toolbar.find('.zoom_in').attr('title', zoomIn);
    $toolbar.find('.zoom_out').attr('title', zoomOut);
    $toolbar.find('.zoom_reset').attr('title', zoomReset);
  }
};