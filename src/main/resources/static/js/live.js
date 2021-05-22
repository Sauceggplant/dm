var contentLiveData;
var editContentLiveData;

/**
 * 入住管理导航
 */
function liveNav() {
    navSelected('#liveNavItem');
    contentHeadShow('.liveContentHead');
    clearContentTable();
    initLive();
}

/**
 * 入住导航初始化
 */
function initLive() {
    $('#contentData').bootstrapTable({
        data: contentLiveData,
        dataType: 'json',
        pagination: true,
        pageSize: 5,
        striped: true,
        search: false,
        singleSelect: false,
        showHeader: true,
        showFooter: false,
        showColumns: false,
        showRefresh: false,
        showToggle: false,
        sortable: false,
        columns: [{
            field: 'studentSn',
            title: '学生编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'studentName',
            title: '学生姓名',
            align: 'left',
            valign: 'left'
        }, {
            field: 'dormitorySn',
            title: '宿舍编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'liveDate',
            title: '入住日期',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: liveFormatter
        }]
    });
    initLiveData();
}

function initLiveData() {
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/live',
        success: function (data) {
            contentLiveData = data._embedded.live;
            addStudentAndDormitoryData(contentLiveData);
        },
        error: function (data) {
        }
    });
}

function addStudentAndDormitoryData(contentLiveData) {
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/student',
        success: function (data) {
            var studentData = data._embedded.student;
            $.ajax({
                async: false,
                cache: false,
                type: 'GET',
                datType: "json",
                accept: "application/json;charset=UTF-8",
                contentType: "application/json;charset=UTF-8",
                url: '/dormitory',
                success: function (data) {
                    var dormitoryData = data._embedded.dormitory;
                    for (var i = 0; i < contentLiveData.length; i++) {
                        for (var j = 0; i < studentData.length; j++) {
                            if (contentLiveData[i].studentId === studentData[j].id) {
                                contentLiveData[i].studentName = studentData[j].name;
                                contentLiveData[i].studentSn = studentData[j].sn;
                                break;
                            }
                        }
                        for (var k = 0; k < dormitoryData.length; k++) {
                            if (contentLiveData[i].dormitoryId === dormitoryData[k].id) {
                                contentLiveData[i].dormitorySn = dormitoryData[k].sn;
                                break;
                            }
                        }
                    }
                    var table = $('#contentData');
                    table.bootstrapTable('refreshOptions', {data: contentLiveData, dataType: "json"});
                },
                error: function (data) {
                }
            });
        },
        error: function (data) {
        }
    });
}

function liveFormatter(value, row, index) {
    var id = value;
    var result = "";
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#liveUpdate' onclick=\"liveUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"liveDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}

function liveQuery() {
    var liveDormitorySn = $("#liveDormitorySn").val();
    var liveStudentNameOrSn = $("#liveStudentNameOrSn").val();
    initLiveData();

    if (isNull(liveDormitorySn)) {
        if (isNull(liveStudentNameOrSn)) {
            initLiveData();
        } else {
            //学生过滤
            var afterContentLiveData = new Array();
            for (var i = 0; i < contentLiveData.length; i++) {
                if (contentLiveData[i].studentSn === liveStudentNameOrSn) {
                    afterContentLiveData.push(contentLiveData[i]);
                } else if (contentLiveData[i].studentName == liveStudentNameOrSn) {
                    afterContentLiveData.push(contentLiveData[i]);
                }
            }
            contentLiveData = afterContentLiveData;
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentLiveData, dataType: "json"});
        }
    } else {
        if (isNull(liveStudentNameOrSn)) {
            //宿舍过滤
            var afterContentLiveData = new Array();
            for (var i = 0; i < contentLiveData.length; i++) {
                if (contentLiveData[i].dormitorySn === liveDormitorySn) {
                    afterContentLiveData.push(contentLiveData[i]);
                }
            }
            contentLiveData = afterContentLiveData;
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentLiveData, dataType: "json"});
        } else {
            //学生宿舍过滤
            var afterContentLiveData = new Array();
            for (var i = 0; i < contentLiveData.length; i++) {
                if (contentLiveData[i].studentSn === liveStudentNameOrSn) {
                    if (contentLiveData[i].dormitorySn === liveDormitorySn) {
                        afterContentLiveData.push(contentLiveData[i]);
                    }
                } else if (contentLiveData[i].studentName == liveStudentNameOrSn) {
                    if (contentLiveData[i].dormitorySn === liveDormitorySn) {
                        afterContentLiveData.push(contentLiveData[i]);
                    }
                }
            }
            contentLiveData = afterContentLiveData;
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentLiveData, dataType: "json"});
        }
    }
}

