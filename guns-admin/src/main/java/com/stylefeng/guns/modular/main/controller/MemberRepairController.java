package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.main.service.ICheckinService;
import com.stylefeng.guns.modular.main.service.IQiandaoCheckinService;
import com.stylefeng.guns.modular.system.model.Checkin;
import com.stylefeng.guns.modular.system.model.QiandaoCheckin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public Object repair(String memberId,String time) throws Exception {
        String[] val1 = time.split(" ");
        String[] val2 = val1[0].split("-");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime(); //获取当前时间的前一天
        BaseEntityWrapper<QiandaoCheckin> qWrapper = new BaseEntityWrapper<>();
        qWrapper.eq("memberid",memberId);
        qWrapper.like("createtime",val1[0]);
        QiandaoCheckin qian = qiandaoCheckinService.selectOne(qWrapper);
        if(qian != null){
            if(StringUtils.isEmpty(qian.getCreatetime())){ //如果未进行首签
                qian.setCreatetime(sdf.format(date));
                qian.setUpdatetime(sdf.format(date));
            }else if(! StringUtils.isEmpty(qian.getCreatetime())){ //已首签未复签
                qian.setUpdatetime(qian.getCreatetime()); //如果已经首签 补签复签的时间就为首签时间 与代码行71-72对应
            }
            qiandaoCheckinService.updateById(qian); //进行补签
        }else {
            StringBuffer defaultVal = new StringBuffer();
            for (int i=0; i<val2.length; i++){
                defaultVal.append(val2[i]);
                if(i == val2.length-1){
                    defaultVal.append("01"); //拼接日期字符串 格式为yyyyMMdd01 默认某天第一场次
                }
            }
            BaseEntityWrapper<Checkin> cWrapper = new BaseEntityWrapper<>();
            cWrapper.eq("screenings",defaultVal);
            Checkin checkin = checkinService.selectOne(cWrapper); //查询签到场次
           try {
               qian.setCreatetime(sdf.format(date));
               qian.setUpdatetime(sdf.format(date));
               qian.setStatus(0);
               qian.setDeptid(ShiroKit.getUser().getDeptId());
               qian.setMemberid(Integer.parseInt(memberId));
               qiandaoCheckinService.insert(qian); //如果没有当前场次的记录
           }catch (Exception e){
               throw new GunsException(BizExceptionEnum.SERVER_ERROR1);
           }
        }
        return SUCCESS_TIP;
    }
}
