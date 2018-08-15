/**
 * 新增积分管理初始化
 */
var Integralrecord = {
    id: "IntegralrecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Integralrecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '积分记录编号', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '部门', field: 'deptName', visible: true, align: 'center', valign: 'middle'},
            {title: '时间', field: 'time', visible: true, align: 'center', valign: 'middle'},
            {title: '操作', field: 'operation', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Integralrecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Integralrecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加新增积分
 */
Integralrecord.openAddIntegralrecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加新增积分',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/integralrecordquery/integralrecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看新增积分详情
 */
Integralrecord.openIntegralrecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '新增积分详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/integralrecordquery/integralrecord_update/' + Integralrecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除新增积分
 */
Integralrecord.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/integralrecordquery/delete", function (data) {
            Feng.success("删除成功!");
            Integralrecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("integralrecordId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询新增积分列表
 */
Integralrecord.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Integralrecord.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Integralrecord.initColumn();
    var table = new BSTable(Integralrecord.id, "/integralrecordclearzero/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    Integralrecord.table = table.init();
});
