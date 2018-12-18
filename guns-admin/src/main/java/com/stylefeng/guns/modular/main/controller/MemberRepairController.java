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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    /**
     *  会员补签
     * @param memberId
     * @param time
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/repair")
    @ResponseBody
    public Object repair(String memberId,String time) throws Exception {
        String[] newTime = time.split(" ~ ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(newTime[0]);
        Date dEnd = sdf.parse(newTime[1]);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dBegin);
        List<String> times = new ArrayList<>();
        while (cal.getTime().compareTo(dEnd) <= 0){ //获取时间范围所有日期
            System.out.println(cal.getTime().compareTo(dEnd));
            times.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        if(StringUtils.isEmpty(memberId)) throw new Exception("请读卡获取会员");
        for (String t : times) {
            BaseEntityWrapper<QiandaoCheckin> qWrapper = new BaseEntityWrapper<>();
            qWrapper.eq("memberid",memberId);
            qWrapper.like("createtime",t);
            QiandaoCheckin qian = qiandaoCheckinService.selectOne(qWrapper);
            if(qian != null){
                if(StringUtils.isEmpty(qian.getUpdatetime())){ //已首签未复签
                    qian.setUpdatetime(qian.getCreatetime()); //如果已经首签 补签复签的时间就为首签时间
                    qiandaoCheckinService.updateById(qian); //进行补签
                }
            }else {
                String[] val = t.split("-");
                StringBuffer defaultVal = new StringBuffer();
                for (int i=0; i<val.length; i++){
                    defaultVal.append(val[i]);
                    if(i == val.length-1){
                        defaultVal.append("01"); //拼接日期字符串 格式为yyyyMMdd01 默认某天第一场次
                    }
                }
                BaseEntityWrapper<Checkin> cWrapper = new BaseEntityWrapper<>();
                cWrapper.eq("screenings",Integer.parseInt(defaultVal.toString()));
                Checkin checkin = checkinService.selectOne(cWrapper); //获取当前日期第一场次
                if(checkin != null){ //如果有当前场次的记录
                    QiandaoCheckin qiandaoCheckin = new QiandaoCheckin();
                    qiandaoCheckin.setCreatetime(t+" "+sdf2.format(new Date())); //年月日 时分秒
                    qiandaoCheckin.setUpdatetime(t+" "+sdf2.format(new Date()));
                    qiandaoCheckin.setStatus(0);
                    qiandaoCheckin.setCheckinid(checkin.getId());
                    qiandaoCheckin.setDeptid(ShiroKit.getUser().getDeptId());
                    qiandaoCheckin.setMemberid(Integer.parseInt(memberId));
                    qiandaoCheckinService.insert(qiandaoCheckin);
                }else {
                    throw new Exception("当天无签到场次，日期为："+t);
                }
            }
        }
        return SUCCESS_TIP;
    }
}
