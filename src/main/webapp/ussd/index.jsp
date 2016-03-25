<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="mobi.eyeline.ips.messages.MissingParameterException" %>
<%@ page import="mobi.eyeline.ips.messages.UssdResponseModel" %>
<%@ page import="mobi.eyeline.ips.messages.UssdResponseModel.RedirectUssdResponseModel" %>
<%@ page import="mobi.eyeline.ips.service.Services" %>
<%@ page import="org.apache.http.HttpStatus" %>
<%@ page import="static mobi.eyeline.ips.messages.UssdResponseModel.TextUssdResponseModel" %>
<%@ page language="java"
         trimDirectiveWhitespaces="true"
         contentType="text/xml; charset=utf-8" %>

<%
  final UssdResponseModel model;

  try {
    model = Services.getInstance().getUssdService().handle(request);

  } catch (MissingParameterException e) {
    response.setStatus(HttpStatus.SC_BAD_REQUEST);      // Avoid displaying HTTP-400 error page.
    response.setContentLength(0);                       // Avoid sending empty lines.
    return;
  }

  if (model instanceof RedirectUssdResponseModel) {
    response.sendRedirect(model.getRedirectUrl());
    return;
  }

  boolean showFinalMessage = model instanceof TextUssdResponseModel;
  pageContext.setAttribute("model", model);
  pageContext.setAttribute("showFinalMessage", showFinalMessage);
%>

<?xml version="1.0" encoding="utf-8"?>

<c:if test="${not empty model}">
  <page version="2.0">

    <c:choose>
      <c:when test="${model.defaultAnswerEnabled}">
        <div>
          <input navigationId="submit" name="input" title="${model.text}"/>
        </div>
        <navigation id="submit">
          <link accesskey="0" pageId="/ussd/index.jsp?survey_id=${model.surveyId}">OK</link>
        </navigation>
      </c:when>

      <c:otherwise>
        <div>
          <c:out value="${model.text}"/>
          <br/>
        </div>
      </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${empty model.options and not showFinalMessage}">
            <navigation>
                <link/>
            </navigation>
        </c:when>

        <c:when test="${not empty model.options and not showFinalMessage}">
            <navigation>

                <c:forEach items="${model.options}" var="option">

                    <link
                            <c:if test="${not empty option.linkType}">
                                type="${option.linkType}"
                            </c:if>
                            accesskey="${option.key}"
                            pageId="/ussd/index.jsp?${fn:escapeXml(option.uri)}">
                        <c:out value="${option.text}"/>
                    </link>
                </c:forEach>

            </navigation>
        </c:when>
    </c:choose>


  </page>
</c:if>