package mobi.eyeline.ips.service

import groovy.json.JsonSlurper
import groovy.util.logging.Log4j

@Log4j
class MobilizerServiceRegistryClient {

  @SuppressWarnings("GrMethodMayBeStatic")
  String getTelegramBotName(String token) {
    final url = "https://api.telegram.org/bot$token/getMe"

    try {
      new JsonSlurper()
          .parse(url.toURL())
          .with { it?.result?.username }

    } catch (e) {
      log.error "Telegram API request failed, url = [$url]", e
      return null
    }
  }

}
