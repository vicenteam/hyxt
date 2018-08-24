package com.stylefeng.guns.modular.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.modular.api.apiparam.RequstData;
import com.stylefeng.guns.modular.api.apiparam.ResponseData;
import com.stylefeng.guns.modular.api.base.BaseController;
import com.stylefeng.guns.modular.api.model.checkin.CheckInModel;
import com.stylefeng.guns.modular.api.model.checkin.CheckInTimeModel;
import com.stylefeng.guns.modular.api.model.memerber.MemberModel;
import com.stylefeng.guns.modular.api.util.ReflectionObject;
import com.stylefeng.guns.modular.main.service.IMemberCardService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.MemberCard;
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

@RestController
@RequestMapping("/api/memberapi")
public class MemberApiController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(MemberApiController.class);

    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @RequestMapping(value = "/getMemberInfo", method = RequestMethod.POST)
    @ApiOperation("获取会员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "selectId", value = "查询id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "selectType", value = "查询方式(1 读卡查询 2身份证查询 3 memberId查询)", paramType = "query"),
    })
    public ResponseData<MemberModel> getMemberInfo(RequstData requstData,String selectId, String selectType ) throws Exception {
        ResponseData<MemberModel> memberModelResponseData = new ResponseData<>();
        if(StringUtils.isEmpty(selectId))throw new Exception("selectId为空");
        if(StringUtils.isEmpty(selectType))throw new Exception("selectType为空");
        if(selectType.equals("1")){
            EntityWrapper<MemberCard> memberCardEntityWrapper = new EntityWrapper<>();
            memberCardEntityWrapper.eq("deptid",requstData.getDeptId());
            memberCardEntityWrapper.eq("code",selectId);
            MemberCard memberCard = memberCardService.selectOne(memberCardEntityWrapper);
            if(memberCard!=null){
                Membermanagement membermanagement = membermanagementService.selectById(memberCard.getMemberid());
                if(membermanagement!=null){
                    MemberModel change = new ReflectionObject<MemberModel>().change(membermanagement, new MemberModel());
                    change.setMemberId(membermanagement.getId());
                    change.setAvatar("http://127.0.0.1:8081/kaptcha/"+ change.getAvatar());//上线更改地址
                    memberModelResponseData.setDataCollection(change);
                }
            }
        }else if(selectType.equals("2")){
            EntityWrapper<Membermanagement> membermanagementEntityWrapper = new EntityWrapper<>();
            membermanagementEntityWrapper.eq("deptid",requstData.getDeptId());
            membermanagementEntityWrapper.eq("cadID",selectId);
            Membermanagement membermanagement = membermanagementService.selectOne(membermanagementEntityWrapper);
            if(membermanagement!=null){
                MemberModel change = new ReflectionObject<MemberModel>().change(membermanagement, new MemberModel());
                change.setMemberId(membermanagement.getId());
                change.setAvatar("http://127.0.0.1:8081/kaptcha/"+ change.getAvatar());//上线更改地址
                memberModelResponseData.setDataCollection(change);
            }
        }else if(selectType.equals("3")){
            Membermanagement membermanagement = membermanagementService.selectById(selectId);
            if(membermanagement!=null){
                MemberModel change = new ReflectionObject<MemberModel>().change(membermanagement, new MemberModel());
                change.setMemberId(membermanagement.getId());
                change.setAvatar("http://127.0.0.1:8081/kaptcha/"+ change.getAvatar());//上线更改地址
                memberModelResponseData.setDataCollection(change);
            }
        }
        return  memberModelResponseData;
    }
}
