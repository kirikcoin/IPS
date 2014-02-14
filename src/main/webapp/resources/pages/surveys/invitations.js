var page = {
    onEditSettingsClick: function() {
        $('#settingsDisplay').hide();
        $('#settingsDialog').show();

        page.disableEditables();

        return false;
    }
};
