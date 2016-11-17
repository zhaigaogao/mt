<%@ page language="java" pageEncoding="utf-8"%>

<div class="panel panel-maintain menu-maintain">
    <div class="panel-heading">企业管理</div>
    <div class="list-group">
        <a href="${contextPath}/group/manager.do" class="list-group-item enterprise ${menuActive == "enterprise" ? "active" : ""}">
            企业管理
        </a>
        <a href="${contextPath}/group/report.do" class="list-group-item enterprise ${menuActive == "enterprise-report" ? "active" : ""}">
            企业报表
        </a>
    </div>
    <div class="panel-heading">系统管理</div>
    <div class="list-group">
        <a href="${contextPath}/mtuser/manager.do" class="list-group-item user ${menuActive == "user" ? "active" : ""}">
            账户管理
        </a>
    </div>
</div>
