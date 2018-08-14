package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Membershipcardtype;
import com.stylefeng.guns.modular.main.service.IMembershipcardtypeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员配置控制器
 *
 * @author fengshuonan
 * @Date 2018-08-13 14:11:25
 */
@Controller
@RequestMapping("/membershipcardtype")
public class MembershipcardtypeController extends BaseController {

    private String PREFIX = "/main/membershipcardtype/";

    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private IDeptService deptService;

    /**
     * 跳转到会员配置首页
     */
    @RequestMapping("")
    public String index(Model model) {
        Dept dept = deptService.selectById(ShiroKit.getUser().getDeptId());
        model.addAttribute("dept",dept);
        return PREFIX + "membershipcardtype.html";
    }

    /**
     * 跳转到添加会员配置
     */
    @RequestMapping("/membershipcardtype_add")
    public String membershipcardtypeAdd(Model model) {
        Dept dept = deptService.selectById(ShiroKit.getUser().getDeptId());
        model.addAttribute("dept",dept);
        return PREFIX + "membershipcardtype_add.html";
    }

    /**
     * 跳转到修改会员配置
     */
    @RequestMapping("/membershipcardtype_update/{membershipcardtypeId}")
    public String membershipcardtypeUpdate(@PathVariable Integer membershipcardtypeId, Model model) {
        Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(membershipcardtypeId);
        model.addAttribute("item",membershipcardtype);
        Dept dept = deptService.selectById(ShiroKit.getUser().getDeptId());
        model.addAttribute("dept",dept);
        LogObjectHolder.me().set(membershipcardtype);
        return PREFIX + "membershipcardtype_edit.html";
    }

    /**
     * 验证会员等级名称
     */
//    @RequestMapping("/validateCardName")
//    @ResponseBody
//    public Map<String, Boolean> validateCardName(String cardname) {
//        EntityWrapper<Membershipcardtype> wrapper = new EntityWrapper<>();
//        wrapper.like("cardname",cardname);
//        wrapper.eq("deptid",ShiroKit.getUser().getDeptId());
//        Integer num = membershipcardtypeService.selectCount(wrapper);
//        boolean result = true;
//        if(num >= 1) result = false;
//        Map<String, Boolean> map = new HashMap<>();
//        map.put("valid", result);
//        return map;
//    }

    /**
     * 获取会员配置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, String cardname, Integer deptid) {
        EntityWrapper<Membershipcardtype> wrapper = new EntityWrapper<>();
        wrapper.like("cardname",cardname);
        wrapper.eq("deptid",deptid);
        wrapper.eq("status",0);
        Page<Membershipcardtype> page = new PageFactory<Membershipcardtype>().defaultPage();
        page = membershipcardtypeService.selectPage(page, wrapper);
        System.out.println(" +++++++ "+page.getRecords());
        return super.packForBT(page);
    }

    /**
     * 新增会员配置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Membershipcardtype membershipcardtype) {
        membershipcardtype.setCreatedt(DateUtil.getTime());
        membershipcardtypeService.insert(membershipcardtype);
        return SUCCESS_TIP;
    }

    /**
     * 删除会员配置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer membershipcardtypeId) {
        membershipcardtypeService.deleteById(membershipcardtypeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改会员配置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Membershipcardtype membershipcardtype) {
        membershipcardtype.setUpdatedt(DateUtil.getTime());
        membershipcardtypeService.updateById(membershipcardtype);
        return SUCCESS_TIP;
    }

    /**
     * 会员配置详情
     */
    @RequestMapping(value = "/detail/{membershipcardtypeId}")
    @ResponseBody
    public Object detail(@PathVariable("membershipcardtypeId") Integer membershipcardtypeId) {
        return membershipcardtypeService.selectById(membershipcardtypeId);
    }
}