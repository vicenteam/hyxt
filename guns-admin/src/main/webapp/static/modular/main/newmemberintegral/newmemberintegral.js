/**
 * 新增会员积分管理初始化
 */
var Newmemberintegral = {
    id: "NewmemberintegralTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Newmemberintegral.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'memberid', visible: true, align: 'center', valign: 'middle'},
            {title: '获得积分值', field: 'integral', visible: true, align: 'center', valign: 'middle'},
            {title: '积分类型(1.推荐新人,2.消费,3.兑换)', field: 'integraltype', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'createdt', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'updatedt', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Newmemberintegral.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Newmemberintegral.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加新增会员积分
 */
Newmemberintegral.openAddNewmemberintegral = function () {
    var index = layer.open({
        type: 2,
        title: '添加新增会员积分',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/newmemberintegral/newmemberintegral_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看新增会员积分详情
 */
Newmemberintegral.openNewmemberintegralDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '新增会员积分详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/newmemberintegral/newmemberintegral_update/' + Newmemberintegral.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除新增会员积分
 */
Newmemberintegral.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/newmemberintegral/delete", function (data) {
            Feng.success("删除成功!");
            Newmemberintegral.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("newmemberintegralId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询新增会员积分列表
 */
Newmemberintegral.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Newmemberintegral.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Newmemberintegral.initColumn();
    var table = new BSTable(Newmemberintegral.id, "/newmemberintegral/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    Newmemberintegral.table = table.init();
});
