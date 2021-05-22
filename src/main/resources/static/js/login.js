/**
 * 初始化验证码
 */
function initCaptcha() {
    $("#captchaImg").src = "/captcha?t=" + new Date().getTime();
}

/**
 * 校验账户和密码录入非空，光标离开时非空提示
 * @param input
 */
function checkInputInfoNull(input) {
    if (input == 'account') {
        var account = $("#account").val();
        if (isNull(account)) {
            $("#accountCheckInfo").html("账户不能为空！");
        } else {
            $("#accountCheckInfo").html("");
        }
    } else if (input == 'password') {
        var password = $("#password").val();
        if (isNull(password)) {
            $("#passwordCheckInfo").html("密码不能为空！");
        } else {
            $("#passwordCheckInfo").html("");
        }
    }
}

/**
 * 生成随机的UUID
 */
function genUuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
    var uuid = s.join("");
    return uuid;
}

/**
 * 判断字符串非空
 */
function isNull(str) {
    if (str === null || str === undefined || str === '' || str.replace(/(^\s*)|(\s*$)/g, '') === '') {
        return true;
    } else {
        return false;
    }
}

/**
 * 登陆
 */
function login() {
    var account = $("#account").val();
    var password = $("#password").val();
    var type = $('input[type="radio"][name="type"]:checked').val();
    var captcha = $("#captcha").val();
    var check = checkInputInfo(account, password, type, captcha);
    if (check !== null) {
        swal("温馨提示！", check, "error");
        return;
    }
    var data = {};
    data.account = account;
    data.password = password;
    data.type = type;
    data.captcha = captcha;
    data.requestId = genUuid();
    data.operator = account;
    $.ajax({
        async: false,
        cache: false,
        type: 'POST',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/login',
        success: function (data) {
            if (data.code === '0000') {
                localStorage.setItem("account", account);
                localStorage.setItem("type", type);
                window.location.href = "index.html";
            } else {
                swal("登陆失败！", data.info, "error");
            }
        },
        error: function (data) {
            window.location.href = "500.html";
        }
    });
}

/**
 * 校验登录录入信息是否为空
 * @param account
 * @param password
 * @param type
 * @param captcha
 * @returns {string|null}
 */
function checkInputInfo(account, password, type, captcha) {
    if (isNull(account)) {
        return "账户不能为空！";
    }
    if (isNull(password)) {
        return "密码不能为空！";
    }
    if (isNull(type)) {
        return "请您选择登录类型！";
    }
    if (isNull(captcha)) {
        return "验证码不能为空！";
    }
    return null;
}