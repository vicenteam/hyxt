package com.stylefeng.guns.modular.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.modular.api.apiparam.RequstData;
import com.stylefeng.guns.modular.api.apiparam.ResponseData;
import com.stylefeng.guns.modular.api.base.BaseController;
import com.stylefeng.guns.modular.api.model.memerber.MemberInfoModel;
import com.stylefeng.guns.modular.api.model.memerber.MemberModel;
import com.stylefeng.guns.modular.api.util.ReflectionObject;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
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

import java.util.List;

@RestController
@RequestMapping("/api/memberClientInfo")
public class MemberInfoController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(MemberInfoController.class);

    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IActivityMemberService activityMemberService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IQiandaoCheckinService qiandaoCheckinService;
    @Autowired
    private MemberApiController memberApiController;

    @RequestMapping(value = "resultInfo",method = RequestMethod.POST)
    @ApiOperation("会员详情获取")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "selectId", value = "查询id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "selectType", value = "查询方式(1 读卡查询 2身份证查询 3 memberId查询)", paramType = "query"),
    })
    public ResponseData<MemberInfoModel> searchMemberInfo(RequstData requstData, String selectId, String selectType)throws Exception{
        ResponseData<MemberInfoModel> responseData = new ResponseData<>();
        // 调用查询方式接口
        ResponseData<MemberModel> dataCollection = memberApiController.getMemberInfo(requstData, selectId, selectType);
        MemberModel modelInfo = dataCollection.getDataCollection();
        if(modelInfo != null){
            Membermanagement mInfo = membermanagementService.selectById(modelInfo.getMemberId()); //获取会员信息
            StringBuilder mIName = new StringBuilder("");
            Membermanagement mIntroducer = membermanagementService.selectById(mInfo.getIntroducerId()); // 获取推荐人信息
            if(mIntroducer != null) mIName.append(mIntroducer.getName());
            EntityWrapper<ActivityMember> aWrapper = new EntityWrapper<>();
            aWrapper.eq("memberid",mInfo.getId());
            aWrapper.eq("deptid",mInfo.getDeptId());
//            aWrapper.groupBy("memberid");
            List<ActivityMember> aList = activityMemberService.selectList(aWrapper); //获取活动信息表数据
            Integer aCount = 0;
            if(aList != null){  //获取会员活动次数
                aCount = aList.size();
            }
            Membershipcardtype tName = membershipcardtypeService.selectById(Integer.parseInt(mInfo.getLevelID())); //获取会员等级
            Dept dName = deptService.selectById(mInfo.getDeptId()); // 获取门店名称
            User uName = userService.selectById(mInfo.getStaffID()); //获取服务员工
            // 数值初始化
            Integer singInCount = 0; Integer singOutCount = 0;
            for(int i=0; i<2; i++){  //获取签到、复签次数
                EntityWrapper<QiandaoCheckin> qWrapper = new EntityWrapper<>();
                qWrapper.eq("memberid",mInfo.getId());
                qWrapper.eq("deptid",mInfo.getDeptId());
                if(i == 0) {
                    qWrapper.notIn("createtime","null","");
                }else if(i == 1){
                    qWrapper.notIn("updatetime","null","");
                }
                Integer qNumber = qiandaoCheckinService.selectCount(qWrapper);
                if(i == 0){
                    singInCount = qNumber;
                }else if(i == 1){
                    singOutCount = qNumber;
                }
            }
            //result info
            MemberInfoModel infoModel = new ReflectionObject<MemberInfoModel>().change(mInfo, new MemberInfoModel());
            infoModel.setAvatar(mInfo.getAvatar());
            infoModel.setCadID(mInfo.getCadID());
            infoModel.setCreateDt(mInfo.getCreateTime());
            infoModel.setDeptName(dName.getFullname());
            infoModel.setServicePerson(uName.getName());
            infoModel.setIntroducer(mIName.toString());
            infoModel.setSignInNew(qiandaoCheckinService.selectNewCreateTime(mInfo.getId())); //最新签到时间
            infoModel.setSignInCount(singInCount);
            infoModel.setSingOutNew(qiandaoCheckinService.selectNewUpdateTime(mInfo.getId())); //最新复签时间
            infoModel.setSingOutCount(singOutCount);
            infoModel.setActivityNumber(aCount);
            infoModel.setMemberStatus(mInfo.getTownshipid());
            infoModel.setLevelId(tName.getCardname());
            //result info
            responseData.setDataCollection(infoModel);
        }else {
            throw new NullPointerException("卡片信息无效");
        }
        return responseData;
    }
}
