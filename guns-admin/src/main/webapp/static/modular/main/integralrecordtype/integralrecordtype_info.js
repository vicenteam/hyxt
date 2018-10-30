/**
 * 初始化积分类型详情对话框
 */
var IntegralrecordtypeInfoDlg = {
    integralrecordtypeInfoData : {}
};

/**
 * 清除数据
 */
IntegralrecordtypeInfoDlg.clearData = function() {
    this.integralrecordtypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IntegralrecordtypeInfoDlg.set = function(key, val) {
    this.integralrecordtypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IntegralrecordtypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
IntegralrecordtypeInfoDlg.close = function() {
    parent.layer.close(window.parent.Integralrecordtype.layerIndex);
}

/**
 * 收集数据
 */
IntegralrecordtypeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name');
}

/**
 * 提交添加
 */
IntegralrecordtypeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/integralrecordtype/add", function(data){
        Feng.success("添加成功!");
        window.parent.Integralrecordtype.table.refresh();
        IntegralrecordtypeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.integralrecordtypeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
IntegralrecordtypeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/integralrecordtype/update", function(data){
        Feng.success("修改成功!");
        window.parent.Integralrecordtype.table.refresh();
        IntegralrecordtypeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.integralrecordtypeInfoData);
    ajax.start();
}

$(function() {

});
