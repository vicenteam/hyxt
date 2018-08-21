package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 积分排名图表controller
 */
@Controller
@RequestMapping("/barranking")
public class BarRankingController extends BaseController {

    private String PREFIX = "/main/bar/";

    @Autowired
    private IMembermanagementService membermanagementService;

    @RequestMapping("")
    public String index() {
        return PREFIX + "barranking.html";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(){
        BaseEntityWrapper<Membermanagement> mWrapper = new BaseEntityWrapper<>();
        mWrapper.orderBy("integral",false);
        List<Membermanagement> mList = membermanagementService.selectList(mWrapper);
        System.out.println(" ---- * -----"+mList);

        return mList;
    }
}
