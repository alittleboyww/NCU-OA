var login= function () {
    //判断验证码是否经过了校验
    var captchaChecked = false;

    /**
     * 检查验证码
     */
    var checkCaptchaChecked = function(){
        var captchaText = $("input[name='captcha']").val();
        var username = $("input[name='username']").val();
        var password = $("input[name='password']").val();
        //当用户名和密码同时为空 验证码为空时不做处理
        if(captchaText.length < 5 && (username != "" || password != "")){
            $("#validate-dev")[0].className = "form-group";
            $("#validate-alert")[0].innerText = "验证码输入错误";
            $("#captcha").css({"border-color": "red"});
            return;
        }else if(captchaText.length < 5){
            return;
        }
        $.ajax({
            url: '/captcha/verifyCaptcha',
            type: 'POST',
            data: JSON.stringify({'captcha': captchaText}),
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                //验证成功
                if (result.code == "200"){
                    console.log(result.code + " 00000")
                    //保存验证通过的验证码
                    captchaChecked = true;
                    $('#validate-dev')[0].className = "form-group";
                    $('#validate-alert')[0].className = "span-success";
                    $('#validate-alert')[0].innerText = "验证成功";
                    $("#captcha").css({"border-color":"#ccc"})
                }
                //验证码失效  提示验证码失效信息
                else if (result.code == "300") {
                    $('#validate-dev')[0].className = "form-group";
                    $('#validate-alert')[0].className = "span-fail";
                    $('#validate-alert')[0].innerText = "验证码已经失效";
                    $("#captcha").css({"border-color":"#ccc"})
                }
                //验证码错误，需重新输入
                else{
                    //提示验证码输入错误
                    $("#validate-dev")[0].className = "form-group";
                    $('#validate-alert')[0].className = "span-fail";
                    $("#validate-alert")[0].innerText = "验证码输入错误";
                    $("#captcha").css({"border-color":"red"})
                }
            },
            error: function (error) {

            }
        })
    }

    /**
     * 刷新验证码
     */
    var refreshCaptcha = function() {
        //刷新了验证码  让检验标志置为false
        captchaChecked = false;
        //重新加载验证码
        $('#captchaImg').attr('src', "http://"+window.location.host + "/captcha?" + Math.random());
    }

    /**
     * 登录逻辑
     */
    var login = function () {
        var username = $("input[name='username']").val();
        var password = $("input[name='password']").val();
        var captchaText = $("input[name='captcha']").val();
        if (username == ""){
            $("#username-dev")[0].className = "form-group";
            $("#username-alert")[0].innerText = "请输入用户名";
            $("#username").css({"border-color":"red"})
        }
        if (password == ""){
            $("#password-dev")[0].className = "form-group";
            $("#password-alert")[0].innerText = "请输入密码";
            $("#password").css({"border-color":"red"})
        }
        if(captchaText.length < 5 || (captchaChecked && captchaText.length < 5)){
            captchaChecked = false;
            $("#validate-dev")[0].className = "form-group";
            $('#validate-alert')[0].className = "span-fail";
            $("#validate-alert")[0].innerText = "验证码输入错误";
            $("#captcha").css({"border-color":"red"})
        }
        //是否验证通过
        if(captchaChecked){
            $("#login").submit();
        }
    }


    /**
     * 验证码 用户名 密码聚焦事件
     */
    var inputFocus = function(id, value){
        if(id == "username"){
            $("#username-dev")[0].className = "form-group hidden";
            $("#username-alert")[0].innerText = "";
            $("#username").css({"border-color":"#ccc"})
        }else if(id == "password"){
            $("#password-dev")[0].className = "form-group hidden";
            $("#password-alert")[0].innerText = "";
            $("#password").css({"border-color":"#ccc"})
        }else if(id == "captcha"){
            $("#validate-dev")[0].className = "form-group hidden";
            $("#validate-alert")[0].innerText = "";
            $("#captcha").css({"border-color":"#ccc"})
        }
    }


    /**
     * 验证码 用户名 密码失焦事件
     */
    var inputBlur = function (id, value){
        if(id == "username"){
            if (value == ""){
                $("#username-dev")[0].className = "form-group";
                $("#username-alert")[0].innerText = "请输入用户名";
                $("#username").css({"border-color":"red"})
            }
        }else if(id == "password"){
            if (value == ""){
                $("#password-dev")[0].className = "form-group";
                $("#password-alert")[0].innerText = "请输入密码";
                $("#password").css({"border-color":"red"})
            }
        }else if(id == "captcha"){
            checkCaptchaChecked();
        }
    }
    var keyup = function () {
        var captchaText = $("input[name='captcha']").val();
        if (captchaText.length > 5){
            captchaText = captchaText.substring(0,5);
            $("input[name='captcha']").val(captchaText);
        }
    }

    return {
        checkCaptchaChecked: function(){
            checkCaptchaChecked();
        },
        refreshCaptcha: function () {
            refreshCaptcha();
        },
        login: function () {
            login();
        },
        inputFocus: function (id, value) {
            inputFocus(id, value);
        },
        inputBlur: function (id, value) {
            inputBlur(id, value);
        },
        keyup: function () {
            keyup();
        }
    }
}();
$(function() {
    login.refreshCaptcha();
    $("#captcha").on("keyup", login.keyup)
    $("#captchaImg").on("click", login.refreshCaptcha);
    $("#login").on("click", login.login);
})