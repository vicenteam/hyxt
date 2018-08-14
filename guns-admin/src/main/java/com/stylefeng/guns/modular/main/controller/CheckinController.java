package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 签到场次控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 10:12:47
 */
@Controller
@RequestMapping("/checkin")
public class CheckinController extends BaseController {

    private String PREFIX = "/main/checkin/";

    @Autowired
    private ICheckinService checkinService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IQiandaoCheckinService qiandaoCheckinService;
    @Autowired
    private IDeptService deptService;

    /**
     * 跳转到签到场次首页
     */
    @RequestMapping("")
    public String index( Model model) {
        List list = membershipcardtypeService.selectList(new BaseEntityWrapper<>());
        model.addAttribute("leave",list);
        List list1 = userService.selectList(new BaseEntityWrapper<>());
        model.addAttribute("staffs",list1);
        Dept dept = deptService.selectById(ShiroKit.getUser().getDeptId());
        model.addAttribute("dept",dept);
        return PREFIX + "checkin.html";
    }

    /**
     * 跳转到添加签到场次
     */
    @RequestMapping("/checkin_add")
    public String checkinAdd() {
        return PREFIX + "checkin_add.html";
    }

    /**
     * 跳转到修改签到场次
     */
    @RequestMapping("/checkin_update/{checkinId}")
    public String checkinUpdate(@PathVariable Integer checkinId, Model model) {
        Checkin checkin = checkinService.selectById(checkinId);
        model.addAttribute("item",checkin);
        LogObjectHolder.me().set(checkin);
        return PREFIX + "checkin_edit.html";
    }

    /**
     * 获取签到场次列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Checkin> page = new PageFactory<Checkin>().defaultPage();
        BaseEntityWrapper<Checkin> baseEntityWrapper = new BaseEntityWrapper<>();
        Page<Checkin> result = checkinService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增签到场次
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Checkin checkin) {
        checkinService.insert(checkin);
        return SUCCESS_TIP;
    }

    /**
     * 删除签到场次
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer checkinId) {
        checkinService.deleteById(checkinId);
        return SUCCESS_TIP;
    }

    /**
     * 修改签到场次
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Checkin checkin) {
        checkinService.updateById(checkin);
        return SUCCESS_TIP;
    }

    /**
     * 签到场次详情
     */
    @RequestMapping(value = "/detail/{checkinId}")
    @ResponseBody
    public Object detail(@PathVariable("checkinId") Integer checkinId) {
        return checkinService.selectById(checkinId);
    }

    /**
     * 获取签到场次信息
     * @return
     */
    @RequestMapping(value = "/getcheck")
    @ResponseBody
    public Object getcheck() {
        BaseEntityWrapper<Checkin> checkinBaseEntityWrapper = new BaseEntityWrapper<>();
        checkinBaseEntityWrapper.ge("startDate",DateUtil.formatDate(new Date(),"yyyy-MM-dd")+" 00:00:00");
        List<Checkin> list = checkinService.selectList(checkinBaseEntityWrapper);
        if(list.size()==0){
            Checkin checkin=new Checkin();
            checkin.setCreateDate(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            checkin.setStartDate(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            checkin.setDeptId(ShiroKit.getUser().getDeptId()+"");
            checkin.setStatus(1);
            String s = DateUtil.formatDate(new Date(), "yyyyMMdd")+"01";
            checkin.setScreenings(Integer.parseInt(s));
            checkinService.insert(checkin);
            return checkin;
        }else {
            Checkin checkinold = list.get(list.size() - 1);
            Checkin checkin=new Checkin();
            checkin.setCreateDate(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            checkin.setStartDate(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            checkin.setDeptId(ShiroKit.getUser().getDeptId()+"");
            checkin.setStatus(1);
            int v=(checkinold.getScreenings()+1);
            checkin.setScreenings(v);
            checkinService.insert(checkin);
            return checkin;
        }
    }
    @RequestMapping(value = "/updatecheck")
    @ResponseBody
    public Object updatecheck(String checkid) {
        Checkin checkin = checkinService.selectById(checkid);
        checkin.setStatus(2);
        checkin.setEndDate(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        checkinService.updateById(checkin);
        return SUCCESS_TIP;
    }
    @RequestMapping(value = "/getUserInfo")
    @ResponseBody
    public Object getUserInfo(String value,String checkid) {
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
        memberCardBaseEntityWrapper.eq("code",value);
        MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
        if(memberCard!=null&&memberCard.getMemberid()!=null){
            BaseEntityWrapper<Membermanagement> membermanagementBaseEntityWrapper = new BaseEntityWrapper<>();
            membermanagementBaseEntityWrapper.eq("id",memberCard.getMemberid());
            Map<String,Object> map = membermanagementService.selectMap(membermanagementBaseEntityWrapper);
            //获取当前签到场次签到信息
            BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
            qiandaoCheckinBaseEntityWrapper.eq("memberid",memberCard.getMemberid());
            qiandaoCheckinBaseEntityWrapper.eq("checkinid",checkid);
            QiandaoCheckin qiandaoCheckin = qiandaoCheckinService.selectOne(qiandaoCheckinBaseEntityWrapper);
            if(qiandaoCheckin==null){
                    //进行首签
                map.put("qiandao",0);
            }else {
               if(StringUtils.isEmpty( qiandaoCheckin.getUpdatetime())){
                   //进行复签
                   map.put("qiandao",1);
               }else {
                   //不能进行操作
                   map.put("qiandao",2);
               }
            }
            return map;
        }
        return null;
    }
}
