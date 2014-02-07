<?xml version="1.0" encoding="utf-8"?>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="mobi.eyeline.ips.messages.MissingParameterException" %>
<%@ page import="mobi.eyeline.ips.messages.UssdModel" %>
<%@ page import="mobi.eyeline.ips.service.Services" %>
<%@page language="java" contentType="text/xml; charset=utf-8" %>

<%
  UssdModel model = null;
  try {
    model = Services.instance().getUssdService().handle(request);

  } catch (MissingParameterException e) {
    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
  }

  pageContext.setAttribute("model", model);
%>

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