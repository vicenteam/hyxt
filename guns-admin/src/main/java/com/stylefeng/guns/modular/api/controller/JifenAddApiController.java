package com.stylefeng.guns.modular.api.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.modular.api.apiparam.RequstData;
import com.stylefeng.guns.modular.api.apiparam.ResponseData;
import com.stylefeng.guns.modular.api.base.BaseController;
import com.stylefeng.guns.modular.api.model.jifen.JifenAddModel;
import com.stylefeng.guns.modular.api.model.jifen.JifenPageDataMode;
import com.stylefeng.guns.modular.api.util.ReflectionObject;
import com.stylefeng.guns.modular.main.controller.IntegralrecordController;
import com.stylefeng.guns.modular.main.controller.MembermanagementController;
import com.stylefeng.guns.modular.main.service.IIntegralrecordService;
import com.stylefeng.guns.modular.main.service.IIntegralrecordtypeService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.Integralrecordtype;
import com.stylefeng.guns.modular.system.model.MemberCard;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import io.swagger.annotations.Api;
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
import java.util.Map;

@RestController
@RequestMapping("/api/jifenaddapi")
@Api(description = "新增积分")
public class JifenAddApiController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(JifenAddApiController.class);

    @Autowired
    private MembermanagementController membermanagementController;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IntegralrecordController integralrecordController;
    @Autowired
    private IIntegralrecordtypeService integralrecordtypeService;

    @RequestMapping(value = "/findUserInfo", method = RequestMethod.POST)
    @ApiOperation("读卡查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "userId", value = "操作人id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "deptId", value = "操作人部门id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "cardCode", value = "卡片信息", paramType = "query"),
    })
    public ResponseData<JifenAddModel> findUserInfo(RequstData requstData, String cardCode) throws Exception {
        ResponseData<JifenAddModel> responseData = new ResponseData();
        try {
            MemberCard userInfo = (MemberCard)membermanagementController.getUserInfo(cardCode);
            Integer memberid = userInfo.getMemberid();
            Membermanagement membermanagement = membermanagementService.selectById(memberid);
            JifenAddModel change = new ReflectionObject<JifenAddModel>().change(membermanagement, new JifenAddModel());
            change.setJifenAddUserId(memberid);
            //获取积分类型
            String deptId = membermanagement.getDeptId();
            EntityWrapper<Integralrecordtype> integralrecordtypeEntityWrapper = new EntityWrapper<>();
            integralrecordtypeEntityWrapper.eq("deptId",deptId);
            List<Map<String, Object>> mapList = integralrecordtypeService.selectMaps(integralrecordtypeEntityWrapper);
            if(mapList==null)throw new NoSuchMethodException("无积分类型");
            change.setJifenType(mapList);
            responseData.setDataCollection(change);
        }catch (Exception e){
            throw new Exception("系统异常!"+e.getMessage());
        }
        return responseData;
    }
    @RequestMapping(value = "/addJifen", method = RequestMethod.POST)
    @ApiOperation("新增积分")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "userId", value = "操作人id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "deptId", value = "操作人部门id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "jifenAddUserId", value = "卡片用户id", paramType = "query"),
            @ApiImplicitParam(required = true, name = "nowPice", value = "新增积分", paramType = "query"),
            @ApiImplicitParam(required = true, name = "jifenType", value = "积分类型", paramType = "query"),
    })
    public ResponseData addJifen(RequstData requstData,Integer jifenAddUserId,Double nowPice,Integer jifenType) throws Exception {
        ResponseData responseData = new ResponseData();
        try {
            integralrecordController.add(nowPice,jifenType,jifenAddUserId);
        }catch (Exception e){
            throw new Exception("新增失败 请联系管理员!");
        }
        return responseData;
    }
}
