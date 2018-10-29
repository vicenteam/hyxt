/**
 * 初始化新增积分详情对话框
 */
var MemberRepair = {
    memberRepair : {}
    ,validateFields: {
        screenings: {
            validators: {
                notEmpty: {
                    message: '签到场次不能为空'
                },
                numeric: {message: '签到场次只是数字'}
            }
        }
    }
};

/**
 * 清除数据
 */
MemberRepair.clearData = function() {
    this.memberRepair = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberRepair.set = function(key, val) {
    this.memberRepair[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberRepair.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberRepair.close = function() {
    parent.layer.close(window.parent.Integralrecord.layerIndex);
}

/**
 * 收集数据
 */
MemberRepair.collectData = function() {
    this
        .set('screenings');
}


/**
 * 验证数据是否为空
 */
MemberRepair.validate = function () {
    $('#memberRepairInfoTable').bootstrapValidator('validate');
    return $("#memberRepairInfoTable").data('bootstrapValidator').isValid();
};

/**
 * 提交补首签
 */
MemberRepair.addSubmit1 = function() {
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    var datas = {};
    datas['memberId'] = $("#introducerId").val();
    datas['screenings']= $("#screenings").val();
    datas['b1'] = 1;
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberRepair/repair", function(data){
        Feng.success(data.responseJSON.message);
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(datas);
    ajax.start();
}

/**
 * 提交补复签
 */
MemberRepair.addSubmit2 = function() {
    var datas = {};
    datas['memberId'] = $("#introducerId").val();
    datas['screenings']= $("#screenings").val();
    datas['b2'] = 2;
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberRepair/repair", function(data){
        Feng.success(data.responseJSON.message);
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(datas);
    ajax.start();
}

$(function() {
    Feng.initValidator("memberRepairInfoTable", MemberRepair.validateFields);
});
