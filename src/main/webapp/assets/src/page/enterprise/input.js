define(function (require, exports, module) {
    var $ = require('jquery'),
        _ = require('underscore'),
        backbone = require('backbone');
    
    var confirm = require('component/Confirm');
    var alert = require('component/Alert');
    var template = require('template');
    var loader = require('component/loader');
    var util = require('component/util');
    require('jquery-util');
    require('jquery-validate');
    require('jquery-validate-add');

    jQuery.validator.addMethod('deptcheck', function (value, element) {
        return this.optional(element) || value !== '-1';
    }, '必须选择地区');

    var urls = {
        getAreas: CONTEXT_PATH + '/dept/findByParentId.do',
        getArea: CONTEXT_PATH + '/dept/getById.do',
        getData: CONTEXT_PATH + '/group/getById.do',
        add: CONTEXT_PATH + '/group/addGroup.do',
        edit: CONTEXT_PATH + '/group/editGroup.do',
        manager: CONTEXT_PATH + '/group/manager.do',
    };
    var defaultRootId = '';  //取省
    var areaTemplate = template($('#tmpl-areas').html());
    var searchId = template($('#tmpl-searchId').html());
    var validateOption = {
            errorClass: 'error-valid',
            rules: {
                'partyId': {
                    required: true,
                    numletter: true,
                    rangelength: [4, 20]
                },
                'customerName': {
                    required: true,
                    maxlength: 20
                },
                'contactAddress': {
                    required: true,
                    maxlength: 100
                },
                'contactUserName': {
                    required: true,
                    mobile: true
                },
                'contactPassword': {
                    required: true,
                    password: true,
                    rangelength: [6, 16]
                },
                'plainPassword': {
                    required: true,
                    password: true,
                    rangelength: [6, 16],
                    equalTo: '[name="contactPassword"]'
                },
                'contactName': {
                    required: true,
                    maxlength: 10
                },

                'contactEmail': {
                    email: true
                },
                'contactMobile': {
                    mobile: true
                },
                'customermanagername': {
                    maxlength: 10
                },
                'customermanagermobile': {
                    mobile: true
                },
                'businesshall': {
                    maxlength: 50
                },
            },
            messages: {
	            'plainPassword': {
                    equalTo: '两次密码输入不同，请返回检查'
                }
            }
        };

    var getAreas = function (parentid, callback) {
        if (parentid === '-1') { callback([]); return; }
        $.ajax({
            url: urls.getAreas,
            dataType: 'json',
            type: 'get',
            data: { parentId: parentid },
            success: function (res) {
                if (res.success) {
                    callback(res.model);
                }
                else {
                    alert(res.message || '获取地区时出错!').delay(3);
                }
            },
            error: function () {
                alert('获取地区时出错!').delay(3);
            }
        });
    };
    var getArea = function (areaid, callback) {
        $.ajax({
            url: urls.getArea,
            dataType: 'json',
            type: 'get',
            data: { id: areaid },
            success: function (res) {
                if (res.success) {
                    callback(res.model);
                }
                else {
                    alert(res.message || '获取地区时出错!').delay(3);
                }
            },
            error: function () {
                alert('获取地区时出错!').delay(3);
            }
        });
    };

    var SearchId = Backbone.View.extend({
        template : searchId,
        className : 'modal',
        events : {
            'click [data-do="save"]' : 'doSave'
        },
        initialize : function() {
            //this.$el.on('hidden.bs.modal', _.bind(this.hide, this));
            this.$el.appendTo(document.body);
        },
        render : function(data) {
            var markup = this.template(data);
            this.$el.html(markup);

            this.$('form[role=search-id]').validate({
                errorClass: 'error-valid',
                rules: {
                    'customerName1': {
                        required: true,
                    },
                    'customermanagermobile1': {
                        required: true,
                        mobile: true
                    }
                },
                messages: {
                    'customerName1': {
                        required: '企业名为必填项',
                    },
                    'customermanagermobile1': {
                        required: '手机为必填项',
                        mobile: '手机号码不正确'
                    },
                }
            });
            return this;
        },
        doSave : function() {
            var _this = this;
            var corpname1 = this.$("[name='customerName1']").val();
            var telnum1 = this.$("[name='customermanagermobile1']").val();
            if (!this.$('form[role=search-id]').valid()) {
                return;
            }

            var modelJSON = this.$('form[role=search-id]')
                    .serializeObject();
            modelJSON.selfCompanyId = COMPANY_ID;
            modelJSON.companyId = COMPANY_ID;
            modelJSON.isManager = 0;
            modelJSON.secretKey = SECRET_KEY;

            $.ajax({
                url : CONTEXT_PATH + '/group/getCorp.do?corpname=' + corpname1 + '&telnum=' + telnum1,
                type : 'post',
                dataType : 'json',
                data : modelJSON,
                success : function(res) {
                    res =_.extend({
                        success: false,
                        message: '操作失败'
                    }, res);
                    if (res.success) {
                        alert('查询成功!', function(){
                            _this.hide()
                        }).delay(3);
                        $("[name='ecode']").val(res.model.corpid).attr("readonly",true);
                    } else {
                        alert(res.message || '未知错误').delay(3);
                    }
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    alert(textStatus + '：' + errorThrown).delay(3);
                }
            });
        },
        show: function (data) {
            // this.isEdit = data.edit;
            this.render(data).$el.modal('show');
        },
        hide: function () {
            this.$el.modal('hide');
        }

    })
    var searchCompanyId = new SearchId();
    var EnterpriseFormView = backbone.View.extend({
        pending: false,
        events: {
            'submit': 'doSubmit',
            'change [role="province-place"]': 'areaChange',
            'change [role="city-place"]': 'areaChange',
            'click .btn-search': 'doSearch'
        },
        initialize: function (options) {
            this.default_deptId = options.default_deptId;
            this.$el.validate(validateOption);
            this.cacheEls();
            this.initCity();
        },
        initCity: function () {
            var _this = this;
            var $province = this.$province;
            getAreas(defaultRootId, function (areas) {
                $province.html(areaTemplate({ areas: areas, level: 1 }));
                _this.initForm();
                _this.setDisabledArea(areas, 1);
            });
        },
        areaChange: function (e, toload) {
            var _this = this;
            var $el = $(e.target).attr('role') === 'province-place' ? this.$province : 
                ($(e.target).attr('role') === 'city-place' ? this.$city : this.$area);
            
            var parentid = $el.val();
            var $childEl = $el.attr('role') === 'province-place' ? this.$city : this.$area;
            var level = $el.attr('role') === 'province-place' ? 2 : 3;
            getAreas(parentid, function (areas) {
                $childEl.html(areaTemplate({ areas: areas, level: level }))
                    .trigger('change');
                if (toload) {
                    $childEl.triggerHandler('loaded');
                }
                _this.setDisabledArea(areas, level);
            });
        },
        doSubmit: function (e) {
            e.preventDefault();
            if (this.pending) { return; }
            if (!this.$el.valid()) {
                return;
            }

            var disabledEls = this.$('[disabled]');
            disabledEls.prop('disabled', false);
            var submitData = this.$el.serializeObject();
            disabledEls.prop('disabled', true);

            submitData.deptId = submitData.area === '-1' ? 
                (submitData.city === '-1' ? submitData.province : submitData.city) : submitData.area;
            submitData.corpId = this.$("input[name='ecode']").val();
            submitData.submitType = $("[name='contactName']").attr('readonly') ? 'vwt' : '';

            $.ajax({
                context: this,
                url: submitData.id.length > 0 ? urls.edit : urls.add,
                dataType: 'json',
                type: 'post',
                data: submitData,
                beforeSend: function () {
                	$("#btn-submit").prop('disabled', true);
                    this.pending = true;
                    loader.show()
                },
                success: function (res) {
                    if (res.success) {
                        alert('保存成功！', function () {
                            location.href = urls.manager;
                        });
                    }
                    else {
                        alert(res.message || '发生错误，操作失败！');
                    }
                },
                error: function () {
                    alert('发生错误，操作失败！');
                },
                complete: function() {
                	$("#btn-submit").prop('disabled', false);
                    this.pending = false;
                    loader.hide()
                }
            });
        },
        doSearch: function(e){
            e.preventDefault();
            var id = this.$("[name='ecode']").val();
            $.ajax({
                url: CONTEXT_PATH + '/vwt/getCorpById.do?id='+id,
                data: {},
                dataType: 'json',
                type: 'post',
                success: function(resp) {
                    resp =_.extend({
                        success: false,
                        message: '操作失败'
                    }, resp);
                    if(resp.success) {
                        $("[name='ecode']").attr("readonly",true);
                        $("[name='customerName']").val(resp.model.corpname)
                                .attr("readonly",true);
                        $("[name='contactUserName']").val(resp.model.corpmobilephone);
                        $("[name='contactName']").val(resp.model.corppersonname)
                                .attr("readonly",true);
                        $("[name='contactMobile']").val(resp.model.corpmobilephone)
                                .attr("readonly",true);
                        $("[name='contactMobile']").val(resp.model.corpmobilephone)
                                .attr("readonly",true);
                        $("[name='customermanagername']").val(resp.model.clientManagerName )
                                .attr("readonly",true);
                        $("[name='customermanagermobile']").val(resp.model.clientManagerphone )
                                .attr("readonly",true)
                    } else {
                        alert(resp.message);
                    }
                },
                error: function(resp) {
                    alert(resp.message);
                }
            });
        },
        initForm: function () {
            if (this.$id.val() !== '') {
                var _this = this;
                this.$('[name="contactPassword"]').parents('.form-group').remove();
                this.$('[name="plainPassword"]').parents('.form-group').remove();
                this.$('[name="partyId"]').prop('disabled', true);
                this.$('[name="contactUserName"]').prop('disabled', true);

                var deptId = this.$deptId.val();
                this.setAreas(deptId);
            }
            else {
                this.setAreas(this.default_deptId);
            }
        },
        setAreas: function (deptId) {
            var _this = this;

            var provinces = this.$province.children().map(function (i,el) { return $(el).val(); }).toArray();
            if (provinces.indexOf(deptId) >= 0) {
                this.$province.val(deptId).trigger('change');
            }
            else {
                getArea(deptId, function (dept) {
                    var parentId = dept.parentId.toString();
                    if (provinces.indexOf(parentId) >= 0) { 
                        _this.$city.on('loaded', function () {
                            _this.$city.val(deptId).trigger('change', true);
                            _this.$city.off('loaded', '**');
                        });
                        _this.$province.val(parentId).trigger('change', true);
                    }
                    else {
                        _this.$city.on('loaded', function () {
                            _this.$city.val(parentId).trigger('change', true);
                            _this.$city.off('loaded', '**');
                        });
                        _this.$area.on('loaded', function () {
                            _this.$area.val(deptId);
                            _this.$area.off('loaded', '**');
                        });
                        getArea(parentId, function (dept_) {
                            _this.$province.val(dept_.parentId.toString()).trigger('change', true);
                        });
                    }
                });
            }
        },
        setDisabledArea: function (areas, level) {
            var defaultArea = _.find(areas, function (item) { return item.id.toString() === this.default_deptId; }, this);
            if (defaultArea) {
                if (level >= 3) {
                    this.$area.prop('disabled', true);
                }
                if (level >= 2) {
                    this.$city.prop('disabled', true);
                }
                if (level >= 1) {
                    this.$province.prop('disabled', true);
                }
            }
        },
        cacheEls: function () {
            this.$id = this.$('[name="id"]');
            this.$deptId = this.$('[name="deptId"]');

            this.$province = this.$('[role="province-place"]');
            this.$city = this.$('[role="city-place"]');
            this.$area = this.$('[role="area-place"]');
        }
    });

    var run = function (default_deptId) {
        var enterpriseForm = new EnterpriseFormView({
            el: '#form',
            default_deptId: default_deptId
        });
        enterpriseForm.$el.css('visibility', 'visible');
        if ($('[name="deptId"]').val()!=='') {
            $('.btn-search').hide();
        };
        $('[data-do="searchId"]').on('click',function(){
            searchCompanyId.show();
        });
        
    };

    module.exports = {
        run: run
    };
});