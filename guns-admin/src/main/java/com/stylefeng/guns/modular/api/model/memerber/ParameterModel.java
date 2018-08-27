package com.stylefeng.guns.modular.api.model.memerber;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "标识model")
public class ParameterModel {

    @ApiModelProperty("卡片id")
    private String code;
    @ApiModelProperty("会员id")
    private String memberid;
    @ApiModelProperty("身份证id")
    private String cadid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getCadid() {
        return cadid;
    }

    public void setCadid(String cadid) {
        this.cadid = cadid;
    }
}
