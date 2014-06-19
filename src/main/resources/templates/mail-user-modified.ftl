<#-- @ftlvariable name="user" type="mobi.eyeline.ips.model.User" -->
<#-- @ftlvariable name="loginUrl" type="java.lang.String" -->

<#include "mail-base.ftl"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<@email_page_head/>

<body>
<table cellpadding="0" cellspacing="0" border="0" id="backgroundTable">

  <tr>
    <td>
      <h3>${user.fullName}!</h3>
    </td>
  </tr>

  <tr>
    <td>
    ${bundle["email.user.modified.text"]}
      <ul>
        <li>
        ${bundle["email.message.login"]} ${user.login}
        </li>
        <li>
          Email: ${user.email}
        </li>
      </ul>
    </td>
  </tr>

  <tr>
    <td>
      <p>
      ${bundle["email.message.link.text"]} <a href="${loginUrl}" target="_blank">${bundle["email.message.link"]}</a>.
      </p>
    </td>
  </tr>

</table>
</body>
