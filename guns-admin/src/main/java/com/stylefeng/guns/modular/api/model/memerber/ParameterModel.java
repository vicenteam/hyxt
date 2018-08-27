package com.stylefeng.guns.modular.api.model.memerber;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "标识model")
public class ParameterModel {

    @ApiModelProperty("卡片id")
    private Integer code;
    @ApiModelProperty("会员id")
    private Integer memberid;
    @ApiModelProperty("身份证id")
    private Integer cadid;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public Integer getCadid() {
        return cadid;
    }

    public void setCadid(Integer cadid) {
        this.cadid = cadid;
    }
}
