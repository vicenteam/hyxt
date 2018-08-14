/**
 * 签到场次管理初始化
 */
var Checkin = {
    id: "CheckinTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Checkin.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'screenings', visible: true, align: 'center', valign: 'middle'},
            {title: '签到人数', field: 'memberCount', visible: true, align: 'center', valign: 'middle'},
            {title: '新卡签到数', field: 'newCount', visible: true, align: 'center', valign: 'middle'},
            {title: '开始时间', field: 'startDate', visible: true, align: 'center', valign: 'middle'},
            {title: '签到场次状态；1：签到中   2：结束签到', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '结束时间', field: 'endDate', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'createDate', visible: true, align: 'center', valign: 'middle'},
            {title: '门店ID', field: 'deptId', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'isActive', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Checkin.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Checkin.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加签到场次
 */
Checkin.openAddCheckin = function () {
    var index = layer.open({
        type: 2,
        title: '添加签到场次',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/checkin/checkin_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看签到场次详情
 */
Checkin.openCheckinDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '签到场次详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/checkin/checkin_update/' + Checkin.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除签到场次
 */
Checkin.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/checkin/delete", function (data) {
            Feng.success("删除成功!");
            Checkin.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("checkinId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询签到场次列表
 */
Checkin.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Checkin.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Checkin.initColumn();
    var table = new BSTable(Checkin.id, "/checkin/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    Checkin.table = table.init();
});
