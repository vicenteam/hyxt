package com.stylefeng.guns.modular.main.controller;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;

/**
 * 会员管理控制器
 *
 * @author fengshuonan
 * @Date 2018-08-10 16:00:02
 */
@Controller
@RequestMapping("/membermanagement")
public class MembermanagementController extends BaseController {

    private String PREFIX = "/main/membermanagement/";

    @Autowired
    private IMembermanagementService membermanagementService;

    /**
     * 跳转到会员管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "membermanagement.html";
    }

    /**
     * 跳转到添加会员管理
     */
    @RequestMapping("/membermanagement_add")
    public String membermanagementAdd() {
        return PREFIX + "membermanagement_add.html";
    }

    /**
     * 跳转到修改会员管理
     */
    @RequestMapping("/membermanagement_update/{membermanagementId}")
    public String membermanagementUpdate(@PathVariable Integer membermanagementId, Model model) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("item",membermanagement);
        LogObjectHolder.me().set(membermanagement);
        return PREFIX + "membermanagement_edit.html";
    }

    /**
     * 获取会员管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String name,String address,String fstatus,String sex,String idcard ,String phone ,String stafff,String deptid,String province,String city,String district) {
        Page<Membermanagement> page = new PageFactory<Membermanagement>().defaultPage();
        BaseEntityWrapper<Membermanagement> baseEntityWrapper = new BaseEntityWrapper<>();
        if(!StringUtils.isEmpty(name))baseEntityWrapper.eq("name",name);
        if(!StringUtils.isEmpty(address))baseEntityWrapper.like("address",address);
        if(!StringUtils.isEmpty(fstatus))baseEntityWrapper.eq("familyStatusID",fstatus);
        if(!StringUtils.isEmpty(sex))baseEntityWrapper.eq("sex",sex);
        if(!StringUtils.isEmpty(idcard))baseEntityWrapper.eq("idcard",idcard);
        if(!StringUtils.isEmpty(phone))baseEntityWrapper.like("phone",phone);
        if(!StringUtils.isEmpty(stafff))baseEntityWrapper.like("staffid",stafff);
        if(!StringUtils.isEmpty(deptid))baseEntityWrapper.eq("deptid",deptid);
        if(!StringUtils.isEmpty(province))baseEntityWrapper.eq("province",province);
        if(!StringUtils.isEmpty(city))baseEntityWrapper.eq("city",city);
        if(!StringUtils.isEmpty(district))baseEntityWrapper.eq("district",district);
        Page<Membermanagement> result = membermanagementService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增会员管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Membermanagement membermanagement) {
        membermanagementService.insert(membermanagement);
        return SUCCESS_TIP;
    }

    /**
     * 删除会员管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer membermanagementId) {
        membermanagementService.deleteById(membermanagementId);
        return SUCCESS_TIP;
    }

    /**
     * 修改会员管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Membermanagement membermanagement) {
        membermanagementService.updateById(membermanagement);
        return SUCCESS_TIP;
    }

    /**
     * 会员管理详情
     */
    @RequestMapping(value = "/detail/{membermanagementId}")
    @ResponseBody
    public Object detail(@PathVariable("membermanagementId") Integer membermanagementId) {
        return membermanagementService.selectById(membermanagementId);
    }
}
