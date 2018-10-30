/**
 * 积分类型管理初始化
 */
var Integralrecordtype = {
    id: "IntegralrecordtypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Integralrecordtype.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '积分类型', field: 'name', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Integralrecordtype.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Integralrecordtype.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加积分类型
 */
Integralrecordtype.openAddIntegralrecordtype = function () {
    var index = layer.open({
        type: 2,
        title: '添加积分类型',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/integralrecordtype/integralrecordtype_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看积分类型详情
 */
Integralrecordtype.openIntegralrecordtypeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '积分类型详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/integralrecordtype/integralrecordtype_update/' + Integralrecordtype.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除积分类型
 */
Integralrecordtype.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/integralrecordtype/delete", function (data) {
            Feng.success("删除成功!");
            Integralrecordtype.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("integralrecordtypeId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询积分类型列表
 */
Integralrecordtype.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Integralrecordtype.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Integralrecordtype.initColumn();
    var table = new BSTable(Integralrecordtype.id, "/integralrecordtype/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    Integralrecordtype.table = table.init();
});
