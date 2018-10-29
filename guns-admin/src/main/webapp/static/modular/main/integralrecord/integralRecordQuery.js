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
                return 'XF';
            }else if (value == 1) {
                return 'TJXR';
            }else if (value == 2){
                return "ZS";
            }else if (value == 3){
                return "QLJF";
            }else if (value == 4){
                return "HFJF";
            }else if (value == 5){
                return "DH";
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
    queryData['memberId'] = "";
    Integralrecord.table.refresh({query: queryData});
};

Integralrecord.aaa = function () {
    var queryData = {};
    readDeviceCard();
//校验密码
    RfAuthenticationKey();
    var ret = CZx_32Ctrl.RfRead(DeviceHandle.value,BlockM1.value);
    if(CZx_32Ctrl.lErrorCode == 0) {
        DevBeep();
        $.ajax({
            url: '/membermanagement/getUserInfo',
            // data: {value:ret},
            data: {value: $("#readDeviceCard").val()},
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            async: false,
            success: function (data) {
                if (data.id != undefined) {
                    queryData['operator'] = $("#operator").val();
                    queryData['memberName'] = $("#memberName").val();
                    queryData['cadId'] = $("#cadId").val();
                    queryData['integralType'] = $("#integralType").val();
                    queryData['begindate'] = $("#begindate").val();
                    queryData['enddate'] = $("#enddate").val();
                    queryData['memberId'] = data.memberid;
                } else {
                    if (data == "202") {
                        Feng.error("该卡已挂失无法执行该操作!");
                    } else {
                        queryData['operator'] = $("#operator").val();
                        queryData['memberName'] = $("#memberName").val();
                        queryData['cadId'] = $("#cadId").val();
                        queryData['integralType'] = $("#integralType").val();
                        queryData['begindate'] = $("#begindate").val();
                        queryData['enddate'] = $("#enddate").val();
                        queryData['memberId'] = -1;
                    }
                }

                Integralrecord.table.refresh({query: queryData});
            }
        })
    }
};

Integralrecord.form = function () {
    var queryData = {};
    queryData['operator'] = $("#operator").val();
    queryData['memberName'] = $("#memberName").val();
    queryData['cadId'] = $("#cadId").val();
    queryData['integralType'] = $("#integralType").val();
    queryData['begindate'] = $("#begindate").val();
    queryData['enddate'] = $("#enddate").val();
    queryData['memberId'] = -1;
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
