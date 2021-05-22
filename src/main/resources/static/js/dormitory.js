var contentDormitoryData;
var editContentDormitoryData;

/**
 * 宿舍管理导航
 */
function dormitoryNav() {
    navSelected('#dormitoryNavItem');
    contentHeadShow('.dormitoryContentHead');
    clearContentTable();
    initDormitory();
}

/**
 * 宿舍导航初始化
 */
function initDormitory() {
    $('#contentData').bootstrapTable({
        data: contentDormitoryData,
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
            title: '宿舍编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'buildingName',
            title: '所属楼宇',
            align: 'left',
            valign: 'left'
        }, {
            field: 'floor',
            title: '所属楼层',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'maxNumber',
            title: '最大可住人数',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'livedNumber',
            title: '已住人数',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: dormitoryFormatter
        }]
    });
    initDormitoryData();
}

function initDormitoryData() {
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitory',
        success: function (data) {
            contentDormitoryData = data._embedded.dormitory;
            addBuildingData(contentDormitoryData);
        },
        error: function (data) {
        }
    });
}

function addBuildingData(contentDormitoryData){
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/building',
        success: function (data) {
            var buildingData = data._embedded.building;
            for (var i = 0; i < contentDormitoryData.length; i++) {
                for (var j = 0; i < buildingData.length; j++) {
                    if (contentDormitoryData[i].buildingId === buildingData[j].id) {
                        contentDormitoryData[i].buildingName = buildingData[j].name;
                        break;
                    }
                }
            }
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentDormitoryData, dataType: "json"});
        },
        error: function (data) {
        }
    });
}

function dormitoryFormatter(value, row, index) {
    var id = value;
    var result = "";
    if (loginType === '3') {
        return result;
    }
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#dormitoryUpdate' onclick=\"dormitoryUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"dormitoryDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}


function dormitoryQuery() {
    var dormitorySn = $("#dormitorySn").val();
    if (isNull(dormitorySn)) {
        $.ajax({
            async: false,
            cache: false,
            type: 'GET',
            datType: "json",
            accept: "application/json;charset=UTF-8",
            contentType: "application/json;charset=UTF-8",
            url: '/dormitory',
            success: function (data) {
                contentDormitoryData = data._embedded.dormitory;
                addBuildingData(contentDormitoryData);
            },
            error: function (data) {
            }
        });
    } else {
        $.ajax({
            async: false,
            cache: false,
            type: 'GET',
            datType: "json",
            accept: "application/json;charset=UTF-8",
            contentType: "application/json;charset=UTF-8",
            url: '/dormitory/search/findBySn?sn=' + dormitorySn,
            success: function (data) {
                var dataArray = new Array();
                if (!isNull(data)) {
                    dataArray.push(data);
                }
                contentDormitoryData = dataArray;
                addBuildingData(contentDormitoryData);
            },
            error: function (data) {
            }
        });
    }
}

function dormitoryAdd() {
    var contentData;
    var html = "";
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/building',
        success: function (data) {
            contentData = data._embedded.building;
            for (var i = 0; i < contentData.length; i++) {
                html += "<option value=\"" + contentData[i].id + "\">" + contentData[i].name + "</option>";
            }
            $("#addDormitoryBuildingId").html(html);
        },
        error: function (data) {
        }
    });
}

function dormitoryAddSave() {
    var data = {};
    data.sn = $("#addDormitorySn").val();
    data.buildingId = $("#addDormitoryBuildingId").val();
    data.floor = $("#addDormitoryFloor").val();
    data.maxNumber = $("#addDormitoryMaxNumber").val();
    data.livedNumber = $("#addDormitoryLivedNumber").val();
    $.ajax({
        async: true,
        cache: false,
        type: 'POST',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitory',
        success: function (data) {
            swal('温馨提示', '新增宿舍成功', 'success');
            initDormitoryData();
        },
        error: function (data) {
            swal('温馨提示', '新增宿舍失败', 'error');
        }
    });
}

function dormitoryUpdate(index) {
    var row = contentDormitoryData[index];
    editContentDormitoryData = contentDormitoryData[index];
    $("#updateDormitoryId").val(row.id);
    $("#updateDormitorySn").val(row.sn);
    $("#updateDormitoryFloor").val(row.floor);
    $("#updateDormitoryMaxNumber").val(row.maxNumber);
    $("#updateDormitoryLivedNumber").val(row.livedNumber);
    var contentData;
    var html = "";
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/building',
        success: function (data) {
            contentData = data._embedded.building;
            for (var i = 0; i < contentData.length; i++) {
                html += "<option value=\"" + contentData[i].id + "\">" + contentData[i].name + "</option>";
            }
            $("#updateDormitoryBuildingId").html(html);
        },
        error: function (data) {
        }
    });
}

function dormitoryUpdateSave() {
    var data = {};
    data.id = $("#updateDormitoryId").val();
    data.sn = $("#updateDormitorySn").val();
    data.floor = $("#updateDormitoryFloor").val();
    data.maxNumber = $("#updateDormitoryMaxNumber").val();
    data.livedNumber = $("#updateDormitoryLivedNumber").val();
    data.buildingId = $("#updateDormitoryBuildingId").val();
    data.createTime = editContentDormitoryData.createTime;
    $.ajax({
        async: true,
        cache: false,
        type: 'PUT',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitory/' + data.id,
        success: function (data) {
            swal('温馨提示', '修改宿舍成功', 'success');
            initDormitoryData();
        },
        error: function (data) {
            swal('温馨提示', '修改宿舍失败', 'error');
        }
    });
}

function dormitoryDelete(id) {
    var data = {};
    data.id = id;
    $.ajax({
        async: true,
        cache: false,
        type: 'DELETE',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitory/' + id,
        success: function (data) {
            swal('温馨提示', '删除宿舍成功', 'success');
            initDormitoryData();
        },
        error: function (data) {
            swal('温馨提示', '删除宿舍失败', 'error');
        }
    });
}

/**
 * 上传Excel文件
 */
function dormitoryUpload() {
    var uploadData = new FormData();
    var uploadName = $("#dormitoryUploadFile").val();
    uploadData.append("file", $("#dormitoryUploadFile")[0].files[0]);
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