package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.main.service.IIntegralrecordService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.Integralrecord;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 积分记录查询控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
@RequestMapping("/integralrecordquery")
public class IntegralRecordQueryController extends BaseController {

    private String PREFIX = "/main/integralrecord/";

    @Autowired
    private IIntegralrecordService integralrecordService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IUserService userService;

    @RequestMapping("")
    public String index(Model model){
        BaseEntityWrapper<User> wrapper = new BaseEntityWrapper<>();
        model.addAttribute("users",userService.selectList(wrapper));
        return PREFIX + "integralRecordQuery.html";
    }

    /**
     * 查询积分记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, String operator, String memberName, String cadId
                        , String integralType, String begindate, String enddate) {
//        System.out.println(" ------*****------ "+operator+": "+memberName+": "+cadId+": "+integralType+": "+begindate+": "+enddate);
        BaseEntityWrapper<Membermanagement> mWrapper = new BaseEntityWrapper<>();
        if(! cadId.equals("")) mWrapper.eq("cadID",cadId);
        if(! memberName.equals("")) mWrapper.like("name",memberName);
        //会员 memberName、cadId 条件筛选
        List<Membermanagement> membermanagements = membermanagementService.selectList(mWrapper);
        Integer[] mIdArray = new Integer[membermanagements.size()];
        for(int i=0; i<mIdArray.length; i++){
            mIdArray[i] = membermanagements.get(i).getId();
        }
        //操作人 operator 条件筛选
        BaseEntityWrapper<User> uWrapper = new BaseEntityWrapper<>();
        if(! operator.equals("-1")) uWrapper.eq("id",operator);
        List<User> users = userService.selectList(uWrapper);
        Integer[] uIdArray = new Integer[users.size()];
        for(int i=0; i<uIdArray.length; i++){
            uIdArray[i] = users.get(i).getId();
        }
        //把 membermanagement 与 user 条件放入 积分记录表实现条件分页查询
        Page<Integralrecord> page = new PageFactory<Integralrecord>().defaultPage();
        BaseEntityWrapper<Integralrecord> iWrapper = new BaseEntityWrapper<>();
        if(! integralType.equals("-1")) iWrapper.like("type",integralType);
        iWrapper.in("memberid",mIdArray);
        iWrapper.in("staffid",uIdArray);
        if(! begindate.equals("") && ! enddate.equals("")){
            iWrapper.between("createTime",begindate,enddate);
        }else if(! begindate.equals("")){
            iWrapper.between("createTime",begindate,"9999-08-16 11:03:35");
        }else if (! enddate.equals("")){
            iWrapper.between("createTime","0001-08-16 11:03:35",enddate);
        }
        iWrapper.orderBy("createTime",false);
        Page<Map<String, Object>> serverPage = integralrecordService.selectMapsPage(page, iWrapper);
        for(Map<String, Object> map : serverPage.getRecords()){
            if(map.get("memberid") != null){
                Membermanagement membermanagement = membermanagementService.selectById(map.get("memberid").toString());
                map.put("memberName",membermanagement.getName());
                map.put("membercadid",membermanagement.getCadID());
            }
            if(map.get("staffid") != null){
                map.put("staffName",userService.selectById(map.get("staffid").toString()).getName());
            }
        }
        return super.packForBT(serverPage);
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public Object shouwMemberInfo(){
        return null;
    }

}
