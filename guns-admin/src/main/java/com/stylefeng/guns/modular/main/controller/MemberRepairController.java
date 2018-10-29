package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.api.base.BaseController;
import com.stylefeng.guns.modular.main.service.ICheckinService;
import com.stylefeng.guns.modular.main.service.IQiandaoCheckinService;
import com.stylefeng.guns.modular.system.model.Checkin;
import com.stylefeng.guns.modular.system.model.QiandaoCheckin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/memberRepair")
public class MemberRepairController extends BaseController {
    private String PREFIX = "/main/membermanagement/";

    @Autowired
    private ICheckinService checkinService;
    @Autowired
    private IQiandaoCheckinService qiandaoCheckinService;

    @RequestMapping("")
    public String index(){
        return PREFIX + "memberRepair.html";
    }

    @RequestMapping(value = "/repair")
    @ResponseBody
    public Object repair(String b1,String b2,String memberId,String screenings){
        String message="";
        BaseEntityWrapper<Checkin> cWrapper = new BaseEntityWrapper<>();
        cWrapper.eq("screenings",screenings);
        Checkin checkin = checkinService.selectOne(cWrapper); //获取签到场次
        if(checkin != null){
            BaseEntityWrapper<QiandaoCheckin> qWrapper = new BaseEntityWrapper<>();
            qWrapper.eq("memberid",memberId);
            qWrapper.eq("checkinid",checkin.getId());
            QiandaoCheckin qian = qiandaoCheckinService.selectOne(qWrapper);
            if(qian != null){
                if(b1 != null && ! b1.equals("")){ //
                    if(qian.getCreatetime() == null && qian.getCreatetime().equals("")){
                        qian.setCreatetime(DateUtil.getTime());
                        qiandaoCheckinService.updateById(qian);
                        message = "首签成功";
                    }else {
                        message = "已经首签";
                    }
                }else if(b2 != null && b2.equals("")){
                    if(qian.getCreatetime() != null && ! qian.getCreatetime().equals("")){
                        qian.setUpdatetime(DateUtil.getTime());
                        qiandaoCheckinService.updateById(qian);
                        message = "复签成功";
                    }else {
                        message="请先首签";
                    }
                }
            }else {
                if(b1 != null && ! b1.equals("")){
                    QiandaoCheckin qian2 = new QiandaoCheckin();
                    qian2.setCreatetime(DateUtil.getTime());
                    qian2.setMemberid(Integer.parseInt(memberId));
                    qian2.setCheckinid(checkin.getId());
                    qian2.setDeptid(ShiroKit.getUser().getDeptId());
                    qian2.setStatus(0);
                    qiandaoCheckinService.insert(qian2);
                    message = "首签成功";
                }else {
                    message = "请先首签";
                }
            }
        }else {
            message = "输入签到场次不存在";
        }
        return message;
    }
}
