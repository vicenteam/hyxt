package com.stylefeng.guns.modular.main.service;

import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 会员基础信息表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-10
 */
public interface IMembermanagementService extends IService<Membermanagement> {

    /**
     * 新增积分 更新会员总积分
     * @param memberId
     * @param integral
     * @return
     */
    public Integer updateByIntegral(Integer memberId, double integral);
}
