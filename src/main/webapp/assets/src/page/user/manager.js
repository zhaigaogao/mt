define(function (require, exports, module) {
    var $ = require('jquery'),
        _ = require('underscore'),
        backbone = require('backbone');
    
    require('jquery-util');
    var confirm = require('component/Confirm');
    var alert = require('component/Alert');
    var template = require('template');

    var urls = {
        list: CONTEXT_PATH + '/mtuser/findByCondition.do',
        edit: CONTEXT_PATH + '/mtuser/editUser.do'
        //dedlete: 
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
            userName: ''
        }
    });
    var SearchForm = backbone.View.extend({
        events: {
            'submit': 'doSearch'
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

    var userRender = template($('#tmpl-userItem').html());
    var UserModel = backbone.Model.extend({
        pending: false,
        destroy: function () {
            var _this = this;
            if (this.pending) return false;

            var data = _.pick(this.toJSON(), 'id', 'status');
            data.status = 9;

            $.ajax({
                context: this,
                url: urls.edit,
                type: 'post',
                dataType: 'json',
                data: data,
                beforeSend: function () {
                    this.pending = true;
                },
                success: function (res) {
                    _.defaults(res, {
                        success: false,
                        message: '未知错误'
                    });
                    if (res.success === true) {
                        this.stopListening();
                        alert('删除成功!', function () {
                            _this.trigger('destroy', _this, _this.collection);
                        }).delay(3);
                    } else {
                        alert(res.message).delay(3);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('系统或网络错误，请稍后再试！').delay(3);
                    //alert(textStatus + '：' + errorThrown).delay(3);
                },
                complete: function () {
                    this.pending = false;
                }
            });
        },
        resetPassword: function () {
            if (this.pending) return false;

            var data = _.pick(this.toJSON(), 'id', 'password');
            data.password = '111111';

            $.ajax({
                context: this,
                url: urls.edit,
                type: 'post',
                dataType: 'json',
                data: data,
                beforeSend: function () {
                    this.pending = true;
                },
                success: function (res) {
                    _.defaults(res, {
                        success: false,
                        message: '未知错误'
                    });
                    if (res.success === true) {
                        alert('操作成功').delay(3);
                    } else {
                        alert(res.message).delay(3);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('系统或网络错误，请稍后再试！').delay(3);
                    //alert(textStatus + '：' + errorThrown).delay(3);
                },
                complete: function () {
                    this.pending = false;
                }
            });
        },
        setStatus: function (status) {
            if (this.pending) return false;

            var data = _.pick(this.toJSON(), 'id', 'status');
            data.status = status;

            $.ajax({
                context: this,
                url: urls.edit,
                type: 'post',
                dataType: 'json',
                data: data,
                beforeSend: function () {
                    this.pending = true;
                },
                success: function (res) {
                    _.defaults(res, {
                        success: false,
                        message: '未知错误'
                    });
                    if (res.success === true) {
                        this.set('status', status);
                    } else {
                        alert(res.message).delay(3);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('系统或网络错误，请稍后再试！').delay(3);
                    //alert(textStatus + '：' + errorThrown).delay(3);
                },
                complete: function () {
                    this.pending = false;
                }
            });
        }
    });
    var UserCollection = backbone.Collection.extend({
        model: UserModel,
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

    var UserRowView = backbone.View.extend({
        tagName: 'tr',
        template: userRender,
        events: {
            'click [data-do="delete"]': 'doDelete',
            'click [data-do="resetPassword"]': 'doResetPassword',
            'click [data-do="changeStatus"]': 'changeStatus',
        },
        initialize: function () {
            this.listenTo(this.model, 'remove', this.remove);
            this.listenTo(this.model, 'change', this.render);
        },
        doDelete: function () {
            var model = this.model;
            confirm('确认删除该用户？', function () {
                model.destroy();
            });
        },
        doResetPassword: function () {
            var model = this.model;
            confirm('是否重置该用户密码？(初始密码:111111)', function () {
                model.resetPassword();
            });
        },
        changeStatus: function (e) {
            var status = $(e.target).data('status');
            this.model.setStatus(status);
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        }
    });
    var UserTableView = backbone.View.extend({
        initialize: function () {
            this.listenTo(this.collection, 'reset', this.reset);
            this.cacheEls();
        },
        addOne: function (model, collection, options) {
            var itemView = new UserRowView({
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
                this.$items.html('<tr><td colspan="6">暂无数据</td></tr>');
            } else {
                collection.each(function (model, index) {
                    model.set({
                        $index: index,
                        createDateText: formatDate(new Date(model.get('createDate')), 'yyyy-MM-dd HH:mm')
                    });
                    this.addOne(model, collection);
                }, this);
            }
        },
        cacheEls: function () {
            this.$items = this.$('[role="items"]');
        }
    });

    var run = function () {
        var searchQuery = new QueryModel();
        var pageModel = new PageModel();
        var userList = new UserCollection();

        var searchForm = new SearchForm({
            model: searchQuery,
            el: '.search-form'
        });
        var userTable = new UserTableView({
            el: '#table-user',
            collection: userList
        });
        var pageView = new PageView({
            el: '.panel-footer',
            model: pageModel,
            query: searchQuery
        });

        userList.url = urls.list;
        userList.parse = function (resp) {
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
            userList.filter(model.toJSON());
        });
        userList.filter(searchQuery.toJSON());
        userList.on('destroy', function () {
            if (userList.length === 0) {
                pageView.pagePrev();
            }
            else {
                pageModel.set('total', pageModel.get('total') - 1);
                pageModel.trigger('response');
            }
        });
    };

    module.exports = {
        run: run
    };
});