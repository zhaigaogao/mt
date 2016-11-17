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
                <c:set var="menuActive" value="enterprise"></c:set>
                <%@ include file="/WEB-INF/views/common/menu.jsp"%>
            </div>
            <div class="col-maintain col-sm-9 col-md-10 col-lg-10">
                <div class="panel panel-maintain panel-maintain-content">
                    <div class="panel-heading">企业管理</div>
                    <div class="panel-body clearfix">
                        <a href="input.do" class="btn btn-add">新建企业账号</a>
                        <a href="javascript:" class="btn btn-submit" data-do="openImportVwt">非V网通用户同步</a>
                        <form class="pull-right search-form">
                            <span>企业名称</span><input name="customerName" type="text">
                            <span>企业联系人</span><input name="contactName" type="text">
                            <button type="submit" class="btn btn-default">搜索</button>
                        </form>
                    </div>
                    <div class="panel-body">
                        <table id="table-enterprise" class="table table-maintain">
                            <thead>
                                <tr>
                                    <th>企业所在地</th>
                                    <!--  <th>企业帐号</th>-->
                                   <th>企业名称</th>
                                    <th>管理员帐号</th>
                                    <th>管理员姓名</th>
                                    <th>注册时间</th>
                                    <th style="width: 185px;">操作</th>
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

<script type="text/html" id="tmpl-enterpriseItem">
    <td>{{companyAddress}}</td>
    <td>{{customerName}}</td>
    <td>{{contactUserName}}</td>
    <td>{{contactName}}</td>
    <td>{{registerDateText}}</td>
    <td class="operation">
        <div><a href="javascript:" class="two-letter" data-do="resetPassword">密码重置</a></div>
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
<script id="tmpl-importVwtModal" type="text/html">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></span>
                </button>
                <h4 class="modal-title">非V网通用户同步</h4>
            </div>
            <div class="modal-body">
                <div class="approval-setting-body row">
                    <div class="panel-main col-sm-12 col-md-12 col-lg-12">
                        <form>
                            <!--<div class="form-group">
                                <label class="control-label" for="orgName">公司名称</label>
                                <input name="companyName" class="form-control" value="">
                            </div>
                            <div class="form-group">
                                <label class="control-label" for="showindex">管理员手机</label>
                                <input name="customerManagerMobile" class="form-control" value="">
                            </div>-->
                            <div class="form-group">
                                <label class="control-label" for="showindex">公司编码</label>
                                <input name="ecode" class="form-control" value="">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" data-do="save">确定</button>
            </div>
        </div>
    </div>
</script>

<script>
    seajs.use('page/enterprise/manager', function(page){
        page.run();
    });
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
