package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("barRankingExcel")
public class MemberExcel {

    @Excel(name = "会员名称",orderNum = "1")
    private String mName;
    @Excel(name = "性别",replace = { "男_1", "女_2" },orderNum = "2")
    private Integer mSex;
    @Excel(name = "联系方式",orderNum = "3")
    private String mPhone;
    @Excel(name = "当前积分",orderNum = "4")
    private double mIntegral;
    @Excel(name = "会员等级",orderNum = "5")
    private String mLevel;
    @Excel(name = "创建时间",orderNum = "6")
    private String mCreateDt;
    @Excel(name = "是否老年协会会员",orderNum = "7")
    private Integer mIsoldsociety;
    @Excel(name = "家庭地址",orderNum = "8")
    private String mAddress;
    @Excel(name = "总获得积分",orderNum = "9")
    private double mCountPrice;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Integer getmSex() {
        return mSex;
    }

    public void setmSex(Integer mSex) {
        this.mSex = mSex;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public double getmIntegral() {
        return mIntegral;
    }

    public void setmIntegral(double mIntegral) {
        this.mIntegral = mIntegral;
    }

    public String getmLevel() {
        return mLevel;
    }

    public void setmLevel(String mLevel) {
        this.mLevel = mLevel;
    }

    public String getmCreateDt() {
        return mCreateDt;
    }

    public void setmCreateDt(String mCreateDt) {
        this.mCreateDt = mCreateDt;
    }

    public Integer getmIsoldsociety() {
        return mIsoldsociety;
    }

    public void setmIsoldsociety(Integer mIsoldsociety) {
        this.mIsoldsociety = mIsoldsociety;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public double getmCountPrice() {
        return mCountPrice;
    }

    public void setmCountPrice(double mCountPrice) {
        this.mCountPrice = mCountPrice;
    }
}
