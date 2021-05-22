var contentStudentData;
var editContentStudentData;

/**
 * 学生管理导航
 */
function studentNav() {
    navSelected('#studentNavItem');
    contentHeadShow('.studentContentHead');
    clearContentTable();
    initStudent();
}

/**
 * 学生导航初始化
 */
function initStudent() {
    $('#contentData').bootstrapTable({
        data: contentStudentData,
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
            field: 'sn',
            title: '学生编号',
            align: 'center',
            valign: 'left'
        }, {
            field: 'name',
            title: '姓名',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'sex',
            title: '性别',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: studentFormatter
        }]
    });
    initStudentData();
}

/**
 * 初始化学生数据
 */
function initStudentData() {
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/student',
        success: function (data) {
            contentStudentData = data._embedded.student;
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentStudentData, dataType: "json"});
        },
        error: function (data) {
        }
    });
}

/**
 * 学生表格的修改和删除操作
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function studentFormatter(value, row, index) {
    var id = value;
    var result = "";
    if (loginType === '3') {
        return result;
    }
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#studentUpdate' onclick=\"studentUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"studentDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}

/**
 * 学生查询
 */
function studentQuery() {
    var studentSn = $("#studentSn").val();
    var studentName = $("#studentName").val();

    if (isNull(studentSn)) {
        if (isNull(studentName)) {
            //均为空，初始化查询
            initStudentData();
        } else {
            //按照姓名查询
            $.ajax({
                async: false,
                cache: false,
                type: 'GET',
                datType: "json",
                accept: "application/json;charset=UTF-8",
                contentType: "application/json;charset=UTF-8",
                url: '/student/search/findByName?name=' + studentName,
                success: function (data) {
                    contentStudentData = data._embedded.student;
                    var table = $('#contentData');
                    table.bootstrapTable('refreshOptions', {data: contentStudentData, dataType: "json"});
                },
                error: function (data) {
                }
            });
        }
    } else {
        //按照学生编号查询
        $.ajax({
            async: false,
            cache: false,
            type: 'GET',
            datType: "json",
            accept: "application/json;charset=UTF-8",
            contentType: "application/json;charset=UTF-8",
            url: '/student/search/findBySn?sn=' + studentSn,
            success: function (data) {
                var dataArray = new Array();
                if (!isNull(data)) {
                    dataArray.push(data);
                }
                contentStudentData = dataArray;
                var table = $('#contentData');
                table.bootstrapTable('refreshOptions', {data: contentStudentData, dataType: "json"});
            },
            error: function (data) {
            }
        });
    }
}

/**
 * 新增保存学生
 */
function studentAddSave() {
    var data = {};
    var sex = $('input[type="radio"][name="addStudentSex"]:checked').val();
    data.sn = $("#addStudentSn").val();
    data.name = $("#addStudentName").val();
    data.password = $("#addStudentPassword").val();
    data.sex = sex;
    $.ajax({
        async: true,
        cache: false,
        type: 'POST',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/student',
        success: function (data) {
            swal('温馨提示', '新增学生成功', 'success');
            initStudentData();
        },
        error: function (data) {
            swal('温馨提示', '新增学生失败', 'error');
        }
    });
}

/**
 * 学生更新
 * @param index
 */
function studentUpdate(index) {
    var row = contentStudentData[index];
    editContentStudentData = contentStudentData[index];
    $("#updateStudentId").val(row.id);
    $("#updateStudentSn").val(row.sn);
    $("#updateStudentName").val(row.name);
    $("#updateStudentPassword").val(row.password);
    if ('男' === row.sex) {
        $("#updateStudentSexM").prop('checked', true);
        $("#updateStudentSexF").prop('checked', false);
    } else {
        $("#updateStudentSexF").prop('checked', true);
        $("#updateStudentSexM").prop('checked', false);
    }
}

function studentUpdateSave() {
    var data = {};
    data.id = $("#updateStudentId").val();
    data.sn = $("#updateStudentSn").val();
    data.name = $("#updateStudentName").val();
    data.password = $("#updateStudentPassword").val();
    data.createTime = editContentStudentData.createTime;
    var sex = $('input[type="radio"][name="updateStudentSex"]:checked').val();
    data.sex = sex;
    $.ajax({
        async: true,
        cache: false,
        type: 'PUT',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/student/' + data.id,
        success: function (data) {
            swal('温馨提示', '修改学生成功', 'success');
            initStudentData();
        },
        error: function (data) {
            swal('温馨提示', '修改学生失败', 'error');
        }
    });
}

/**
 * 学生删除
 * @param id
 */
function studentDelete(id) {
    $.ajax({
        async: true,
        cache: false,
        type: 'DELETE',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/student/' + id,
        success: function (data) {
            swal('温馨提示', '删除学生成功', 'success');
            initStudentData();
        },
        error: function (data) {
            swal('温馨提示', '删除学生失败', 'error');
        }
    });
}

/**
 * 上传Excel文件
 */
function studentUpload() {
    var uploadData = new FormData();
    var uploadName = $("#studentUploadFile").val();
    uploadData.append("file", $("#studentUploadFile")[0].files[0]);
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