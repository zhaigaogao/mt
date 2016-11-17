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
                <c:set var="menuActive" value=""></c:set>
                <%@ include file="/WEB-INF/views/common/menu.jsp"%>
            </div>
            <div class="col-maintain col-sm-9 col-md-10 col-lg-10">
                <div class="panel panel-maintain  panel-maintain-content">
                    <div class="panel-heading">修改密码</div>
                    <div class="panel-body">
                        <form id="form-password" class="form-maintain">
                            <div class="form-group">
                                <label for="prePwd" class="control-label">原密码</label>
                                <input type="password" class="form-control" name="prePwd" placeholder="请输入原密码">
                            </div>
                            <div class="form-group">
                                <label for="newPwd" class="control-label">新密码</label>
                                <input type="password" class="form-control" name="newPwd" placeholder="只能包含英文字母、数字以及“@”、“_”、“-”三种符号，6-16个字符">
                            </div>
                            <div class="form-group">
                                <label for="surePwd" class="control-label">确认密码</label>
                                <input type="password" class="form-control" name="surePwd" placeholder="只能包含英文字母、数字以及“@”、“_”、“-”三种符号，6-16个字符">
                            </div>
                            <div class="form-group button-group">
                                <button type="submit" class="btn btn-submit">保存</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    seajs.use('page/password', function (page) {
        page.run();
    });
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
</body>