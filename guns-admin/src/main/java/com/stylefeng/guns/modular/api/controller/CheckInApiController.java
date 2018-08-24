package com.stylefeng.guns.modular.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.api.apiparam.ResponseData;
import com.stylefeng.guns.modular.api.base.BaseController;
import com.stylefeng.guns.modular.api.model.checkin.CheckInModel;
import com.stylefeng.guns.modular.api.model.checkin.CheckInTimeModel;
import com.stylefeng.guns.modular.api.model.user.UserModel;
import com.stylefeng.guns.modular.api.util.ReflectionObject;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.main.service.IQiandaoCheckinService;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.system.model.QiandaoCheckin;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/checkinapi")
public class CheckInApiController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(CheckInApiController.class);
@Autowired
private IQiandaoCheckinService qiandaoCheckinService;
@Autowired
private IMembermanagementService membermanagementService;

    @RequestMapping(value = "/getCheckInRecord", method = RequestMethod.POST)
    @ApiOperation("获取签到详情数据")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "selectId", value = "查询id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "selectType", value = "查询方式(1 读卡查询 2身份证查询 3 memberId查询)", paramType = "query"),
            @ApiImplicitParam(required = true, name = "selectYear", value = "查询年", paramType = "query"),
            @ApiImplicitParam(required = true, name = "selectMonth", value = "查询月", paramType = "query"),
    })
    public ResponseData<CheckInModel> getCheckInRecord(String selectId,Integer selectType,String selectYear,String selectMonth){
        ResponseData<CheckInModel> checkInModelResponseData = new ResponseData<>();
        EntityWrapper<QiandaoCheckin> memberCardEntityWrapper = new EntityWrapper<>();
        memberCardEntityWrapper.eq("memberid",selectId);
        String starTime=selectYear+"-";
        if(selectMonth.length()==1){
            starTime+="0"+selectMonth+"-01 00:00:01";
        }else {
            starTime+=selectMonth+"-01 00:00:01";
        }
        String endTime=selectYear+"-";
        if(selectMonth.length()==1){
            endTime+="0"+selectMonth+"-31 23:59:59";
        }else {
            endTime+=selectMonth+"-31 23:59:59";
        }
        memberCardEntityWrapper.between("createtime",starTime,endTime);
        List<QiandaoCheckin> list = qiandaoCheckinService.selectList(memberCardEntityWrapper);
        List<CheckInTimeModel> checkInTimeModels = new ReflectionObject<CheckInTimeModel>().changeList(list, new CheckInTimeModel());
        Membermanagement membermanagement =null;
        CheckInModel checkInModel=new CheckInModel();
        if(selectType==3){
             membermanagement = membermanagementService.selectById(selectId);
            checkInModel.setName(membermanagement.getName());
            checkInModel.setCardID(membermanagement.getCardID());
            checkInModel.setCheckInRecord(checkInTimeModels);
        }
        checkInModelResponseData.setDataCollection(checkInModel);
        return checkInModelResponseData;
    }
}
