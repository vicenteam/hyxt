package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 签到数据图表controller
 */
@Controller
@RequestMapping("/barbrush")
public class BarbrushController extends BaseController {
    private String PREFIX = "/main/bar/";

    /**
     * 跳转到签到数据图表
     * @return
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "barbrush.html";
    }
}
