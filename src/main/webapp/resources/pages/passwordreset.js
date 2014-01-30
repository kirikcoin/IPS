var page = {

    init: function () {
        var $emailInput = $('#recoveryEmail');
        $emailInput.keypress(function (e) {
            if (e.which == 10 || e.which == 13) {
                $('a[id$=sendButton]').click();
                return false;
            }
            return true;
        });

        $emailInput.focus();
    }

};
