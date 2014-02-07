var page = {

    nameFilterKeyDown : function (event) {
        if(event.keyCode == 13) {
            jsfc('table').update(true);
            return false;
        }
        else {
            return true;
        }
    },

    init: function () {
        ips.$byId("search").focus();
    } ,

    showBlockDialog: function (login) {
        ips.$byId('userLogin').val(login);
        jsfc('clientBlockDialog').show();
        return false;
    },

    showUnblockDialog: function (login) {
        ips.$byId('userLogin').val(login);
        jsfc('clientUnblockDialog').show();
        return false;
    }
};