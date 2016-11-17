define(function (require, exports, module) {
    var $ = require('jquery'),
        _ = require('underscore'),
        backbone = require('backbone');
    
    var confirm = require('component/Confirm');
    var alert = require('component/Alert');
    var template = require('template');
    require('jquery-util');
    require('jquery-validate');
    require('jquery-validate-add');
    jQuery.validator.addMethod('deptcheck', function (value, element) {
        return this.optional(element) || value !== '-1';
    }, '必须选择地区');

    var urls = {
        getAreas: CONTEXT_PATH + '/dept/findByParentId.do',
        getArea: CONTEXT_PATH + '/dept/getById.do',
        getData: CONTEXT_PATH + '/mtuser/getById.do',
        add: CONTEXT_PATH + '/mtuser/addUser.do',
        edit: CONTEXT_PATH + '/mtuser/editUser.do',
        manager: CONTEXT_PATH + '/mtuser/manager.do'
    };
    var defaultRootId = '';
    var areaTemplate = template($('#tmpl-areas').html());
    var validateOption = {
            errorClass: 'error-valid',
            rules: {
                'province': {
                    required: true,
                    deptcheck: true
                },
                'loginName': {
                    required: true,
                    numletter: true,
                    rangelength: [4, 20]
                },
                'password': {
                    required: true,
                    password: true,
                    rangelength: [6, 16]
                },
                'plainPassword': {
                    required: true,
                    password: true,
                    rangelength: [6, 16],
                    equalTo: '[name="password"]'
                },
                'userName': {
                    required: true,
                    maxlength: 10
                },
                'email': {
                    email: true
                },
                'mobile': {
                    required: true,
                    mobile: true
                }
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

    var UserFormView = backbone.View.extend({
        pending: false,
        events: {
            'submit': 'doSubmit',
            'change [role="province-place"]': 'areaChange',
            'change [role="city-place"]': 'areaChange'
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

            $.ajax({
                context: this,
                url: submitData.id.length > 0 ? urls.edit : urls.add,
                dataType: 'json',
                type: 'post',
                data: submitData,
                beforeSend: function () {
                    this.pending = true;
                },
                success: function (res) {
                    if (res.success) {
                        alert('保存成功！', function () {
                            location.href = urls.manager;
                        }).delay(3);
                    }
                    else {
                        alert(res.message || '发生错误，操作失败！');
                    }
                },
                error: function () {
                    alert('发生错误，操作失败！');
                },
                completa: function () {
                    this.pending = false;
                }
            });
        },
        initForm: function () {
            if (this.$id.val() !== '') {
                var _this = this;
                this.$('[name="password"]').parents('.form-group').remove();
                this.$('[name="plainPassword"]').parents('.form-group').remove();
                this.$('[name="loginName"]').prop('disabled', true);

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
        var userForm = new UserFormView({
            el: '#form',
            default_deptId: default_deptId
        });
        userForm.$el.css('visibility', 'visible');
    };

    module.exports = {
        run: run
    };
});