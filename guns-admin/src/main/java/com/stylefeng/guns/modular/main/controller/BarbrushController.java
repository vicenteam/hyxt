package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.modular.main.service.IQiandaoCheckinService;
import com.stylefeng.guns.modular.system.model.QiandaoCheckin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 签到数据图表controller
 */
@Controller
@RequestMapping("/barbrush")
public class BarbrushController extends BaseController {
    private String PREFIX = "/main/bar/";
    @Autowired
    private IQiandaoCheckinService qiandaoCheckinService;

    /**
     * 跳转到签到数据图表
     * @return
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "barbrush.html";
    }

    /**
     * 获取周签到统计数据
     * @param startdate
     * @param enddate
     * @return
     */
    @RequestMapping(value = "/getWeekdata")
    @ResponseBody
    public Object getWeekdata(String startdate,String enddate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Map<String,Object> result=new HashMap<>();
        int[] data1=new int[7];
        int[] data2=new int[7];
        int[] data3=new int[7];
        for(int i=0;i<7;i++){
            Date date = simpleDateFormat.parse(startdate);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, -1+i);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //获得比较前一天时间
            //获得比较时间
            Date date2 = simpleDateFormat.parse(startdate);
            Calendar calendar2 = new GregorianCalendar();
            calendar2.setTime(date2);
            calendar2.add(calendar2.DATE, i);
            date2 = calendar2.getTime();
            BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
            qiandaoCheckinBaseEntityWrapper.like("createtime",simpleDateFormat.format(date2));
            qiandaoCheckinBaseEntityWrapper.isNotNull("updatetime");

            BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper2 = new BaseEntityWrapper<>();
            qiandaoCheckinBaseEntityWrapper2.like("createtime",simpleDateFormat.format(date));
            qiandaoCheckinBaseEntityWrapper2.isNotNull("updatetime");
            int i1 = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper);//当天签到人数
            int i2 = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper2);//当天前一天签到人数
            data1[i]=i1;
            data2[i]=i2;
            data3[i]=(i1-i2);
        }
        result.put("data1",data1);
        result.put("data2",data2);
        result.put("data3",data3);
        return result;
    }
    /**
     * 获取月签到统计数据
     * @param startdate
     * @param enddate
     * @return
     */
    @RequestMapping(value = "/getMonthdata")
    @ResponseBody
    public Object getMonthdata(String startdate,Integer enddate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Map<String,Object> result=new HashMap<>();
        int[] data4=new int[enddate];
        int[] data5=new int[enddate];
        int[] data6=new int[enddate];
        for(int i=0;i<enddate;i++){
            Date date = simpleDateFormat.parse(startdate);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, -1+i);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //获得比较前一天时间
            //获得比较时间
            Date date2 = simpleDateFormat.parse(startdate);
            Calendar calendar2 = new GregorianCalendar();
            calendar2.setTime(date2);
            calendar2.add(calendar2.DATE, i);
            date2 = calendar2.getTime();
            BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
            qiandaoCheckinBaseEntityWrapper.like("createtime",simpleDateFormat.format(date2));
            qiandaoCheckinBaseEntityWrapper.isNotNull("updatetime");

            BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper2 = new BaseEntityWrapper<>();
            qiandaoCheckinBaseEntityWrapper2.like("createtime",simpleDateFormat.format(date));
            qiandaoCheckinBaseEntityWrapper2.isNotNull("updatetime");
            int i1 = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper);//当天签到人数
            int i2 = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper2);//当天前一天签到人数
            data4[i]=i1;
            data5[i]=i2;
            data6[i]=(i1-i2);
        }
        result.put("data4",data4);
        result.put("data5",data5);
        result.put("data6",data6);
        return result;
    }
}
