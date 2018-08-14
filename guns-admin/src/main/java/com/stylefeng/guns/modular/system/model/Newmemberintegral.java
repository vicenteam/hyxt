package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员积分新增与消费表
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-13
 */
@TableName("main_newmemberintegral")
public class Newmemberintegral extends Model<Newmemberintegral> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String memberid;
    /**
     * 获得积分值
     */
    private Integer integral;
    /**
     * 积分类型(1.推荐新人,2.消费,3.兑换)
     */
    private Integer integraltype;
    private String createdt;
    private String updatedt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getIntegraltype() {
        return integraltype;
    }

    public void setIntegraltype(Integer integraltype) {
        this.integraltype = integraltype;
    }

    public String getCreatedt() {
        return createdt;
    }

    public void setCreatedt(String createdt) {
        this.createdt = createdt;
    }

    public String getUpdatedt() {
        return updatedt;
    }

    public void setUpdatedt(String updatedt) {
        this.updatedt = updatedt;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Newmemberintegral{" +
        "id=" + id +
        ", memberid=" + memberid +
        ", integral=" + integral +
        ", integraltype=" + integraltype +
        ", createdt=" + createdt +
        ", updatedt=" + updatedt +
        "}";
    }
}
