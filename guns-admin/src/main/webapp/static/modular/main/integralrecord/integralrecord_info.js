/**
 * 初始化新增积分详情对话框
 */
var IntegralrecordInfoDlg = {
    integralrecordInfoData : {},
    validateFields: {
        integral: {
            validators: {
                notEmpty: {
                    message: '新增积分不能为空'
                },
                numeric: {message: '新增积分只能输入数字'},
                greaterThan: {
                    value: 0.001,
                    message: "新增积分最小输入值为 0.001"
                }
            }
        }
    }
};

/**
 * 清除数据
 */
IntegralrecordInfoDlg.clearData = function() {
    this.integralrecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IntegralrecordInfoDlg.set = function(key, val) {
    this.integralrecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
IntegralrecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
IntegralrecordInfoDlg.close = function() {
    parent.layer.close(window.parent.Integralrecord.layerIndex);
}

/**
 * 收集数据
 */
IntegralrecordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('integral')
    .set('target')
    .set('type')
    .set('memberid')
    .set('createTime');
}


/**
 * 验证数据是否为空
 */
IntegralrecordInfoDlg.validate = function () {
    $('#integralrecordInfoTable').bootstrapValidator('validate');
    return $("#integralrecordInfoTable").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
IntegralrecordInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/integralrecord/add", function(data){
        Feng.success("添加成功!");
        $("#introducerName").val("");
        $("#name").val("");
        $("#memberid").val("");
        $("#tel").val("");
        $("#address").val("");
        $("#integralSum").val("");
        $("#countPrice").val("");
        $("#levelID").val("");
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.integralrecordInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
IntegralrecordInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/integralrecord/update", function(data){
        Feng.success("修改成功!");
        window.parent.Integralrecord.table.refresh();
        IntegralrecordInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.integralrecordInfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("integralrecordInfoTable", IntegralrecordInfoDlg.validateFields);
});
