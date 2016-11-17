<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="./common/head-public.jsp"%>
<%@ include file="./common/ie.jsp"%>
<link rel="stylesheet" href="${contextPath}/assets/src/css/login.css" />
</head>
<body>
<form id="form-login" class="form-login" method="post" action="${contextPath}/checkPass.do" style="display: none;">
    <div class="form-login-heading"></div>
    <div class="form-login-content">
        <h3 class="form-login-title">江苏移动审批服务平台</h3>
        <label for="loginName" class="sr-only">手机号</label>
        <input type="text" id="loginName" name="loginName" class="form-control user" placeholder="用户名" autofocus autocomplete="off">
        <label for="password" class="sr-only">密码</label>
        <input type="password" id="password" name="password" class="form-control password" placeholder="密码" autocomplete="off">
        <button class="btn btn-login" type="submit">登录</button>
    </div>
</form>
<script>
    seajs.use('page/login', function(page){ 
        page.run(); 
    });
</script>
</body>
</html>
