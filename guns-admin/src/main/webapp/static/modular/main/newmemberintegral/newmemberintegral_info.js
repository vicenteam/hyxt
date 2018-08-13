/**
 * 初始化新增会员积分详情对话框
 */
var NewmemberintegralInfoDlg = {
    newmemberintegralInfoData : {}
};

/**
 * 清除数据
 */
NewmemberintegralInfoDlg.clearData = function() {
    this.newmemberintegralInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
NewmemberintegralInfoDlg.set = function(key, val) {
    this.newmemberintegralInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
NewmemberintegralInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
NewmemberintegralInfoDlg.close = function() {
    parent.layer.close(window.parent.Newmemberintegral.layerIndex);
}

/**
 * 收集数据
 */
NewmemberintegralInfoDlg.collectData = function() {
    this
    .set('id')
    .set('memberid')
    .set('integral')
    .set('integraltype')
    .set('createdt')
    .set('updatedt');
}

/**
 * 提交添加
 */
NewmemberintegralInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/newmemberintegral/add", function(data){
        Feng.success("添加成功!");
        window.parent.Newmemberintegral.table.refresh();
        NewmemberintegralInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.newmemberintegralInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
NewmemberintegralInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/newmemberintegral/update", function(data){
        Feng.success("修改成功!");
        window.parent.Newmemberintegral.table.refresh();
        NewmemberintegralInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.newmemberintegralInfoData);
    ajax.start();
}

$(function() {

});
