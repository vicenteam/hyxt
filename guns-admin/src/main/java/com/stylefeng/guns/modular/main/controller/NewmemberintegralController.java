package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Newmemberintegral;
import com.stylefeng.guns.modular.main.service.INewmemberintegralService;

/**
 * 新增会员积分控制器
 *
 * @author fengshuonan
 * @Date 2018-08-13 16:13:23
 */
@Controller
@RequestMapping("/newmemberintegral")
public class NewmemberintegralController extends BaseController {

    private String PREFIX = "/main/newmemberintegral/";

    @Autowired
    private INewmemberintegralService newmemberintegralService;

    /**
     * 跳转到新增会员积分首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "newmemberintegral.html";
    }

    /**
     * 跳转到添加新增会员积分
     */
    @RequestMapping("/newmemberintegral_add")
    public String newmemberintegralAdd() {
        return PREFIX + "newmemberintegral_add.html";
    }

    /**
     * 跳转到修改新增会员积分
     */
    @RequestMapping("/newmemberintegral_update/{newmemberintegralId}")
    public String newmemberintegralUpdate(@PathVariable Integer newmemberintegralId, Model model) {
        Newmemberintegral newmemberintegral = newmemberintegralService.selectById(newmemberintegralId);
        model.addAttribute("item",newmemberintegral);
        LogObjectHolder.me().set(newmemberintegral);
        return PREFIX + "newmemberintegral_edit.html";
    }

    /**
     * 获取新增会员积分列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Newmemberintegral> page = new PageFactory<Newmemberintegral>().defaultPage();
        BaseEntityWrapper<Newmemberintegral> baseEntityWrapper = new BaseEntityWrapper<>();
        Page<Newmemberintegral> result = newmemberintegralService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增新增会员积分
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Newmemberintegral newmemberintegral) {
        newmemberintegralService.insert(newmemberintegral);
        return SUCCESS_TIP;
    }

    /**
     * 删除新增会员积分
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer newmemberintegralId) {
        newmemberintegralService.deleteById(newmemberintegralId);
        return SUCCESS_TIP;
    }

    /**
     * 修改新增会员积分
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Newmemberintegral newmemberintegral) {
        newmemberintegralService.updateById(newmemberintegral);
        return SUCCESS_TIP;
    }

    /**
     * 新增会员积分详情
     */
    @RequestMapping(value = "/detail/{newmemberintegralId}")
    @ResponseBody
    public Object detail(@PathVariable("newmemberintegralId") Integer newmemberintegralId) {
        return newmemberintegralService.selectById(newmemberintegralId);
    }
}
