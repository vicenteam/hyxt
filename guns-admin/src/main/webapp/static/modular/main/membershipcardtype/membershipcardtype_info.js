/**
 * 初始化会员配置详情对话框
 */
var MembershipcardtypeInfoDlg = {
    membershipcardtypeInfoData : {}
};

/**
 * 清除数据
 */
MembershipcardtypeInfoDlg.clearData = function() {
    this.membershipcardtypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MembershipcardtypeInfoDlg.set = function(key, val) {
    this.membershipcardtypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MembershipcardtypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MembershipcardtypeInfoDlg.close = function() {
    parent.layer.close(window.parent.Membershipcardtype.layerIndex);
}

/**
 * 收集数据
 */
MembershipcardtypeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('cardname')
    .set('signin')
    .set('shopping')
    .set('newpoints')
    .set('signinnew')
    .set('shoppingnew')
    .set('upamount')
    .set('deptid')
    .set('tips')
    .set('status')
    .set('createdt')
    .set('updatedt');
}

/**
 * 提交添加
 */
MembershipcardtypeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/membershipcardtype/add", function(data){
        Feng.success("添加成功!");
        window.parent.Membershipcardtype.table.refresh();
        MembershipcardtypeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.membershipcardtypeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MembershipcardtypeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/membershipcardtype/update", function(data){
        Feng.success("修改成功!");
        window.parent.Membershipcardtype.table.refresh();
        MembershipcardtypeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.membershipcardtypeInfoData);
    ajax.start();
}

$(function() {

});
