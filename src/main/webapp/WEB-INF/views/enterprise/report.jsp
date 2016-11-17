<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/head-public.jsp"%>
<%@ include file="/WEB-INF/views/common/ie.jsp"%>
<link rel="stylesheet"
	href="${contextPath}/assets/dep/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" />
<style>
    #table-enterprise-report thead th {
        height: 27px;
    }
    #table-enterprise-report .time-header {
        text-align: center;
    }
</style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<div class="content">
    <div class="container-fulti">
        <div class="row row-maintain">
            <div class="col-maintain col-sm-3 col-md-2 col-lg-2">
                <c:set var="menuActive" value="enterprise-report"></c:set>
                <%@ include file="/WEB-INF/views/common/menu.jsp"%>
            </div>
            <div class="col-maintain col-sm-9 col-md-10 col-lg-10">
                <div class="panel panel-maintain panel-maintain-content">
                    <div class="panel-heading">企业管理</div>
                    <div class="panel-body clearfix">
                        <a id="btn-export" href="javascript:" class="btn btn-default">导出</a>
                        <form class="pull-right search-form">
                            <span>开始时间</span><input role="startDate" name="startDate" type="text" readonly>
                            <span>结束时间</span><input role="endDate" name="endDate" type="text" readonly>
                            <button type="submit" class="btn btn-default">搜索</button>
                        </form>
                    </div>
                    <div class="panel-body">
                        <table id="table-enterprise-report" class="table table-maintain">
                            <thead>
                                <tr>
                                    <th rowspan="2">企业所在地</th>
                                    <th rowspan="2">企业名称</th>
                                    <th rowspan="2">企业人数</th>
                                    <th rowspan="2">注册时间</th>
                                    <th colspan="3" class="time-header" role="time"></th>
                                </tr>
                                <tr>
                                    <th>登入数</th>
                                    <th>发起工单数</th>
                                    <th>处理工单人数</th>
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
    <td>{{companyNum}}</td>
    <td>{{registerDateText}}</td>
    <td>{{loginNum}}</td>
    <td>{{startNum}}</td>
    <td>{{dealNum}}</td>
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
    seajs.use('page/enterprise/report', function(page){
        page.run();
    });
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>
