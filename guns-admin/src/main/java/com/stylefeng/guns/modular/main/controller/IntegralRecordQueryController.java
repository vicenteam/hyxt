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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String index(){
        return PREFIX + "integralRecordQuery.html";
    }

    /**
     * 查询积分记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Integralrecord> page = new PageFactory<Integralrecord>().defaultPage();
        EntityWrapper<Integralrecord> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderBy("createTime",false);
        Page<Map<String, Object>> result = integralrecordService.selectMapsPage(page, entityWrapper);
        for(Map<String, Object> map : result.getRecords()){
            if(map.get("memberid") != null){
                Membermanagement membermanagement = membermanagementService.selectById(map.get("memberid").toString());
                map.put("memberName",membermanagement.getName());
                map.put("membercadid",membermanagement.getCadID());
            }
            if(map.get("staffid") != null){
                map.put("staffName",userService.selectById(map.get("staffid").toString()).getName());
            }
        }
        return super.packForBT(result);
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public Object shouwMemberInfo(){
        return null;
    }

}
