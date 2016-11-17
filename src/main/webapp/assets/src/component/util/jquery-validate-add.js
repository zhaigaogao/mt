define(function (require) {
    require('jquery');
    require('jquery-validate');

    jQuery.validator.addMethod('hanzi', function (value, element) {
        var patt = /^[\u4E00-\u9FA5]+$/;
        return this.optional(element) || patt.test(value);
    }, '必须是汉字字符');
    jQuery.validator.addMethod('fullname', function (value, element) {
        var patt = /^[a-zA-Z\u4E00-\u9FA5]+$/;
        return this.optional(element) || patt.test(value);
    }, '必须是汉字字符和英文字符');
    jQuery.validator.addMethod('mobile', function (value, element) {
        var patt = /^[1][3,4,5,7,8][0-9]{9}$/;
        return this.optional(element) || patt.test(value);
    }, '手机号码格式不正确');
    jQuery.validator.addMethod('contactName', function (value, element) {
        var patt = /\d{11}|^\d{7,8}$|^\d{7,8}-\d{3,4}$|^\d{3,4}-\d{7,8}-\d{3,4}$/;
        return this.optional(element) || patt.test(value);
    }, '联系号码格式不正确');
    jQuery.validator.addMethod('illegalCharacter', function (value, element) {
        var patt = /[\[\]`~!_@#$%^&*+={}':;',.<>\/?！￥……&*——【】‘；：”“’。，、？]/;
        return this.optional(element) || !patt.test(value);
    }, '不能含有非法字符');
    jQuery.validator.addMethod('password', function (value, element) {
        var patt = /^[a-zA-Z\d-@_]+$/;
        //console.log(this.optional(element), patt.test(value));
        return this.optional(element) || patt.test(value);
    }, '只能包含英文字母、数字以及“@”、“_”、“-”三种符号');
    jQuery.validator.addMethod('numletter', function (value, element) {
        var patt = /^[a-zA-Z\d]+$/;
        return this.optional(element) || patt.test(value);
    }, '只能包含英文字母和数字');

    jQuery.extend(jQuery.validator.messages, {
        required: '必须填写',
        remote: '请修正此字段',
        email: '电子邮件地址不正确',
        url: '请输入有效的网址',
        date: '请输入有效的日期',
        dateISO: '请输入有效的日期 (YYYY-MM-DD)',
        number: '请输入有效的数字',
        digits: '只能输入数字',
        creditcard: '请输入有效的信用卡号码',
        equalTo: '你的输入不相同',
        extension: '请输入有效的后缀',
        maxlength: $.validator.format('最多只能输入 {0} 个字符'),
        minlength: $.validator.format('最少需要输入 {0} 个字符'),
        rangelength: $.validator.format('长度必须在 {0} 到 {1} 个字符之间'),
        range: $.validator.format('请输入范围在 {0} 到 {1} 之间的数值'),
        max: $.validator.format('请输入不大于 {0} 的数值'),
        min: $.validator.format('请输入不小于 {0} 的数值')
    });
});