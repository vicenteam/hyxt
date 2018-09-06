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
            {title: '身份证号', field: 'membercadid', visible: true, align: 'center', valign: 'middle'},
            {title: '联系电话', field: 'memberPhone', visible: true, align: 'center', valign: 'middle'},
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
            {title: '会员id', field: 'memberid', visible: false, align: 'center', valign: 'middle'},
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
 * 查询新增积分列表
 */
Integralrecord.search = function () {
    var queryData = {};
    queryData['operator'] = $("#operator").val();
    queryData['memberName'] = $("#memberName").val();
    queryData['cadId'] = $("#cadId").val();
    queryData['integralType'] = $("#integralType").val();
    queryData['begindate'] = $("#begindate").val();
    queryData['enddate'] = $("#enddate").val();
    Integralrecord.table.refresh({query: queryData});
};

Integralrecord.form = function () {
    var queryData = {};
    queryData['operator'] = $("#operator").val();
    queryData['memberName'] = $("#memberName").val();
    queryData['cadId'] = $("#cadId").val();
    queryData['integralType'] = $("#integralType").val();
    queryData['begindate'] = $("#begindate").val();
    queryData['enddate'] = $("#enddate").val();
    return queryData;
};

$(function () {
    var defaultColunms = Integralrecord.initColumn();
    var table = new BSTable(Integralrecord.id, "/integralrecordquery/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    table.setQueryParams(Integralrecord.form());
    Integralrecord.table = table.init();
});
