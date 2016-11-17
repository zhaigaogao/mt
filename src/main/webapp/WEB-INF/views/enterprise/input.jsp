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
                    <div class="panel-heading">企业信息</div>
                    <div class="panel-body">
                        <form id="form" class="form-maintain" style="visibility:hidden;">
                            <input type="hidden" name="id" value="${group.id}">
                            <input type="hidden" name="deptId" value="${group.deptId}">
                            <div class="form-group clearfix" id='company-toggle'>
                                <label for="ecode" class="control-label">集团编号</label>
                                <input type="text" class="form-control" data-do="search" name="ecode" value="${group.ecode}" placeholder="请输入集团编号">
                                <button type="button" class="form-control btn btn-info btn-search">查询集团</button>
                                <div class="pull-right">
                                    <a href="javascript:" data-do="searchId">查询集团编号</a>
                                </div>
                            </div>
                            <div class="form-group">
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
                            <!--  
                            <div class="form-group required">
                                <label for="partyId" class="control-label">企业账号</label>
                                <input type="text" class="form-control" name="partyId" value="${group.partyId}" placeholder="只能使用字母和数字，4-20个字符">
                            </div>-->
                            <div class="form-group required">
                                <label for="customerName" class="control-label">企业名称</label>
                                <input type="text" class="form-control" name="customerName" value="${group.customerName}" placeholder="最多20个字符">
                            </div>
                            <div class="form-group required">
                                <label for="contactAddress" class="control-label">企业地址</label>
                                <input type="text" class="form-control" name="contactAddress" value="${group.contactAddress}" placeholder="最多100个字符">
                            </div>
                            <div class="form-group required">
                                <label for="contactUserName" class="control-label">企业管理员账号(手机号)</label>
                                <input type="text" class="form-control" name="contactUserName" value="${group.contactUserName}" placeholder="请输入手机号">
                            </div>
                            <div class="form-group required">
                                <label for="contactPassword" class="control-label">企业管理员密码</label>
                                <input type="password" class="form-control" name="contactPassword" value="" placeholder="只能包含英文字母、数字以及“@”、“_”、“-”三种符号，6-16个字符">
                            </div>
                            <div class="form-group required">
                                <label for="plainPassword" class="control-label">确认密码</label>
                                <input type="password" class="form-control" name="plainPassword" value="" placeholder="只能包含英文字母、数字以及“@”、“_”、“-”三种符号，6-16个字符">
                            </div>
                            <div class="form-group required">
                                <label for="contactName" class="control-label">企业管理员姓名</label>
                                <input type="text" class="form-control" name="contactName" value="${group.contactName}" placeholder="最多10个字符">
                            </div>

                            <div class="form-group">
                                <label for="contactEmail" class="control-label">企业管理员邮箱</label>
                                <input type="text" class="form-control" name="contactEmail" value="${group.contactEmail}" placeholder="请输入邮箱">
                            </div>
                            <div class="form-group">
                                <label for="contactMobile" class="control-label">企业管理员手机</label>
                                <input type="text" class="form-control" name="contactMobile" value="${group.contactMobile}" placeholder="请输入手机号">
                            </div>
                            <div class="form-group">
                                <label for="customermanagername" class="control-label">客户经理姓名</label>
                                <input type="text" class="form-control" name="customermanagername" value="${group.customermanagername}" placeholder="最多10个字符">
                            </div>
                            <div class="form-group">
                                <label for="customermanagermobile" class="control-label">客户经理手机</label>
                                <input type="text" class="form-control" name="customermanagermobile" value="${group.customermanagermobile}" placeholder="请输入手机号">
                            </div>
                            <div class="form-group">
                                <label for="businesshall" class="control-label">营业部</label>
                                <input type="text" class="form-control" name="businesshall" value="${group.businesshall}" placeholder="最多50个字符">
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

<script type="text/html" id="tmpl-searchId">
    <div class="modal-dialog" style="width:700px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></span>
                </button>
                <h4>查询企业编号</h4>
            </div>
            <div class="modal-body">
                <form role="search-id" class="form-maintain" style="paddingLeft:50px">
                    <div class="form-group required">
                        <label for="customerName1" class="control-label">企业名称</label>
                        <input type="text" class="form-control" name="customerName1" value="${group.customerName}" placeholder="最多20个字符">
                    </div>
                    <div class="form-group required">
                        <label for="customermanagermobile1" class="control-label">客户经理手机</label>
                        <input type="text" class="form-control" name="customermanagermobile1" value="${group.customermanagermobile}" placeholder="请输入手机号">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" data-do="save">保存</button>
            </div>
        </div>
    </div>
</script>

<script>
    seajs.use('page/enterprise/input', function(page){ 
        var deptId = '${sessionScope.user.deptId}';
        page.run(deptId); 
    });
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>