var contentBuildingData;
var editContentBuildingData;

/**
 * 楼宇管理导航
 */
function buildingNav() {
    navSelected('#buildingNavItem');
    contentHeadShow('.buildingContentHead');
    clearContentTable();
    initBuilding();
}

/**
 * 楼宇导航初始化
 */
function initBuilding() {
    $('#contentData').bootstrapTable({
        data: contentBuildingData,
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
            field: 'name',
            title: '楼宇名称',
            align: 'left',
            valign: 'left'
        }, {
            field: 'location',
            title: '楼宇所属位置',
            align: 'left',
            valign: 'left'
        }, {
            field: 'dormitoryManagerSn',
            title: '宿管编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'dormitoryManagerName',
            title: '宿管名称',
            align: 'left',
            valign: 'left'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: buildingFormatter
        }]
    });
    initBuildingData();
}

/**
 * 初始化楼宇数据
 */
function initBuildingData() {
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/building',
        success: function (data) {
            contentBuildingData = data._embedded.building;
            addDormitoryManagerData(contentBuildingData);
        },
        error: function (data) {
        }
    });
}

function addDormitoryManagerData(contentBuildingData) {
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitoryManager',
        success: function (data) {
            var dormitoryManagerData = data._embedded.dormitoryManager;
            for (var i = 0; i < contentBuildingData.length; i++) {
                for (var j = 0; i < dormitoryManagerData.length; j++) {
                    if (contentBuildingData[i].dormitoryManagerId === dormitoryManagerData[j].id) {
                        contentBuildingData[i].dormitoryManagerSn = dormitoryManagerData[j].sn;
                        contentBuildingData[i].dormitoryManagerName = dormitoryManagerData[j].name;
                        break;
                    }
                }
            }
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentBuildingData, dataType: "json"});
        },
        error: function (data) {
        }
    });
}

/**
 * 楼宇表格的修改和删除操作
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function buildingFormatter(value, row, index) {
    var id = value;
    var result = "";
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#buildingUpdate' onclick=\"buildingUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"buildingDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}

/**
 * 楼宇查询
 */
function buildingQuery() {
    var buildingName = $("#buildingName").val();
    if (isNull(buildingName)) {
        $.ajax({
            async: false,
            cache: false,
            type: 'GET',
            datType: "json",
            accept: "application/json;charset=UTF-8",
            contentType: "application/json;charset=UTF-8",
            url: '/building',
            success: function (data) {
                contentBuildingData = data._embedded.building;
                addDormitoryManagerData(contentBuildingData);
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
            url: '/building/search/findByName?name=' + buildingName,
            success: function (data) {
                var dataArray = new Array();
                if (!isNull(data)) {
                    dataArray.push(data);
                }
                contentBuildingData = dataArray;
                addDormitoryManagerData(contentBuildingData);
            },
            error: function (data) {
            }
        });
    }
}

function buildingAdd() {
    var contentData;
    var html = "";
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitoryManager',
        success: function (data) {
            contentData = data._embedded.dormitoryManager;
            for (var i = 0; i < contentData.length; i++) {
                html += "<option value=\"" + contentData[i].id + "\">" + contentData[i].name + "</option>";
            }
            $("#addBuildingDormitoryManager").html(html);
        },
        error: function (data) {
        }
    });
}

function buildingAddSave() {
    var data = {};
    data.name = $("#addBuildingName").val();
    data.location = $("#addBuildingLocation").val();
    data.dormitoryManagerId = $("#addBuildingDormitoryManager").val();
    $.ajax({
        async: true,
        cache: false,
        type: 'POST',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/building',
        success: function (data) {
            swal('温馨提示', '新增楼宇成功', 'success');
            initBuildingData();
        },
        error: function (data) {
            swal('温馨提示', '新增楼宇失败', 'error');
        }
    });
}

function buildingUpdate(index) {
    var row = contentBuildingData[index];
    editContentBuildingData = contentBuildingData[index];
    $("#updateBuildingId").val(row.id);
    $("#updateBuildingName").val(row.name);
    $("#updateBuildingLocation").val(row.location);
    var contentData;
    var html = "";
    $.ajax({
        async: false,
        cache: false,
        type: 'GET',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/dormitoryManager',
        success: function (data) {
            contentData = data._embedded.dormitoryManager;
            for (var i = 0; i < contentData.length; i++) {
                html += "<option value=\"" + contentData[i].id + "\">" + contentData[i].name + "</option>";
            }
            $("#updateBuildingDormitoryManager").html(html);
        },
        error: function (data) {
        }
    });
}

function buildingUpdateSave() {
    var data = {};
    data.id = $("#updateBuildingId").val();
    data.name = $("#updateBuildingName").val();
    data.location = $("#updateBuildingLocation").val();
    data.dormitoryManagerId = $("#updateBuildingDormitoryManager").val();
    data.createTime = editContentBuildingData.createTime;
    $.ajax({
        async: true,
        cache: false,
        type: 'PUT',
        data: JSON.stringify(data),
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/building/' + data.id,
        success: function (data) {
            swal('温馨提示', '修改楼宇成功', 'success');
            initBuildingData();
        },
        error: function (data) {
            swal('温馨提示', '修改楼宇失败', 'error');
        }
    });
}

function buildingDelete(id) {
    var data = {};
    data.id = id;
    $.ajax({
        async: true,
        cache: false,
        type: 'DELETE',
        datType: "json",
        accept: "application/json;charset=UTF-8",
        contentType: "application/json;charset=UTF-8",
        url: '/building/' + id,
        success: function (data) {
            swal('温馨提示', '删除楼宇成功', 'success');
            initBuildingData();
        },
        error: function (data) {
            swal('温馨提示', '删除楼宇失败', 'error');
        }
    });
}

/**
 * 上传Excel文件
 */
function buildingUpload() {
    var uploadData = new FormData();
    var uploadName = $("#buildingUploadFile").val();
    uploadData.append("file", $("#buildingUploadFile")[0].files[0]);
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