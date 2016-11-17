<%@ page language="java" pageEncoding="utf-8"%>
<%--
配置项
1、系统配置，由后台输出至页面
2、seajs 配置
--%>
<script id="config">
var COMPANY_ID = '<c:out value="${sessionScope.companyId}"/>';
var COMPANY_NAME = '<c:out value="${sessionScope.companyName}"/>';
var USER_ID = '<c:out value="${sessionScope.userId}"/>';
var MOBILEPHONE = '<c:out value="${sessionScope.mobile}"/>';
var SECRET_KEY = 0;

var CONTEXT_PATH = '<c:out value="${contextPath}"/>';
var SEA_CONFIG = {
    debug: true,
    base: CONTEXT_PATH + '/assets',
    map: [
    	[ /^(.*\.(?:css|js))(.*)$/i, '$1?' + 'v=100015' ]
    ],
    paths: {
        'page': 'src/page',
        'component': 'src/component',
        'util': 'src/component/util'
    },
    alias: {
        'jquery': 'dep/jquery/1.11.2/jquery.js',
        'jqueryui': 'dep/jquery-ui/jquery-ui.min.js',
        'bootstrap': 'dep/bootstrap/3.3.6/js/bootstrap-cmd',
        'underscore': 'dep/underscore/1.8.3/cmd/underscore-min',
        'backbone': 'dep/backbone/1.3.3/backbone',
        'zepto': 'dep/zepto/zepto.min.js',

        'video': 'dep/video/5.8.0/video.min',
        'unslider': 'dep/unslider/2.0/dist/js/unslider-min',
        'underscore': 'dep/underscore/1.8.3/underscore',

        'plupload': 'dep/plupload/2.1.3/js/plupload.full.min.js',
        'jquery-slimScroll': 'dep/jquery-slimscroll/1.3.7/jquery.slimscroll',
        'jquery-placeholder': 'dep/jquery-placeholder/2.1.2/jquery.placeholder.min',

        'jquery-validate': 'dep/jquery-validation/1.15.0/jquery.validate.min',
        'calendar': 'dep/calendar/calendar',
        'bootstrap-datetimepicker': 'dep/bootstrap-datetimepicker/js/bootstrap-datetimepicker',
        'bootstrap-datetimepicker-zh-CN': 'dep/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN',

        'ztree': 'dep/zTree/js/jquery.ztree.core',
        'ztree.exedit': 'dep/zTree/js/jquery.ztree.exedit',

        'md5': 'util/md5',
        'cookie': 'util/cookie',
        'parseQueryString': 'util/parseQueryString',
        'template': 'util/template',
        'walkList': 'util/walkList',
        'jquery-util': 'util/jquery-util',
        'jquery-validate-add': 'util/jquery-validate-add',
        'csrf': 'util/csrf'
    }
};
</script>
