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
        Page<Map<String, Object>> result = integralrecordService.selectMapsPage(page, entityWrapper);
        for(int i=0; i<result.getRecords().size(); i++){
            Membermanagement m = membermanagementService.selectById(result.getRecords().get(i).get("memberid").toString());
            result.getRecords().get(i).put("memberName",m.getName());
            result.getRecords().get(i).put("membercadid",m.getCadID());
        }
        System.out.println(result.getRecords());
        return super.packForBT(result);
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public Object shouwMemberInfo(){
        return null;
    }

}
