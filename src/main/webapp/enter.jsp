<%
  if (request.isUserInRole("manager") || request.isUserInRole("client")) {
    response.sendRedirect(request.getContextPath() + "/pages/surveys/index.faces");

  } else {
    response.sendError(403, "In development");
  }
%>