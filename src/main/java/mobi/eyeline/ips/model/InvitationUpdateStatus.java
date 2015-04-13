package mobi.eyeline.ips.model;

public enum InvitationUpdateStatus {

  UNDEFINED("undefined", "status.undefined"),
  SUCCESSFUL("successful", "status.successful"),
  SERVER_IS_NOT_AVAILABLE("server_is_not_available", "status.server.not.available"),
  CAMPAIGN_NOT_FOUND("campaign_not_found", "status.campaign.not.found");

  private final String name;

  /**
   * Localization key.
   */
  private final String message;

  private InvitationUpdateStatus(String name, String message) {
    this.name = name;
    this.message = message;
  }

  public String getName() {
    return name;
  }

  public String getMessage() {
    return message;
  }

  @SuppressWarnings("UnusedDeclaration")
  public static InvitationUpdateStatus fromName(String name) {
    switch (name) {
      case "undefined":
        return UNDEFINED;
      case "successful":
        return SUCCESSFUL;
      case "server_is_not_available":
        return SERVER_IS_NOT_AVAILABLE;
      case "campaign_not_found":
        return CAMPAIGN_NOT_FOUND;
      default:
        throw new RuntimeException("Unexpected status name: " + name);
    }
  }
}
