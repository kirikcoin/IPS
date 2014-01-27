<%
  response.sendRedirect(request.getContextPath() + "/error.faces?id="+request.getParameter("id"));
%>