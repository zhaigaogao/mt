define(function (require, exports, module) {
    var $ = require('jquery'),
        _ = require('underscore'),
        backbone = require('backbone');

    require('jquery-util');
    var confirm = require('component/Confirm');
    var alert = require('component/Alert');
    var template = require('template');

    require('bootstrap-datetimepicker');
    require('bootstrap-datetimepicker-zh-CN');

    var urls = {
        list: CONTEXT_PATH + '/userCount/getUserCount.do',
        export: CONTEXT_PATH + '/userCount/exportUserCount.do'
    };

    var globalShowCount = 3;
    var getPageList = function (pageNo, pageCount, showCount) {
        var pages = [];
        if (pageCount <= showCount) {
            var i = 1;
            while (i <= pageCount) { pages.push(i++); }
        }
        else {
            var radius = Math.floor(showCount / 2),
                upLimit = pageNo + radius,
                downLimit = pageNo - radius;
            while (upLimit > pageCount) { upLimit--; downLimit--; }
            while (downLimit < 1) { upLimit++; downLimit++; }
            if (upLimit >= pageCount) { upLimit = pageCount - 1; }
            if (downLimit <= 1) { downLimit = 2; }

            pages.push(1);
            while (downLimit <= upLimit) { pages.push(downLimit++); }
            pages.push(pageCount);
        }
        return pages;
    };
    var formatDate = function (paramDate, format) {
        if (!format) format = 'yyyy-MM-dd';
        var o = {
            'M+': paramDate.getMonth() + 1,                     //month
            'd+': paramDate.getDate(),                          //day
            'H+': paramDate.getHours(),                         //hour
            'm+': paramDate.getMinutes(),                       //minute
            's+': paramDate.getSeconds(),                       //second
            'q+': Math.floor((paramDate.getMonth() + 3) / 3),   //quarter
            'S': paramDate.getMilliseconds()                    //millisecond
        };
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (paramDate.getFullYear() + '').substr(4 - RegExp.$1.length));
        for (var k in o) {
            if (new RegExp('(' + k + ')').test(format)) {
                format = format.replace(RegExp.$1,
                    RegExp.$1.length == 1 ? o[k] :
                    ('00' + o[k]).substr(('' + o[k]).length));
            }
        }
        return format;
    };

    var QueryModel = backbone.Model.extend({
        defaults: {
            pageNum: 1,
            pageSize: 10,
            startDate: '',
            endDate: ''
        }
    });
    var SearchForm = backbone.View.extend({
        events: {
            'submit': 'doSearch'
        },
        initialize: function () {
            var timePickerSetting = {
                format: 'yyyy-mm-dd',
                language: 'zh-CN',
                minView: 2
            };
            var $startTimePicker = this.$('[role="startDate"]');
            var $endTimePicker = this.$('[role="endDate"]');
            $startTimePicker.datetimepicker(timePickerSetting);
            $endTimePicker.datetimepicker(timePickerSetting);
        },
        doSearch: function (e) {
            e.preventDefault();

            var queryData = this.$el.serializeObject();
            queryData.pageNum = 1;
            this.model.set(queryData);
        }
    });

    var PageModel = backbone.Model.extend({
        defaults: {
            pageNum: 1,
            pageSize: 10,
            total: 0,
            pages: 1
        }
    });
    var pageRender = template($('#tmpl-pagination').html());
    var PageView = backbone.View.extend({
        template: pageRender,
        events: {
            'click [data-do="pagechange"]': 'pageChange',
            'click [data-do="first"]': 'pageFirst',
            'click [data-do="prev"]': 'pagePrev',
            'click [data-do="next"]': 'pageNext',
            'click [data-do="last"]': 'pageLast',
        },
        initialize: function (options) {
            this.query = options.query;
            this.listenTo(this.model, 'response', this.render);
            this.render();
        },
        pageChange: function (e) {
            var currentPage = this.model.get('pageNum');
            var newPage = parseInt($(e.target).text());
            if (newPage !== currentPage) {
                this.gotoPage(newPage);
            }
        },
        pageFirst: function (e) {
            var currentPage = this.model.get('pageNum');
            if (currentPage > 1) {
                this.gotoPage(1);
            }
        },
        pagePrev: function (e) {
            var currentPage = this.model.get('pageNum');
            if (currentPage > 1) {
                this.gotoPage(currentPage - 1);
            }
        },
        pageLast: function (e) {
            var currentPage = this.model.get('pageNum'),
                pages = this.model.get('pages');
            if (currentPage < pages) {
                this.gotoPage(pages);
            }
        },
        pageNext: function (e) {
            var currentPage = this.model.get('pageNum'),
                pages = this.model.get('pages');
            if (currentPage < pages) {
                this.gotoPage(currentPage + 1);
            }
        },
        gotoPage: function (pageNum) {
            this.query.set('pageNum', pageNum);
        },
        render: function () {
            var json = this.model.toJSON();
            json.pagelist = getPageList(json.pageNum, json.pages, globalShowCount);
            this.$el.html(this.template(json));
        }
    });

    var enterpriseRender = template($('#tmpl-enterpriseItem').html());
    var Enterprise = backbone.Model.extend({
        pending: false
    });
    var EnterpriseCollection = backbone.Collection.extend({
        model: Enterprise,
        parse: function (resp) {
            return resp;
        },
        filter: function (query) {
            query.rnd = (new Date).getTime();
            this.fetch({
                reset: true,
                data: query
            });
        }
    });

    var EnterpriseRowView = backbone.View.extend({
        tagName: 'tr',
        template: enterpriseRender,
        events: {
            'click [data-do="delete"]': 'doDelete',
            'click [data-do="resetPassword"]': 'doResetPassword'
        },
        initialize: function () {
            this.listenTo(this.model, 'remove', this.remove);
            this.listenTo(this.model, 'change', this.render);
        },
        doDelete: function () {
            var model = this.model;
            confirm('确认删除该企业？', function () {
                model.destroy();
            });
        },
        doResetPassword: function () {
            var model = this.model;
            confirm('是否重置该用户密码？(初始密码:111111)', function () {
                model.resetPassword();
            });
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        }
    });
    var EnterpriseTableView = backbone.View.extend({
        initialize: function () {
            this.listenTo(this.collection, 'reset', this.reset);
            this.cacheEls();
        },
        addOne: function (model, collection, options) {
            var itemView = new EnterpriseRowView({
                model: model
            });
            this.$items.append(itemView.render().el);
        },
        reset: function (collection, options) {
            var previousModels = options.previousModels;
            _.each(previousModels, function (model) {
                model.trigger('remove');
            });

            this.$items.empty();
            if (collection.length === 0) {
                this.$items.html('<tr><td colspan="7">暂无数据</td></tr>');
            } else {
                collection.each(function (model, index) {
                    model.set({
                        $index: index,
                        registerDateText: formatDate(new Date(model.get('registerDate')), 'yyyy-MM-dd HH:mm')
                    });
                    this.addOne(model, collection);
                }, this);
            }
        },
        cacheEls: function () {
            this.$time = this.$('[role="time"]');
            this.$items = this.$('[role="items"]');
        }
    });

    var serializeJson = function (json) {
        var paramString = [];
        _.each(json, function (val, key) {
            paramString.push(key + '=' + val);
        });
        return paramString.join('&');
    };
    var run = function () {
        var searchQuery = new QueryModel();
        var pageModel = new PageModel();
        var enterpriseList = new EnterpriseCollection();

        var searchForm = new SearchForm({
            model: searchQuery,
            el: '.search-form'
        });
        var enterpriseTable = new EnterpriseTableView({
            el: '#table-enterprise-report',
            collection: enterpriseList
        });
        var pageView = new PageView({
            el: '.panel-footer',
            model: pageModel,
            query: searchQuery
        });

        enterpriseList.url = urls.list;
        enterpriseList.parse = function (resp) {
            var data = [];
            if (_.isObject(resp) && resp.success) {
                data = resp.model.list;
                pageModel.set({
                    pageNum: resp.model.pageNum,
                    pageSize: resp.model.pageSize,
                    pages: resp.model.pages < 1 ? 1 : resp.model.pages,
                    total: resp.model.total
                });
                pageModel.trigger('response');
            }

            return data;
        };

        searchQuery.on('change', function (model, option) {
            var query = model.toJSON();
            enterpriseList.filter(query);

            var timeText = (query.startDate == '' && query.endDate == '') ? '' : query.startDate + '至' + query.endDate;
            enterpriseTable.$time.html(timeText);
        });
        searchQuery.trigger('change', searchQuery);

        $('#btn-export').on('click', function () {
            location.href = urls.export + '?' + serializeJson(searchQuery.toJSON());
        });
    };

    module.exports = {
        run: run
    };
});
