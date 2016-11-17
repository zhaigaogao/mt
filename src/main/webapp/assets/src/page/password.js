define(function (require, exports, module) {
    var $ = require('jquery');
    var alert = require('component/Alert');
    require('jquery-validate');
    require('jquery-validate-add');

    var urls = {
        submit: CONTEXT_PATH + '/user/modifyPassword.do'
    };

    var run = function () {
        var $form = $('#form-password');
        var pending = false;

        $form.validate({
            errorClass: 'error-valid',
            rules: {
                'prePwd': {
                    required: true
                },
                'newPwd': {
                    required: true,
                    password: true,
                    rangelength: [6, 16]
                },
                'surePwd': {
                    required: true,
                    password: true,
                    rangelength: [6, 16],
                    equalTo: '[name=newPwd]'
                }
            },
            messages: {
                'prePwd': {
                    required: '请输入原密码'
                },
                'newPwd': {
                    required: '请输入新密码'
                },
	            'surePwd': {
                    required: '请输入确认密码',
                    equalTo: '两次密码输入不同，请返回检查'
                }
            }
        });

        $form.on('submit', function (e) {
            e.preventDefault();
            if (pending) { return false; }

            if (!$form.valid()) { return; }
            var data = $form.serializeArray();

            $.ajax({
                url: urls.submit,
                type: 'post',
                dataType: 'json',
                data: data,
                beforeSend: function () {
                    pending = true;
                },
                success: function (res) {
                    if (res.success) {
                        alert('保存成功');
                        $form[0].reset();
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
        });
        $form.show();
    };

    module.exports = {
        run: run
    };
});