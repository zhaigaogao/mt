define(function (require, exports, module) {
    var $ = require('jquery');
    var alert = require('component/Alert');

    var urls = {
        index: CONTEXT_PATH + '/group/manager.do',
        login: CONTEXT_PATH + '/user/login.do'
    };

    var run = function () {
        var $form = $('#form-login');
        var pending = false;

        $form.on('submit', function (e) {
            e.preventDefault();

            if (pending) { return false; }
            var data = $form.serializeArray();

            $.ajax({
                url: urls.login,
                type: 'post',
                dataType: 'json',
                data: data,
                beforeSend: function () {
                    pending = true;
                },
                success: function (res) {
                    if (res.success) {
                        location.href = urls.index;
                    }
                    else {
                        alert(res.message || '发生错误!');
                    }
                },
                error: function () {
                    alert('发生错误!');
                },
                complete: function () {
                    pending = false;
                }
            });

            return false;
        });
        $form.show();
    };

    module.exports = {
        run: run
    };
});