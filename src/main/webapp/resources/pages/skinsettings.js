var page = {
  init: function () {

  },

  onLogoModifyDialogShow: function() {
    $('#logoModifyDialog').show();
    $('.displayed').hide();
  //  jsfc('logoModifyDialog').show();

    return false;
  },


  onLogoModifyDialogHide: function() {
    $('#logoModifyDialog').hide();
    $('.displayed').show();
   // jsfc('logoModifyDialog').hide();
    return false;
  }



};