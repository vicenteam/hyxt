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
            {title: '会员名称', field: 'memberName', visible: true, align: 'center', valign: 'middle'},
            {title: '会员卡号', field: 'membercadid', visible: true, align: 'center', valign: 'middle'},
            {title: '积分值', field: 'integral', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
                     if (row.type == 5||row.type == 3){
                        return "<span style='color: red'>-"+value+"</span>";
                    }else {
                         return "<span style='color: green'>+"+value+"</span>";
                     }
                }},
            {title: '附加参数，如果是本人或新人购物获得积分，则该列的值是该购物记录的ID，如果是带新人或新人签到获得积分，则是所带新人的ID', field: 'target', visible: false, align: 'center', valign: 'middle'},
            {title: '积分类型', field: 'type', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
            if(value==0){
                return '购物';
            }else if (value == 1) {
                return '推荐新人';
            }else if (value == 2){
                return "赠送";
            }else if (value == 3){
                return "清零积分";
            }else if (value == 4){
                return "恢复积分";
            }else if (value == 5){
                return "兑换";
            }
            }},
            {title: '操作人', field: 'staffName', visible: true, align: 'center', valign: 'middle'},
            {title: '会员id', field: 'memberid', visible: false, align: 'center', valign: 'middle',formatter: function (value, row, index) {
                    console.log(value)
                }},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
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
    var table = new BSTable(Integralrecord.id, "/integralrecordquery/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    Integralrecord.table = table.init();
});
