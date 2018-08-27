package com.stylefeng.guns.modular.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.api.apiparam.RequstData;
import com.stylefeng.guns.modular.api.apiparam.ResponseData;
import com.stylefeng.guns.modular.api.base.BaseController;
import com.stylefeng.guns.modular.api.base.PageModel;
import com.stylefeng.guns.modular.api.model.activity.ActivityModel;
import com.stylefeng.guns.modular.api.model.activity.SettlementActivityPageDataModel;
import com.stylefeng.guns.modular.api.model.memerber.MemberModel;
import com.stylefeng.guns.modular.api.util.ReflectionObject;
import com.stylefeng.guns.modular.main.controller.ActivityController;
import com.stylefeng.guns.modular.main.service.IActivityService;
import com.stylefeng.guns.modular.system.model.Activity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activityapi")
public class ActivityApiController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(ActivityApiController.class);

    @Autowired
    private IActivityService activityService;
    @Autowired
    private MemberApiController memberApiController;
    @Autowired
    private ActivityController activityController;

    @RequestMapping(value = "/getActivityPageList", method = RequestMethod.POST)
    @ApiOperation("有效活动分页数据查询")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "userId", value = "登录人id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "deptId", value = "登录人门店id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "offset", value = "当前页码(从0开始)", paramType = "query"),
            @ApiImplicitParam(required = true, name = "limit", value = "每页条数", paramType = "query"),
    })
    public ResponseData<PageModel<ActivityModel>> getActivityPageList(RequstData requstData, Integer offset, Integer limit) throws Exception {
        ResponseData<PageModel<ActivityModel>> activityModelResponseData = new ResponseData<>();
        EntityWrapper<Activity> activityEntityWrapper = new EntityWrapper<>();
        activityEntityWrapper.eq("deptid", requstData.getDeptId());
        activityEntityWrapper.eq("status", 2);
        Page<Activity> page = new PageFactory<Activity>().defaultPage();
        if (offset != null && limit != null) {
            Page<Activity> activityPage = activityService.selectPage(page, activityEntityWrapper);
            List<Activity> records = activityPage.getRecords();
            List<ActivityModel> activityModels = new ReflectionObject<ActivityModel>().changeList(records, new ActivityModel());
            PageModel<ActivityModel> pageModel=new PageModel<ActivityModel>();
            pageModel.setTotal(activityPage.getTotal());
            pageModel.setDataList(activityModels);
            activityModelResponseData.setDataCollection(pageModel);
        } else {
            throw new Exception("offset,limit参数不能为空!");
        }
        return activityModelResponseData;
    }
    @RequestMapping(value = "/settlementActivityPageData1", method = RequestMethod.POST)
    @ApiOperation("活动领取页面-基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "selectId", value = "查询id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "selectType", value = "查询方式(1 读卡查询 2身份证查询 3 memberId查询)", paramType = "query"),
    })
    private  ResponseData<SettlementActivityPageDataModel> settlementActivityPageData1(RequstData requstData, String selectId, String selectType) throws Exception {
        ResponseData<SettlementActivityPageDataModel> settlementActivityPageDataModelResponseData = new ResponseData<>();
        ResponseData<MemberModel> memberInfo = memberApiController.getMemberInfo(requstData, selectId, selectType);
        MemberModel dataCollection = memberInfo.getDataCollection();
        return settlementActivityPageDataModelResponseData;
    }
    @RequestMapping(value = "/settlementActivityPageData2", method = RequestMethod.POST)
    @ApiOperation("活动领取页面-礼品明细分页")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "memberId", value = "查询id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "offset", value = "当前页码(从0开始)", paramType = "query"),
            @ApiImplicitParam(required = true, name = "limit", value = "每页条数", paramType = "query"),
    })
    private void settlementActivityPageData2(RequstData requstData, String memberId, Integer offset, Integer limit) throws Exception {


    }
    @RequestMapping(value = "/settlementActivityData", method = RequestMethod.POST)
    @ApiOperation("领取活动")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "memberId", value = "领取人id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "activityId", value = "活动id", paramType = "query"),
    })
    private ResponseData settlementActivityData(RequstData requstData,String memberId,String activityId){
        ResponseData activityModelResponseData = new ResponseData<>();
        activityController.lingqu(activityId,memberId);
        return activityModelResponseData;
    }
}
