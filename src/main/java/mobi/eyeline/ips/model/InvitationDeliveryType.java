package mobi.eyeline.ips.model;


public enum InvitationDeliveryType {
    USSD_PUSH,
    NI_DIALOG;

    public String toString(){
        String name;
        switch (name()) {
            case "USSD_PUSH":           name="USSD-push"; break;
            case "NI_DIALOG":           name="NI-dialog"; break;
            default: name= "Ussd-push";
        }
        return name;
    }
}
