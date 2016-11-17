<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/head-public.jsp"%>
<%@ include file="/WEB-INF/views/common/ie.jsp"%>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<div class="content">
    <div class="container-fulti">        
        <div class="row row-maintain">
            <div class="col-maintain col-sm-3 col-md-2 col-lg-2">
                <c:set var="menuActive" value="user"></c:set>
                <%@ include file="/WEB-INF/views/common/menu.jsp"%>
            </div>
            <div class="col-maintain col-sm-9 col-md-10 col-lg-10">
                <div class="panel panel-maintain panel-maintain-content">
                    <div class="panel-heading">账户表单</div>
                    <div class="panel-body">
                        <form id="form" class="form-maintain" style="visibility: hidden;">
                            <input type="hidden" name="id" value="${mtuser.id}">
                            <input type="hidden" name="deptId" value="${mtuser.deptId}">
                            <div class="form-group required">
                                <label for="loginName" class="control-label">登录账号</label>
                                <input type="text" class="form-control" name="loginName" value="${mtuser.loginName}" placeholder="登录账号">
                            </div>
                            <div class="form-group required">
                                <label for="password" class="control-label">登录密码</label>
                                <input type="password" class="form-control" name="password" value="" placeholder="登录密码">
                            </div>
                            <div class="form-group required">
                                <label for="plainPassword" class="control-label">确认登录密码</label>
                                <input type="password" class="form-control" name="plainPassword" value="" placeholder="确认登录密码">
                            </div>
                            <div class="form-group required">
                                <label for="userName" class="control-label">用户姓名</label>
                                <input type="text" class="form-control" name="userName" value="${mtuser.userName}" placeholder="用户姓名">
                            </div>

                            <div class="form-group required">
                                <label class="control-label">企业所在地</label>
                                <select class="form-control" name="province" role="province-place">
                                    <option value="-1">请选择省</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <select class="form-control" name="city" role="city-place">
                                    <option value="-1">请选择市</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <select class="form-control" name="area" role="area-place">
                                    <option value="-1">请选择市辖区、县、县级市</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label class="control-label">性别</label>
                                <div class="checkbox">
                                    <label>
                                        <input type="radio" name="sex" value="1" ${(mtuser.sex == 1 || empty mtuser.sex) ? "checked" : ""}> 男
                                    </label>
                                    <label>
                                        <input type="radio" name="sex" value="0" ${mtuser.sex == 0 ? "checked" : ""}> 女
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="control-label">邮箱</label>
                                <input type="text" class="form-control" name="email" value="${mtuser.email}" placeholder="邮箱">
                            </div>
                            <div class="form-group required">
                                <label for="mobile" class="control-label">手机号码</label>
                                <input type="text" class="form-control" name="mobile" value="${mtuser.mobile}" placeholder="手机号码">
                            </div>
                            <div class="form-group required">
                                <label class="control-label">是否管理员</label>
                                <div class="checkbox">
                                    <label>
                                        <input type="radio" name="isAdmin" value="1" ${mtuser.isAdmin == 1 ? "checked" : ""}> 是
                                    </label>
                                    <label>
                                        <input type="radio" name="isAdmin" value="0" ${(mtuser.isAdmin == 0 || empty mtuser.isAdmin) ? "checked" : ""}> 否
                                    </label>
                                </div>
                            </div>
                            <div class="form-group button-group">
                                <button type="submit" class="btn btn-submit">保存</button>
                                <a href="manager.do" class="btn btn-cancle">返回</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script  type="text/html" id="tmpl-areas">
    <option value="-1" selected>{{level === 1 ? '请选择省' : ( level === 2 ? '请选择市' : '请选择市辖区、县、县级市')}}</option>
    <# _.each(areas, function (area) { #>
        <option value="{{area.id}}">{{area.depName}}</option>
    <# }) #>
</script>

<script>
    seajs.use('page/user/input', function(page){ 
        var deptId = '${sessionScope.user.deptId}';
        page.run(deptId); 
    });
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>