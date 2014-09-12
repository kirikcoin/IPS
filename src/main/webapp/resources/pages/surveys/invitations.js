var page = {

  init: function () {
    var self = this;

    $('#deliveryType').change(function () {
      self.handleDeliveryTypeChange();
      $('#invitationText').val('');
    });

    this.handleDeliveryTypeChange();

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

    wireModificationLink('groupMadv');
  },

  handleDeliveryTypeChange: function () {
    var deliveryType = $('#deliveryType').val();
    $('#invitationTextBlock').toggle(deliveryType == 'USSD_PUSH' || deliveryType == 'SMS');
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

  getEditables: function() {
    return $('#madvNotBound, #addInvitations, #addDelivery, .deliveryActions, .delInvitation, a.modify-left');
  },

  disableEditables: function() {
    this.getEditables().hide();
  },

  enableEditables: function() {
    this.getEditables().show();
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
    $("#inviteDate").val($("#newInviteDate").val());
    ips.message.hideAll();
    return false;
  },

  onNewInviteShow: function() {
    jsfc('newInviteDialog').show();
    $('#inviteValue').val('');
    return false;
  },

  onNewDeliveryCancel: function() {
    jsfc('newDeliveryDialog').hide();
    ips.message.hideAll();
    return false;
  },

  onNewDeliveryShow: function() {
    jsfc('newDeliveryDialog').show();
    return false;
  },

  showActivateDialog: function (id) {
    ips.$byId('deliveryId').val(id);
    jsfc('deliveryActivateDialog').show();
    return false;
  },

  showPauseDialog: function (id) {
    ips.$byId('deliveryId').val(id);
    jsfc('deliveryPauseDialog').show();
    return false;
  }

};
