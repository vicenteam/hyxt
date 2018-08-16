package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.IClearService;
import com.stylefeng.guns.modular.main.service.IIntegralrecordService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.Clear;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.model.Integralrecord;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 积分清零控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
@RequestMapping("/integralgift")
public class IntegralGiftController extends BaseController {

    private String PREFIX = "/main/integralrecord/";

    @Autowired
    private IDeptService deptService;
    @Autowired
    private IIntegralrecordService integralrecordService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private MembermanagementController membermanagementController;

    @RequestMapping("")
    public String index(Model model){
        Dept dept = deptService.selectById(ShiroKit.getUser().getDeptId());
        model.addAttribute("dept",dept);
        return PREFIX + "integralGift.html";
    }

    /**
     * 添加赠送积分
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object add(double integral) {
        System.out.println(integral);
        BaseEntityWrapper<Membermanagement> wrapper = new BaseEntityWrapper<>();
        List<Membermanagement> ms = membermanagementService.selectList(wrapper);
        Membermanagement m = new Membermanagement();
        Integralrecord integralrecord = new Integralrecord();
        for (Membermanagement membermanagement : ms){
            m.setId(membermanagement.getId());
            m.setCountPrice(membermanagement.getCountPrice()+integral);
            m.setIntegral(membermanagement.getIntegral()+integral);
            //会员积分赠送保存
            membermanagementService.updateById(m);

            integralrecord.setIntegral(integral);
            integralrecord.setMemberid(membermanagement.getId());
            integralrecord.setType(2);
            integralrecord.setCreateTime(DateUtil.getTime());
            integralrecord.setDeptid(ShiroKit.getUser().getDeptId());
            integralrecord.setStaffid(ShiroKit.getUser().getId());
            //赠送积分记录添加
            integralrecordService.insert(integralrecord);
            //如果积分上限更新会员等级
            membermanagementController.updateMemberLeave(membermanagement.getId()+"");
        }
        return SUCCESS_TIP;
    }
}
