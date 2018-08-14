package com.stylefeng.guns.modular.main.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.IMemberCardService;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.model.MemberCard;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.Date;
import java.util.List;

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
    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDeptService deptService;

    /**
     * 跳转到会员管理首页
     */
    @RequestMapping("")
    public String index(Model model) {
        BaseEntityWrapper<User> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs",list);
        EntityWrapper<Dept> deptBaseEntityWrapper1 = new EntityWrapper<>();
        List depts = deptService.selectList(deptBaseEntityWrapper1);
        model.addAttribute("depts",depts);
        return PREFIX + "membermanagement.html";
    }

    /**
     * 跳转到添加会员管理
     */
    @RequestMapping("/membermanagement_add")
    public String membermanagementAdd(Model model) {
        BaseEntityWrapper<Dept> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs",list);
        return PREFIX + "membermanagement_add.html";
    }

    /**
     * 跳转到修改会员管理
     */
    @RequestMapping("/membermanagement_update/{membermanagementId}")
    public String membermanagementUpdate(@PathVariable Integer membermanagementId, Model model) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("item", membermanagement);
        BaseEntityWrapper<Dept> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs",list);
        LogObjectHolder.me().set(membermanagement);
        return PREFIX + "membermanagement_edit.html";
    }

    /**
     * 签到记录页面
     * @param membermanagementId
     * @param model
     * @return
     */
    @RequestMapping("/membermanagementcheckHistory/{membermanagementId}")
    public String membermanagementcheckHistory(@PathVariable Integer membermanagementId, Model model) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("memberId",membermanagementId);
        return PREFIX + "membermanagementcheckHistory.html";
    }
    /**
     * 获取会员管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String name, String address, String fstatus, String sex, String idcard, String phone, String stafff, String deptid, String province, String city, String district,String memberid) {
        Page<Membermanagement> page = new PageFactory<Membermanagement>().defaultPage();
        EntityWrapper<Membermanagement> baseEntityWrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(name)) baseEntityWrapper.eq("name", name);
        if (!StringUtils.isEmpty(address)) baseEntityWrapper.like("address", address);
        if (!StringUtils.isEmpty(fstatus)) baseEntityWrapper.eq("familyStatusID", fstatus);
        if (!StringUtils.isEmpty(sex)) baseEntityWrapper.eq("sex", sex);
        if (!StringUtils.isEmpty(idcard)) baseEntityWrapper.eq("idcard", idcard);
        if (!StringUtils.isEmpty(phone)) baseEntityWrapper.like("phone", phone);
        if (!StringUtils.isEmpty(stafff)) baseEntityWrapper.eq("staffid", stafff);
        if (!StringUtils.isEmpty(deptid)) baseEntityWrapper.eq("deptid", deptid);
        if (!StringUtils.isEmpty(province)) baseEntityWrapper.eq("province", province);
        if (!StringUtils.isEmpty(city)) baseEntityWrapper.eq("city", city);
        if (!StringUtils.isEmpty(district)) baseEntityWrapper.eq("district", district);
        if (!StringUtils.isEmpty(memberid)) baseEntityWrapper.eq("id", memberid);
        baseEntityWrapper.eq("state",0);
        Page<Membermanagement> result = membermanagementService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增会员管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object add(Membermanagement membermanagement,String cardCode) {
        membermanagement.setCreateTime(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        membermanagement.setDeptId(""+ShiroKit.getUser().getDeptId());
        membermanagementService.insert(membermanagement);
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
        memberCardBaseEntityWrapper.eq("code",cardCode);
        MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
        memberCard.setName(membermanagement.getName());
        memberCard.setMemberid(membermanagement.getId());
        memberCardService.updateById(memberCard);
        return SUCCESS_TIP;
    }

    /**
     * 删除会员管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer membermanagementId) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        membermanagement.setState(1);
        membermanagementService.updateById(membermanagement);
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

    @RequestMapping(value = "/getXieKaVal")
    @ResponseBody
    public Object getXieKaVal() {
        return getXieKaValInfo();
    }
    @RequestMapping(value = "/getUserInfo")
    @ResponseBody
    public Object getUserInfo(String value) {
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
        memberCardBaseEntityWrapper.eq("code",value);
        MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
       return memberCard;
    }
    public String getXieKaValInfo() {
        String chars = "ABCDEF1234567890";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt((int) (Math.random() * 16)));
        }
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
        memberCardBaseEntityWrapper.eq("code", sb.toString());
        int i = memberCardService.selectCount(memberCardBaseEntityWrapper);
        if (i != 0) {
            getXieKaValInfo();
        }
        MemberCard memberCard = new MemberCard();
        memberCard.setCode(sb.toString());
        memberCard.setCreatetime(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        memberCard.setDeptid(ShiroKit.getUser().getDeptId());
        memberCardService.insert(memberCard);
        return sb.toString();
    }

}
