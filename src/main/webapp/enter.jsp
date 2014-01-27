<%
  if(request.isUserInRole("admin"))
    response.sendRedirect(request.getContextPath() + "/pages/section1/");
  else
    response.sendError(403, "In development");
%>