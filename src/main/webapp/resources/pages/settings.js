var page = {
  init: function () {

  },

  onLogoModifyDialogShow: function() {
    jsfc('logoModifyDialog').show();

    return false;
  },


  onLogoModifyDialogHide: function() {
    jsfc('logoModifyDialog').hide();
    return false;
  }

};