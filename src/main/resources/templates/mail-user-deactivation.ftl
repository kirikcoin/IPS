<#-- @ftlvariable name="user" type="ussd.poll.backend.db.model.User" -->
<#-- @ftlvariable name="password" type="java.lang.String" -->
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
      Ваш аккаунт в системе IPS со следующими учетными данными был деактивирован.
      <ul>
        <li>
          Логин: ${user.login}
        </li>
      </ul>
    </td>
  </tr>

</table>
</body>
