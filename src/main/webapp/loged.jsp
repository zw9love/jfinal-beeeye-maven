<%--
  Created by IntelliJ IDEA.
  User: machinly
  Date: 2016/9/7
  Time: 08:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录中...</title>
</head>
<body>
<form method="post" action="/" id="loged">
    <input type="text" name="token" value="${ token }" hidden>
    <input type="text" name="role" value="${ role }" hidden>
</form>
<script>
    document.getElementById("loged").submit();
</script>
</body>
</html>
