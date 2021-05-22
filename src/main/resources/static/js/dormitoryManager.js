var contentDormitoryManagerData;
var editContentDormitoryManagerData;
/**
 * 宿管管理导航
 */
function dormitoryManagerNav() {
    navSelected('#dormitoryManagerNavItem');
    contentHeadShow('.dormitoryManagerContentHead');
    clearContentTable();
    initDormitoryManager();
}

/**
 * 宿管导航初始化
 */
function initDormitoryManager() {
    $('#contentData').bootstrapTable({
        data: contentDormitoryManagerData,
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
            title: '宿管编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'name',
            title: '姓名',
            align: 'left',
            valign: 'left'
        }, {
            field: 'sex',
            title: '性别',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'password',
            title: '密码',
            align: 'left',
            valign: 'left'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: dormitoryManagerFormatter
        }]
    });
    initDormitoryManagerData();
}

function initDormitoryManagerData() {
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitoryManager',
        success: function (data) {
            contentDormitoryManagerData = data._embedded.dormitoryManager;
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentDormitoryManagerData, dataType: "json"});
        },
        error: function (data) {
        }
    });
}

function dormitoryManagerFormatter(value, row, index) {
    var id = value;
    var result = "";
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#dormitoryManagerUpdate'  onclick=\"dormitoryManagerUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"dormitoryManagerDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}

/**
 * 宿管查询
 */
function dormitoryManagerQuery() {
    var dormitoryManagerSn = $("#dormitoryManagerSn").val();
    var dormitoryManagerName = $("#dormitoryManagerName").val();

    if (isNull(dormitoryManagerSn)) {
        if (isNull(dormitoryManagerName)) {
            //均为空，初始化查询
            initDormitoryManagerData();
        } else {
            //按照姓名查询
            $.ajax({
                async: false,
                cache: false,
                type: 'GET',
                datType: "json",
                accept: "application/json;charset=UTF-8",
                contentType: "application/json;charset=UTF-8",
                url: '/dormitoryManager/search/findByName?name=' + dormitoryManagerName,
                success: function (data) {
                    contentDormitoryManagerData = data._embedded.dormitoryManager;
                    var table = $('#contentData');
                    table.bootstrapTable('refreshOptions', {data: contentDormitoryManagerData, dataType: "json"});
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
            url: '/dormitoryManager/search/findBySn?sn=' + dormitoryManagerSn,
            success: function (data) {
                var dataArray = new Array();
                if (!isNull(data)) {
                    dataArray.push(data);
                }
                contentDormitoryManagerData = dataArray;
                var table = $('#contentData');
                table.bootstrapTable('refreshOptions', {data: contentDormitoryManagerData, dataType: "json"});
            },
            error: function (data) {
            }
        });
    }
}

function dormitoryManagerAddSave() {
    var data = {};
    var sex = $('input[type="radio"][name="addDormitoryManagerSex"]:checked').val();
    data.sn = $("#addDormitoryManagerSn").val();
    data.name = $("#addDormitoryManagerName").val();
    data.password = $("#addDormitoryManagerPassword").val();
    data.sex = sex;
    $.ajax({
        async: true,
        cache: false,
        type: 'POST',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitoryManager',
        success: function (data) {
            swal('温馨提示', '新增宿管成功', 'success');
            initDormitoryManagerData();
        },
        error: function (data) {
            swal('温馨提示', '新增宿管失败', 'error');
        }
    });
}

function dormitoryManagerUpdate(index) {
    var row = contentDormitoryManagerData[index];
    editContentDormitoryManagerData = contentDormitoryManagerData[index];
    $("#updateDormitoryManagerId").val(row.id);
    $("#updateDormitoryManagerSn").val(row.sn);
    $("#updateDormitoryManagerName").val(row.name);
    $("#updateDormitoryManagerPassword").val(row.password);
    if ('男' === row.sex) {
        $("#updateDormitoryManagerSexM").prop('checked', true);
        $("#updateDormitoryManagerSexF").prop('checked', false);
    } else {
        $("#updateDormitoryManagerSexF").prop('checked', true);
        $("#updateDormitoryManagerSexM").prop('checked', false);
    }
}

function dormitoryManagerUpdateSave(){
    var data = {};
    data.id = $("#updateDormitoryManagerId").val();
    data.sn = $("#updateDormitoryManagerSn").val();
    data.name = $("#updateDormitoryManagerName").val();
    data.password = $("#updateDormitoryManagerPassword").val();
    data.createTime = editContentDormitoryManagerData.createTime;
    var sex = $('input[type="radio"][name="updateDormitoryManagerSex"]:checked').val();
    data.sex = sex;
    $.ajax({
        async: true,
        cache: false,
        type: 'PUT',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitoryManager/' + data.id,
        success: function (data) {
            swal('温馨提示', '修改宿管成功', 'success');
            initDormitoryManagerData();
        },
        error: function (data) {
            swal('温馨提示', '修改宿管失败', 'error');
        }
    });
}

function dormitoryManagerDelete(id) {
    var data = {};
    data.id = id;
    $.ajax({
        async: true,
        cache: false,
        type: 'DELETE',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitoryManager/'+id,
        success: function (data) {
            swal('温馨提示', '删除宿管成功', 'success');
            initDormitoryManagerData();
        },
        error: function (data) {
            swal('温馨提示', '删除宿管失败', 'error');
        }
    });
}

/**
 * 上传Excel文件
 */
function dormitoryManagerUpload() {
    var uploadData = new FormData();
    var uploadName = $("#dormitoryManagerUploadFile").val();
    uploadData.append("file", $("#dormitoryManagerUploadFile")[0].files[0]);
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