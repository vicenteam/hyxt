/**
 * 会员基础信息管理初始化
 */
var Membermanagement = {
    id: "MembermanagementTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Membermanagement.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '编号id', field: 'cadID', visible: true, align: 'center', valign: 'middle'},
            {title: '姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '电话', field: 'telphone', visible: true, align: 'center', valign: 'middle'},
            {title: '性别(1男 2女)', field: 'sex', visible: true, align: 'center', valign: 'middle'},
            {title: '邮箱地址', field: 'email', visible: true, align: 'center', valign: 'middle'},
            {title: '联系方式', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '状态(0可用 1不可用)', field: 'state', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'integral', visible: true, align: 'center', valign: 'middle'},
            {title: '会员等级id', field: 'levelID', visible: true, align: 'center', valign: 'middle'},
            {title: '会员卡信息', field: 'cardID', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '是否老年协会会员 (1是2否)', field: 'isoldsociety', visible: true, align: 'center', valign: 'middle'},
            {title: '生日', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'deptName', visible: true, align: 'center', valign: 'middle'},
            {title: '介绍人id', field: 'introducerId', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'province', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'city', visible: true, align: 'center', valign: 'middle'},
            {title: '地址', field: 'district', visible: true, align: 'center', valign: 'middle'},
            {title: '病史', field: 'medicalHistory', visible: true, align: 'center', valign: 'middle'},
            {title: '家庭状态id', field: 'familyStatusID', visible: true, align: 'center', valign: 'middle'},
            {title: '服务员工id', field: 'staffID', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'countyID', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'townshipid', visible: true, align: 'center', valign: 'middle'},
            {title: '健康状态', field: 'healthStatus', visible: true, align: 'center', valign: 'middle'},
            {title: '最新签到时间1', field: 'CheckINTime1', visible: true, align: 'center', valign: 'middle'},
            {title: '最新签到时间2', field: 'CheckINTime2', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'recommendMember', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'address', visible: true, align: 'center', valign: 'middle'},
            {title: '总消费金额', field: 'countPrice', visible: true, align: 'center', valign: 'middle'},
            {title: '用户图片名称', field: 'imgName', visible: true, align: 'center', valign: 'middle'},
            {title: '所属门店id', field: 'deptId', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'token', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Membermanagement.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Membermanagement.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员基础信息
 */
Membermanagement.openAddMembermanagement = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员基础信息',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/membermanagement/membermanagement_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员基础信息详情
 */
Membermanagement.openMembermanagementDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员基础信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/membermanagement/membermanagement_update/' + Membermanagement.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员基础信息
 */
Membermanagement.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/membermanagement/delete", function (data) {
            Feng.success("删除成功!");
            Membermanagement.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("membermanagementId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员基础信息列表
 */
Membermanagement.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Membermanagement.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Membermanagement.initColumn();
    var table = new BSTable(Membermanagement.id, "/membermanagement/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    Membermanagement.table = table.init();
});
