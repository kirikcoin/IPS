package mobi.eyeline.ips.model;

/**
 * Actually, these are permissions.
 */
public enum Role {

  /**
   * Менеджер со стороны исполнителя (рекламного агенства).
   * Обсуждает параметры опроса с заказчиком и затем публикует его в системе
   */
  MANAGER("manager"),

  /**
   * Менеджер со стороны заказчика.
   * Человек, отвечающий за проведение опросов в режиме конференции.
   */
  CLIENT("client"),

  /**
   * Администратор системы со стороны исполнителя.
   */
  ADMIN("admin");

  private final String name;

  private Role(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static Role fromName(String name) {
    switch (name) {
      case "manager":
        return MANAGER;
      case "client":
        return CLIENT;
      case "admin":
        return ADMIN;
      default:
        throw new RuntimeException("Unexpected role name: " + name);
    }
  }

}
