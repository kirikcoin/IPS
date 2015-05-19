package mobi.eyeline.ips.service

import mobi.eyeline.ips.messages.OuterRequest

import javax.servlet.http.HttpServletRequest

class MockOuterRequest extends OuterRequest {

  private final Map params

  MockOuterRequest(Map params, HttpServletRequest request = new HttpRequestMock()) {
    super(request)
    this.params = params
  }

  @Override
  Map<String, String[]> getUrlParams() {
    UssdServiceUtils.asMultimap(params)
  }

}
