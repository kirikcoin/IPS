var page = {
  onEditSettingsClick: function() {
    $('#settingsDisplay').hide();
    $('#settingsDialog').show();

    page.disableEditables();

    return false;
  },

  onMadvEditClick: function() {
    $('#madvDisplay').hide();
    $('#madvEdit').show();

    page.disableEditables();

    return false;
  },

  onMadvEditCancel: function() {
    $('#madvEdit').hide();
    $('#madvDisplay').show();

    page.enableEditables();
    ips.message.hideAll();

    return false;
  },

  disableEditables: function() {

  },

  enableEditables: function() {
    $('#deleteButton, a.modify, a.modify-left, #questionsList_body .eyeline_buttons').show();
  },

  onRemoveBindingClick: function() {
    jsfc('removeBindingDialog').show();
    return false;
  }

};
