package mobi.eyeline.ips.service

import mobi.eyeline.ips.messages.UssdResponseModel

class UssdServiceUtils {

  @SuppressWarnings("GrMethodMayBeStatic")
  static Map<String, String[]> asMultimap(Map map) {
    map.collectEntries { k, v -> [(k.toString()): [v.toString()] as String[]] } as Map<String, String[]>
  }

  @SuppressWarnings("GrUnresolvedAccess")
  UssdResponseModel request(params) { ussdService.handle(new MockOuterRequest(params)) }
}