function liveAdd() {
    var selectStudentData;
    var selectDormitoryData;
    var html1 = "";
    var html2 = "";
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/student',
        success: function (data) {
            selectStudentData = data._embedded.student;
            for (var i = 0; i < selectStudentData.length; i++) {
                html1 += "<option value=\"" + selectStudentData[i].id + "\">" + selectStudentData[i].name + "</option>";
            }
            $("#addLiveStudentId").html(html1);
        },
        error: function (data) {
        }
    });
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitory',
        success: function (data) {
            selectDormitoryData = data._embedded.dormitory;
            for (var i = 0; i < selectDormitoryData.length; i++) {
                html2 += "<option value=\"" + selectDormitoryData[i].id + "\">" + selectDormitoryData[i].sn + "</option>";
            }
            $("#addLiveDormitoryId").html(html2);
        },
        error: function (data) {
        }
    });
}

function liveAddSave() {
    var data = {};
    data.studentId = $("#addLiveStudentId").val();
    data.dormitoryId = $("#addLiveDormitoryId").val();
    data.liveDate = $("#addLiveDate").val();
    $.ajax({
        async: true,
        cache: false,
        type: 'POST',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/live',
        success: function (data) {
            swal('温馨提示', '新增入住成功', 'success');
            initLiveData();
        },
        error: function (data) {
            swal('温馨提示', '新增入住失败', 'error');
        }
    });
}

function liveUpdate(index) {
    var row = contentLiveData[index];
    var selectStudentData;
    var selectDormitoryData;
    var html1 = "";
    var html2 = "";
    editContentLiveData = contentLiveData[index];
    $("#updateLiveId").val(row.id);
    $("#updateLiveDate").val(row.liveDate);
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/student',
        success: function (data) {
            selectStudentData = data._embedded.student;
            for (var i = 0; i < selectStudentData.length; i++) {
                html1 += "<option value=\"" + selectStudentData[i].id + "\">" + selectStudentData[i].name + "</option>";
            }
            $("#updateLiveStudentId").html(html1);
        },
        error: function (data) {
        }
    });
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitory',
        success: function (data) {
            selectDormitoryData = data._embedded.dormitory;
            for (var i = 0; i < selectDormitoryData.length; i++) {
                html2 += "<option value=\"" + selectDormitoryData[i].id + "\">" + selectDormitoryData[i].sn + "</option>";
            }
            $("#updateLiveDormitoryId").html(html2);
        },
        error: function (data) {
        }
    });
}

function liveUpdateSave() {
    var data = {};
    data.id = $("#updateLiveId").val();
    data.liveDate = $("#updateLiveDate").val();
    data.studentId = $("#updateLiveStudentId").val();
    data.dormitoryId = $("#updateLiveDormitoryId").val();
    data.createTime = editContentLiveData.createTime;
    $.ajax({
        async: true,
        cache: false,
        type: 'PUT',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/live/' + data.id,
        success: function (data) {
            swal('温馨提示', '修改入住成功', 'success');
            initLiveData();
        },
        error: function (data) {
            swal('温馨提示', '修改入住失败', 'error');
        }
    });
}

function liveDelete(id) {
    var data = {};
    data.id = id;
    $.ajax({
        async: true,
        cache: false,
        type: 'DELETE',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/live/' + id,
        success: function (data) {
            swal('温馨提示', '删除入住成功', 'success');
            initLiveData();
        },
        error: function (data) {
            swal('温馨提示', '删除入住失败', 'error');
        }
    });
}

/**
 * 上传Excel文件
 */
function liveUpload() {
    var uploadData = new FormData();
    var uploadName = $("#liveUploadFile").val();
    uploadData.append("file", $("#liveUploadFile")[0].files[0]);
    uploadData.append("name", uploadName);
    $.ajax({
        url: '/excel/import',
        type: 'POST',
        async: false,
        data: uploadData,
        // 告诉jQuery不要去处理发送的数据
        processData: false,
        // 告诉jQuery不要去设置Content-Type请求头
        contentType: false,
        beforeSend: function () {
            console.log("正在进行，请稍候");
        },
        success: function (data) {
            swal('温馨提示', '导入成功', 'success');
        }
    });
}