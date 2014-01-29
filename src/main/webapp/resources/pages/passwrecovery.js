var page = {

    login: function () {
        var l = $('#recoveryEmail');
        l.val($.trim(l.val()));
        document.getElementById('rpForm').submit();
    },

    init: function () {
        var loginFunc = this.login;
        $('#j_password').keypress(function (e) {
            if (e.which == 10 || e.which == 13) //Enter pressed
                loginFunc();
        });
        $('#recoveryEmail').focus();
    }



};
