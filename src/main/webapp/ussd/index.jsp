<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="mobi.eyeline.ips.messages.MissingParameterException" %>
<%@ page import="mobi.eyeline.ips.messages.UssdResponseModel" %>
<%@ page import="mobi.eyeline.ips.service.Services" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.http.HttpStatus" %>
<%@ page language="java"
         trimDirectiveWhitespaces="true"
         contentType="text/xml; charset=utf-8" %>

<%
  final UssdResponseModel model;
  try {
    @SuppressWarnings("unchecked")
    final Map<String, String[]> parameters =
        (Map<String, String[]>) request.getParameterMap();

    model = Services.instance().getUssdService().handle(parameters);
    pageContext.setAttribute("model", model);

  } catch (MissingParameterException e) {
    response.setStatus(HttpStatus.SC_BAD_REQUEST);      // Avoid displaying HTTP-400 error page.
    response.setContentLength(0);                       // Avoid sending empty lines.
  }
%>

<?xml version="1.0" encoding="utf-8"?>

<c:if test="${not empty model}">
  <page version="2.0">

    <div>
      <c:out value="${model.text}"/>
      <br/>
    </div>

    <c:if test="${not empty model.options}">
      <navigation>

        <c:forEach items="${model.options}" var="option">
          <link accesskey="${option.key}" pageId="index.jsp?${fn:escapeXml(option.uri)}">
            <c:out value="${option.text}"/>
          </link>
        </c:forEach>

      </navigation>
    </c:if>

  </page>
</c:if>