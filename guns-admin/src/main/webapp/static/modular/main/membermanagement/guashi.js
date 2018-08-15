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
            {title: '', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '性别', field: 'sex', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
                if(value==1){
                    return '男';
                }else {
                    return '女';
                }
                }},
            {title: '联系方式', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            {title: '当前积分', field: 'integral', visible: true, align: 'center', valign: 'middle'},
            {title: '会员等级', field: 'levelID', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '老年协会会员', field: 'isoldsociety', visible: true, align: 'center', valign: 'middle',formatter: function (value, row, index) {
                if(value==1){
                    return '是';
                }else {
                    return '否';
                }
                }},
            {title: '家庭地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            {title: '总获得积分', field: 'countPrice', visible: true, align: 'center', valign: 'middle'},
            {title: '操作', field: 'id', visible: true, align: 'center',  valign: 'middle',formatter: function (value, row, index) {
                    if(row.townshipid==1){
                       return '<button type="button" class="btn btn-primary button-margin" onclick="Membermanagement.guashiData1(' + row.id + ')" id=""><i class="fa fa-unlock"></i>解除挂失</button>';
                    }else {
                        return '<button type="button" class="btn btn-primary button-margin" onclick="Membermanagement.guashiData(' + row.id + ')" id=""><i class="fa fa-unlock-alt"></i>挂失</button>'

                    }
                }},
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
Membermanagement.openMembermanagementDetail = function (id) {
        var index = layer.open({
            type: 2,
            title: '会员基础信息详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/membermanagement/membermanagement_update/' + id
        });
        this.layerIndex = index;
};
/**
 * 打开签到记录页面
 * @param id
 */
Membermanagement.guashiData = function (id) {
    var ajax = new $ax(Feng.ctxPath + "/membermanagement/guashiData", function (data) {
        Feng.success("挂失成功!");
        Membermanagement.table.refresh();
    }, function (data) {
        Feng.error("挂失失败!" + data.responseJSON.message + "!");
    });
    ajax.set("memberId",id);
    ajax.start();
};
Membermanagement.guashiData1 = function (id) {
    var ajax = new $ax(Feng.ctxPath + "/membermanagement/guashiData1", function (data) {
        Feng.success("解除挂失成功!");
        Membermanagement.table.refresh();
    }, function (data) {
        Feng.error("解除挂失失败!" + data.responseJSON.message + "!");
    });
    ajax.set("memberId",id);
    ajax.start();
};
/**
 * 介绍人查询
 * @param id
 */
Membermanagement.openintroducer = function (id) {
        var index = layer.open({
            type: 2,
            title: '我推荐的人',
            area: ['850px', '500px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/membermanagement/openintroducer/' + id
        });
        this.layerIndex = index;
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
    queryData['name'] = $("#name").val();
    queryData['address'] = $("#address").val();
    queryData['fstatus'] = $("#fstatus").val();
    queryData['sex'] = $("#sex").val();
    queryData['idcard'] = $("#idcard").val();
    queryData['phone'] = $("#phone").val();
    queryData['stafff'] = $("#stafff").val();
    queryData['deptid'] = $("#deptid").val();
    queryData['province'] = $("#province").val();
    queryData['city'] = $("#city").val();
    queryData['district'] = $("#district").val();
    queryData['townshipid'] = $("#townshipid").val();
    Membermanagement.table.refresh({query: queryData});
};
Membermanagement.search1 = function () {
    var queryData = {};
    readDeviceCard();
//校验密码
    RfAuthenticationKey();
    var ret = CZx_32Ctrl.RfRead(DeviceHandle.value,BlockM1.value);
    if(CZx_32Ctrl.lErrorCode == 0){
        DevBeep();
        $.ajax({
            url: '/membermanagement/getUserInfo',
            data: {value:ret},
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            async: false,
            success: function (data) {
                if(data.id!=undefined){
                    queryData['memberid'] = data.memberid;
                }else {
                    queryData['memberid'] = -1;
                }

                Membermanagement.table.refresh({query: queryData});
            }})
    }


};

$(function () {
    var defaultColunms = Membermanagement.initColumn();
    var table = new BSTable(Membermanagement.id, "/membermanagement/list", defaultColunms);
    //table.setPaginationType("client");
    table.setPaginationType("server");
    Membermanagement.table = table.init();
});