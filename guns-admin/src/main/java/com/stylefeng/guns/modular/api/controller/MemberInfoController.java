package com.stylefeng.guns.modular.api.controller;

import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.modular.api.apiparam.ResponseData;
import com.stylefeng.guns.modular.api.base.BaseController;
import com.stylefeng.guns.modular.api.model.memerber.MemberInfoModel;
import com.stylefeng.guns.modular.api.model.memerber.ParameterModel;
import com.stylefeng.guns.modular.api.util.ReflectionObject;
import com.stylefeng.guns.modular.main.service.IActivityMemberService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.main.service.IMembershipcardtypeService;
import com.stylefeng.guns.modular.main.service.IQiandaoCheckinService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "resultInfo",method = RequestMethod.POST)
    @ApiOperation("会员详情获取")
    public ResponseData<MemberInfoModel> searchMemberInfo(ParameterModel parameterModel){
        ResponseData<MemberInfoModel> responseData = new ResponseData<>();
        String mIName = "";
        BaseEntityWrapper<MemberInfoController> mWrapper = new BaseEntityWrapper<>();
        mWrapper.eq("id",parameterModel.getMemberid());
        Membermanagement mInfo = membermanagementService.selectOne(mWrapper); //获取会员信息
        BaseEntityWrapper<Membermanagement> mIWrapper = new BaseEntityWrapper<>();
        mIWrapper.eq("id",mInfo.getIntroducerId());
        Membermanagement mIntroducer = membermanagementService.selectOne(mIWrapper); // 获取推荐人信息
        if(mIntroducer != null) mIName = mIntroducer.getName();
        BaseEntityWrapper<ActivityMember> aWrapper = new BaseEntityWrapper<>();
        aWrapper.eq("memberid",parameterModel.getMemberid());
        List<ActivityMember> aList = activityMemberService.selectList(aWrapper);
        Integer aCount = 0;
        if(aList != null){  //获取会员活动次数
            aCount = aList.size();
        }
        Membershipcardtype tName = membershipcardtypeService.selectById(Integer.parseInt(mInfo.getLevelID())); //获取会员等级
        Dept dName = deptService.selectById(mInfo.getDeptId()); // 获取门店名称
        User uName = userService.selectById(mInfo.getStaffID()); //获取服务员工
        BaseEntityWrapper<QiandaoCheckin> qWrapper = new BaseEntityWrapper<>();
        qWrapper.eq("memberid",parameterModel.getMemberid());
        List<QiandaoCheckin> qInfo = qiandaoCheckinService.selectList(qWrapper);
        Integer signInCount = 0; Integer signOutCount = 0;
        for(int i=0; i<qInfo.size(); i++){  // 求取签到、复签次数
            if(qInfo.get(i).getCreatetime() != null && ! qInfo.get(i).equals("")){
                signInCount += 1;
            }else if(qInfo.get(i).getUpdatetime() != null && ! qInfo.get(i).getUpdatetime().equals("")){
                signOutCount += 1;
            }
        }

        //result info
        MemberInfoModel infoModel = new ReflectionObject<MemberInfoModel>().change(mInfo, new MemberInfoModel());
        infoModel.setAvatar(mInfo.getAvatar());
        infoModel.setCadID(mInfo.getCadID());
        infoModel.setCreateDt(mInfo.getCreateTime());
        infoModel.setDeptName(dName.getFullname());
        infoModel.setServicePerson(uName.getName());
        infoModel.setIntroducer(mIName);
        infoModel.setSignInCount(signInCount);
        infoModel.setSignInNew(mInfo.getCheckINTime1());
        infoModel.setSingOutNew(mInfo.getCheckINTime2());
        infoModel.setSingOutCount(signOutCount);
        infoModel.setActivityNumber(aCount);
        infoModel.setMemberStatus(mInfo.getTownshipid());
        infoModel.setLevelId(tName.getCardname());
        //result info
        responseData.setDataCollection(infoModel);
        return responseData;
    }
}
