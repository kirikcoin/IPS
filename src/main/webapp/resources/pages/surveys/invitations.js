var page = {

  init: function () {

    $("#deliveryType").change(function(){
      $('#invitationTextBlock').toggle($('#deliveryType').val()=="USSD_PUSH");
      $('#invitationText').val("");
    });

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
  }
};
