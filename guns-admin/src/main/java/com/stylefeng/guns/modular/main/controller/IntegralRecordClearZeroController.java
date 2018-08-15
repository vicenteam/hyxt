package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 积分清零控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
@RequestMapping("/integralrecordclearzero")
public class IntegralRecordClearZeroController extends BaseController {

    private String PREFIX = "/main/integralrecord/";

    @RequestMapping("")
    public String index(){
        return PREFIX + "integralRecordClearZero.html";
    }

    /**
     * 查询积分记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {

//        return super.packForBT(null);
        return null;
    }
}
