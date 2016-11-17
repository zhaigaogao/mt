<%@ page language="java" pageEncoding="utf-8"%>
<div class="container-fulti bg-layer">
    <div class="row row-maintain">
        <div class="col-maintain bg-left col-sm-3 col-md-2 col-lg-2">
        </div>
        <div class="col-maintain bg-right col-sm-9 col-md-10 col-lg-10">
        </div>
    </div>
</div>
<nav class="navbar navbar-maintain navbar-static-top">
    <div class="container-fluid">
        <div class="nav navbar-nav navbar-left logo">
            <img src="${contextPath}/assets/src/css/img/logo-1.png" alt="中国移动">
        </div>
        <span class="nav navbar-nav navbar-left separate"></span>
        <h3 class="nav navbar-nav navbar-left title">
            江苏移动审批服务平台
        </h3>
        <div class="nav navbar-nav navbar-right user">
            <div class="text-right">您好！${user.userName}</div>
            <div class="text-right">
                <a href="${contextPath}/password.do">修改密码</a>
                <a id="exit-btn" href="javascript:">退出</a>
            </div>
        </div>
    </div>
</nav>
