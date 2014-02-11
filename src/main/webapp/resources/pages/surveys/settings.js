var page = {

  onCreateSurveyClick: function () {
    jsfc('newSurveyDialog').show();

    return false;
  },

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

  onDialogCancel: function (id) {
    jsfc(id).hide();
    ips.message.hideAll();
    return false;
  },

  addQuestion: function () {
    ips.$byId('questionId').val('');

    return false;
  }
};