package com.stylefeng.guns.core.common.BaseEntityWrapper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.shiro.ShiroKit;

public class BaseEntityWrapper<T> extends EntityWrapper {
    Integer userOrgId = ShiroKit.getUser().getDeptId();

    public BaseEntityWrapper() {
        eq("orgId", userOrgId);
    }

    @Override
    public Wrapper eq(String column, Object params) {
        return super.eq(column, params);
    }
}
