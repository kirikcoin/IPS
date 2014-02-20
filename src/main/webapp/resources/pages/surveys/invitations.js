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
    $('#madvNotBound, #addInvitations, a.modify-left').hide();
  },

  enableEditables: function() {
    $('#madvNotBound, #addInvitations, a.modify-left').show();
  },

  onRemoveBindingClick: function() {
    jsfc('removeBindingDialog').show();
    return false;
  },

  onDeleteInvite: function(inviteId) {
    $('#inviteId').val(inviteId);
    jsfc('removeInviteDialog').show();
    return false;
  },

  onNewInviteCancel: function() {
    jsfc('newInviteDialog').hide();
    return false;
  },

  onNewInviteShow: function() {
    jsfc('newInviteDialog').show();
    return false;
  }
};
