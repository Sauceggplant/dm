var loginType;

/**
 * 导航选择
 */
function navSelected(nav) {
    $('.nav-item').css('background', '#92B3C4');
    $(nav).css('background', '#EEEEEE');
}

/**
 * 内容头部展示
 */
function contentHeadShow(head) {
    $('.contentHead').css('display', 'none');
    $(head).css('display', '');
}

/**
 * 点击导航清除内容数据
 */
function clearContentTable() {
    $('.contentTable').html('<table class="table table-bordered table-hover table-condensed" id="contentData" name="contentData"></table>');
}

/**
 * 判断字符串非空
 */
function isNull(str) {
    if (str === null || str === undefined || str === '') {
        return true;
    } else {
        return false;
    }
}

/**
 * 退出
 */
function exit() {
    var account = localStorage.getItem("account");
    var type = localStorage.getItem("type");
    var data = {};
    data.account = account;
    data.type = type;
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
        url: '/logout',
        success: function (data) {
            if (data.code === '0000') {
                window.location.href = "login.html";
            } else {
                window.location.href = "500.html";
            }
        },
        error: function (data) {
            window.location.href = "500.html";
        }
    });
    localStorage.setItem("account", "");
    localStorage.setItem("type", "");
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