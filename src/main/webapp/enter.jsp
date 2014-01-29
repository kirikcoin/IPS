<%
  if (request.isUserInRole("manager") || request.isUserInRole("client")) {
    response.sendRedirect(request.getContextPath() + "/pages/surveys/index.faces");

  } else if (request.isUserInRole("admin")) {
    response.sendRedirect(request.getContextPath() + "/pages/section1/");

  } else {
    response.sendError(403, "In development");
  }
%>