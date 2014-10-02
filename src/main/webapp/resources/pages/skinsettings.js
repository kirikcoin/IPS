var page = {
  init: function () {

  },

  onLogoModifyDialogShow: function() {
    $('#logoModifyDialog').show();
    $('.displayed').hide();

    return false;
  },

  onLogoModifyDialogHide: function() {
    $('#logoModifyDialog').hide();
    $('.displayed').show();

    this.resetFormElement($('#logo'));

    return false;
  },

  resetFormElement: function (e) {
    e.wrap('<form>').closest('form').get(0).reset();
    e.unwrap();
  }

};