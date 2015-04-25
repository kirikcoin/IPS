package mobi.eyeline.ips.util

public class DelegateResourceBundle extends ResourceBundle {
  private final ResourceBundle delegate

  DelegateResourceBundle(ResourceBundle delegate) {
    this.delegate = delegate
  }

  @Override
  Object handleGetObject(String key) {
    delegate.handleGetObject(key)
  }

  @Override
  Enumeration<String> getKeys() {
    delegate.getKeys()
  }

}