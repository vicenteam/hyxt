package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Integralrecord;
import com.stylefeng.guns.modular.main.service.IIntegralrecordService;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增积分控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
@RequestMapping("/integralrecord")
public class IntegralrecordController extends BaseController {

    private String PREFIX = "/main/integralrecord/";

    @Autowired
    private IIntegralrecordService integralrecordService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private MembermanagementController membermanagementController;

    /**
     * 跳转到新增积分首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "integralrecord.html";
    }

    /**
     * 跳转到添加新增积分
     */
    @RequestMapping("/integralrecord_add")
    public String integralrecordAdd() {
        return PREFIX + "integralrecord_add.html";
    }

    /**
     * 跳转到修改新增积分
     */
    @RequestMapping("/integralrecord_update/{integralrecordId}")
    public String integralrecordUpdate(@PathVariable Integer integralrecordId, Model model) {
        Integralrecord integralrecord = integralrecordService.selectById(integralrecordId);
        model.addAttribute("item",integralrecord);
        LogObjectHolder.me().set(integralrecord);
        return PREFIX + "integralrecord_edit.html";
    }

    /**
     * 获取新增积分列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Integralrecord> page = new PageFactory<Integralrecord>().defaultPage();
        BaseEntityWrapper<Integralrecord> baseEntityWrapper = new BaseEntityWrapper<>();
        Page<Integralrecord> result = integralrecordService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增新增积分
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object add(Integralrecord integralrecord) {
        List<Membermanagement> membermanagements = new ArrayList<>();
        Membermanagement membermanagement = new Membermanagement();
        membermanagement.setId(integralrecord.getMemberid());
        membermanagements.add(membermanagement);
        //积分添加操作
        insertIntegral(integralrecord.getIntegral(),0,membermanagements);
        return SUCCESS_TIP;
    }

    /**
     * 会员积分增加并新增记录
     * @param integral
     * @param type
     * @param mList
     */
    public void insertIntegral(double integral, Integer type, List<Membermanagement> mList){
        Membermanagement membermanagement = new Membermanagement();
        Integralrecord integralrecord = new Integralrecord();
        double nowIntegral = 0;
        double nowCountPrice = 0;
        for(Membermanagement memberId : mList){  //循环当前门店会员列表为
//            System.out.println(" -------****------ "+memberId.getIntegral());
            if(memberId.getIntegral() != null && memberId.getCountPrice() != null){
                nowIntegral = memberId.getIntegral();
                nowCountPrice = memberId.getCountPrice();
            }else {
                Membermanagement mInfo = membermanagementService.selectById(memberId.getId());
                nowIntegral = mInfo.getIntegral();
                nowCountPrice = mInfo.getCountPrice();
            }
            membermanagement.setId(memberId.getId());
            if(type != 5){ // 判断是否为兑换
                membermanagement.setIntegral(nowIntegral+integral); //可用积分数 + 新增积分数
                membermanagement.setCountPrice(nowCountPrice+integral); //总积分数 + 新增积分数
            }else {  //扣除积分
                membermanagement.setIntegral(nowIntegral-integral);//可用积分数 - 兑换积分数
            }
            //更新会员总积分和实际积分
            membermanagementService.updateById(membermanagement);
            membermanagementController.updateMemberLeave(memberId.getId()+"");
            //添加积分记录
            integralrecord.setIntegral(integral);
            integralrecord.setType(type);
            integralrecord.setCreateTime(DateUtil.getTime());
            integralrecord.setMemberid(memberId.getId());
            integralrecord.setDeptid(ShiroKit.getUser().getDeptId());
            integralrecord.setStaffid(ShiroKit.getUser().getId());
            integralrecordService.insert(integralrecord);
        }

    }

    /**
     * 删除新增积分
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer integralrecordId) {
        integralrecordService.deleteById(integralrecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改新增积分
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Integralrecord integralrecord) {
        integralrecordService.updateById(integralrecord);
        return SUCCESS_TIP;
    }

    /**
     * 新增积分详情
     */
    @RequestMapping(value = "/detail/{integralrecordId}")
    @ResponseBody
    public Object detail(@PathVariable("integralrecordId") Integer integralrecordId) {
        return integralrecordService.selectById(integralrecordId);
    }
}
