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
                    <div class="panel-heading">账户管理</div>
                    <div class="panel-body clearfix">
                        <a href="input.do" class="btn btn-add">增加账户</a>
                        <form class="pull-right search-form">
                            <span>用户姓名</span><input name="userName" type="text">
                            <button type="submit" class="btn btn-default">搜索</button>
                        </form>
                    </div>
                    <div class="panel-body">
                        <table id="table-user" class="table table-maintain">
                            <thead>
                                <tr>
                                    <th>所在地</th>
                                    <th>登陆帐号</th>
                                    <th>用户姓名</th>
                                    <th>注册时间</th>
                                    <th style="width: 65px;">状态</th>
                                    <th style="width: 270px;">操作</th>
                                </tr>
                            </thead>
                            <tbody role="items">
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer clearfix">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="tmpl-userItem">
    <td>{{companyAddress}}</td>
    <td>{{loginName}}</td>
    <td>{{userName}}</td>
    <td>{{createDateText}}</td>
    <td class="status">
        <# if (status == '1') {#>
            <span class="status-enable">启用</span>
        <# } else { #>
            <span class="status-disable">停用</span>
        <# } #>
    </td>
    <td class="operation">
        <div>
        <# if (status == '1') {#>
            <a href="javascript:" class="do-disable two-letter" data-do="changeStatus" data-status="0">停用</a>
        <# } else { #>
            <a href="javascript:" class="do-enable two-letter" data-do="changeStatus" data-status="1">启用</a>
        <# } #>
        </div>
        <div><a href="javascript:" class="four-letter" data-do="resetPassword">密码重置</a></div>
        <div><a href="input.do?id={{id}}" class="two-letter">编辑</a></div>
        <div><a href="javascript:" class="do-delete two-letter" data-do="delete">删除</a></div>
    </td>
</script>
<script type="text/html" id="tmpl-pagination">
    <div class="pageinfo">共计：{{total}}条 ,每页：{{pageSize}}条</div>
    <div class="page-box">
        <ul class="pagination clearfix">
            <li class="{{pageNum <= 1 ? 'disabled' : ''}}">
                <a href="javascript:" data-do="first">首页</a>
            </li>
            <li class="{{pageNum <= 1 ? 'disabled' : ''}}">
                <a href="javascript:" data-do="prev">上一页</a>
            </li>
            <# _.each(pagelist, function (page, index) { #>
                <# if (index > 0 && (page - pagelist[index - 1]) > 1) { #>
                    <li><span>...</span></li>
                <# } #>
                <li class="{{page === pageNum ? 'active' : ''}}">
                    <a href="javascript:" aria-label="第{{page}}页" data-do="pagechange">{{page}}</a>
                </li>
            <# }) #>
            <li class="{{pageNum >= pages ? 'disabled' : ''}}">
                <a href="javascript:" data-do="next">下一页</a>
            </li>
            <li class="{{pageNum >= pages ? 'disabled' : ''}}">
                <a href="javascript:" data-do="last">末页</a>
            </li>
        </ul>
    </div>
</script>

<script>
    seajs.use('page/user/manager', function(page){ 
        page.run(); 
    });
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>