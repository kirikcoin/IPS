package mobi.eyeline.ips.service

import groovy.json.JsonSlurper
import groovy.transform.Immutable
import groovy.transform.InheritConstructors
import groovy.util.logging.Log4j

@Log4j
@Immutable
class MobilizerServiceRegistryClient {

  final String registryApiUrl

  @SuppressWarnings("GrMethodMayBeStatic")
  String getTelegramBotName(String serviceId, String token) {

    final rc = checkProperty 'telegram.token', token

    if (!rc?.valid)
      throw new ServiceRegistryException.TokenInvalid()

    if (rc.services && !(serviceId in rc.services))
      throw new ServiceRegistryException.TokenAlreadyTaken()

    try {
      parse("https://api.telegram.org/bot$token/getMe".toURL())
          .with { it?.result?.username }

    } catch (e) {
      log.error "Telegram API request failed", e
      return null
    }
  }


  //
  // Registry API methods.
  //

  private final parse =
      { URL _ -> new JsonSlurper().parse _ }
  private final invoke =
      { String _ -> parse([registryApiUrl, _]*.replaceAll('^/|/$', '').join('/').toURL()) }

  private getServiceProperties =
      { String serviceId -> invoke "/services/$serviceId" }
  private checkProperty =
      { String key, String val -> invoke "/properties/$key/check?value=$val" }

  @InheritConstructors
  static class ServiceRegistryException extends Exception {

    @InheritConstructors
    static class TokenAlreadyTaken extends ServiceRegistryException {}

    @InheritConstructors
    static class TokenInvalid extends ServiceRegistryException {}
  }

}
