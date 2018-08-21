package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.main.service.IMembershipcardtypeService;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 积分排名图表controller
 */
@Controller
@RequestMapping("/barranking")
public class BarRankingController extends BaseController {

    private String PREFIX = "/main/bar/";

    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;

    @RequestMapping("")
    public String index() {
        return PREFIX + "barranking.html";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(){
        Page<Membermanagement> page = new PageFactory<Membermanagement>().defaultPage();
        BaseEntityWrapper<Membermanagement> mWrapper = new BaseEntityWrapper<>();
        mWrapper.orderBy("integral",false);
        List<Membermanagement> mList =new ArrayList<>();// membermanagementService.selectList(mWrapper);
        Page<Map<String,Object>> pagemap=membermanagementService.selectMapsPage(page,mWrapper);
       List< Map<String,Object>> mapList=pagemap.getRecords();
        mapList.forEach(a->{
            String levelID=(String)a.get("levelID");
            if(!StringUtils.isEmpty(levelID)){
                a.put("levelName",membershipcardtypeService.selectById(levelID).getCardname());
            }
        });

        return mapList;
    }
}
