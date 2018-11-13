package com.stylefeng.guns.modular.api.controller;

import com.stylefeng.guns.modular.api.apiparam.RequstData;
import com.stylefeng.guns.modular.api.apiparam.ResponseData;
import com.stylefeng.guns.modular.api.base.BaseController;
import com.stylefeng.guns.modular.main.controller.MemberRepairController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientRepairApi")
@Api(description = "会员补签")
public class MemberRepairApiController extends BaseController {

    @Autowired
    private MemberRepairController memberRepairController;

    @RequestMapping(value = "/clientRepair", method = RequestMethod.POST)
    @ApiOperation("会员补签")
    @ApiImplicitParams({
//            @ApiImplicitParam(required = true, name = "userId", value = "操作人id", paramType = "query"),
//            @ApiImplicitParam(required = true, name = "deptId", value = "操作人部门id", paramType = "query"),
    })
    public ResponseData memberRepair(RequstData requstData,String memberId,String time) throws Exception{
        ResponseData responseData = new ResponseData();
        memberRepairController.repair(memberId,time);
        return responseData;
    }

}
