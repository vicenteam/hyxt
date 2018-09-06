package com.stylefeng.guns.modular.main.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.main.service.IBaMedicalService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.utils.MemberExcel;
import com.stylefeng.guns.modular.system.utils.SignInExcel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 会员管理控制器
 *
 * @author fengshuonan
 * @Date 2018-08-10 16:00:02
 */
@Controller
@RequestMapping("/membermanagement")
public class MembermanagementController extends BaseController {

    private String PREFIX = "/main/membermanagement/";
    private List<Membermanagement> membermanagements;

    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private IQiandaoCheckinService qiandaoCheckinService;
    @Autowired
    private IMemberBamedicalService memberBamedicalService;
    @Autowired
    private IBaMedicalService baMedicalService;
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IntegralrecordController integralrecordController;
    @Autowired
    private ActivityController activityController;

    /**
     * 跳转到会员管理首页
     */
    @RequestMapping("")
    public String index(Model model) {
        BaseEntityWrapper<User> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs", list);
        EntityWrapper<Dept> deptBaseEntityWrapper1 = new EntityWrapper<>();
        List depts = deptService.selectList(deptBaseEntityWrapper1);
        model.addAttribute("depts", depts);
        return PREFIX + "membermanagement.html";
    }

    /**
     * 跳转到添加会员管理
     */
    @RequestMapping("/membermanagement_add")
    public String membermanagementAdd(Model model) {
        BaseEntityWrapper<Dept> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        EntityWrapper<BaMedical> memberBamedicalEntityWrapper = new EntityWrapper<>();
        List<BaMedical> baMedicals = baMedicalService.selectList(memberBamedicalEntityWrapper);
        model.addAttribute("staffs", list);
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<baMedicals.size();i++){
            sb.append("<tr>");
                sb.append(" <td style='width: 20px'><input name='baMedicals' type='checkbox'  value='"+baMedicals.get(i-1).getId()+"'> </td><td style='width: 170px'>"+baMedicals.get(i-1).getName()+"</td>");
                sb.append(" <td style='width: 20px'><input name='baMedicals' type='checkbox'  value='"+baMedicals.get(i).getId()+"'> </td><td style='width: 170px'>"+baMedicals.get(i).getName()+"</td>");
            sb.append("</tr>");
            i++;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("val",sb.toString());
        model.addAttribute("baMedicals", map);
        return PREFIX + "membermanagement_add.html";
    }

    /**
     * 跳转到修改会员管理
     */
    @RequestMapping("/membermanagement_update/{membermanagementId}")
    public String membermanagementUpdate(@PathVariable Integer membermanagementId, Model model) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("item", membermanagement);
        BaseEntityWrapper<Dept> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs", list);
        EntityWrapper<BaMedical> memberBamedicalEntityWrapper = new EntityWrapper<>();
        List<BaMedical> baMedicals = baMedicalService.selectList(memberBamedicalEntityWrapper);
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<baMedicals.size();i++){
            sb.append("<tr>");
            sb.append(" <td style='width: 20px'><input name='baMedicals' type='checkbox'  value='"+baMedicals.get(i-1).getId()+"'> </td><td style='width: 170px'>"+baMedicals.get(i-1).getName()+"</td>");
            sb.append(" <td style='width: 20px'><input name='baMedicals' type='checkbox'  value='"+baMedicals.get(i).getId()+"'> </td><td style='width: 170px'>"+baMedicals.get(i).getName()+"</td>");
            sb.append("</tr>");
            i++;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("val",sb.toString());
        model.addAttribute("baMedicals", map);
        EntityWrapper<MemberBamedical> memberBamedicalEntityWrapper1 = new EntityWrapper<>();
        memberBamedicalEntityWrapper1.eq("memberid",membermanagementId);
        List<MemberBamedical> memberBamedicals = memberBamedicalService.selectList(memberBamedicalEntityWrapper1);
        String userbaMedicals="";
        for(MemberBamedical ba:memberBamedicals){
            userbaMedicals+= ba.getBamedicalid()+",";
        }
        LogObjectHolder.me().set(membermanagement);
        Map<String,Object> map1=new HashMap<>();
        map1.put("val",userbaMedicals);
        model.addAttribute("userbaMedicals", map1);
        return PREFIX + "membermanagement_edit.html";
    }

    /**
     * 签到记录页面
     *
     * @param membermanagementId
     * @param model
     * @return
     */
    @RequestMapping("/membermanagementcheckHistory/{membermanagementId}")
    public String membermanagementcheckHistory(@PathVariable Integer membermanagementId, Model model) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("memberId", membermanagementId);
        return PREFIX + "membermanagementcheckHistory.html";
    }

    /**
     * 获取会员管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String name, String address, String fstatus, String sex, String idcard, String phone, String stafff, String deptid, String province, String city, String district, String memberid,String townshipid) {
        Page<Membermanagement> page = new PageFactory<Membermanagement>().defaultPage();
        EntityWrapper<Membermanagement> baseEntityWrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(name)) baseEntityWrapper.eq("name", name);
        if (!StringUtils.isEmpty(address)) baseEntityWrapper.like("address", address);
        if (!StringUtils.isEmpty(fstatus)) baseEntityWrapper.eq("familyStatusID", fstatus);
        if (!StringUtils.isEmpty(sex)) baseEntityWrapper.eq("sex", sex);
        if (!StringUtils.isEmpty(idcard)) baseEntityWrapper.eq("idcard", idcard);
        if (!StringUtils.isEmpty(phone)) baseEntityWrapper.like("phone", phone);
        if (!StringUtils.isEmpty(stafff)) baseEntityWrapper.eq("staffid", stafff);
        if (!StringUtils.isEmpty(deptid)) baseEntityWrapper.eq("deptid", deptid);
        if (!StringUtils.isEmpty(province)) baseEntityWrapper.eq("province", province);
        if (!StringUtils.isEmpty(city)) baseEntityWrapper.eq("city", city);
        if (!StringUtils.isEmpty(district)) baseEntityWrapper.eq("district", district);
        if (!StringUtils.isEmpty(memberid)) baseEntityWrapper.eq("id", memberid);
        if (!StringUtils.isEmpty(townshipid)) baseEntityWrapper.eq("townshipid", townshipid);
        baseEntityWrapper.eq("state", 0);
        membermanagements = membermanagementService.selectList(baseEntityWrapper);
        Page<Map<String, Object>> mapPage = membermanagementService.selectMapsPage(page, baseEntityWrapper);
        List<Map<String, Object>> records = mapPage.getRecords();
        for (Map<String, Object> map : records) {
            String s = (String) map.get("levelID");
            Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(s);
            if(membershipcardtype!=null){
                map.put("levelID",membershipcardtype.getCardname());
            }
        }
        return super.packForBT(mapPage);
    }

    /**
     * 新增会员管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object add(Membermanagement membermanagement, String cardCode,String baMedicals,String code) {
        membermanagement.setCreateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        membermanagement.setDeptId("" + ShiroKit.getUser().getDeptId());
        membermanagement.setTownshipid("0");
        membermanagementService.insert(membermanagement);
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
//        memberCardBaseEntityWrapper.eq("code", cardCode);
        memberCardBaseEntityWrapper.eq("code", code);
        MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
        memberCard.setName(membermanagement.getName());
        memberCard.setMemberid(membermanagement.getId());
        memberCardService.updateById(memberCard);

        //保存用户健康信息
        if(!StringUtils.isEmpty(baMedicals)){
            String[] split = baMedicals.split(",");
            for(String s:split){
                MemberBamedical memberBamedical = new MemberBamedical();
                memberBamedical.setBamedicalid(Integer.parseInt(s));
                memberBamedical.setMemberid(membermanagement.getId());
                memberBamedicalService.insert(memberBamedical);
            }
        }
        //判断推荐人]
        String introducerId = membermanagement.getIntroducerId();
        if(!StringUtils.isEmpty(introducerId)){
            Membermanagement membermanagement1 = membermanagementService.selectById(introducerId);
            BaseEntityWrapper<Activity> activityBaseEntityWrapper = new BaseEntityWrapper<>();
            activityBaseEntityWrapper.eq("ruleexpression",3);
            Activity activity = activityService.selectOne(activityBaseEntityWrapper);
            if(membermanagement1!=null){
                Integer ruleexpression = activity.getRuleexpression();
                if (ruleexpression == 3) {//积分操作
                    Double jifen = activity.getJifen();//
                    //积分操作
                    List<Membermanagement> membermanagements = new ArrayList<>();
                    membermanagements.add(membermanagement);
                    //调用积分变动方法
                    integralrecordController.insertIntegral(jifen, 1, membermanagements);
                }
                activityController.insertAcitvityMember(activity.getId()+"", membermanagement.getId()+"");
            }
        }
        return SUCCESS_TIP;
    }

    /**
     * 删除会员管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer membermanagementId) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        membermanagement.setState(1);
        membermanagementService.updateById(membermanagement);
        return SUCCESS_TIP;
    }

    /**
     * 修改会员管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object update(Membermanagement membermanagement,String baMedicals) {
        membermanagementService.updateById(membermanagement);
        //删除历史健康记录
        EntityWrapper<MemberBamedical> memberBamedicalEntityWrapper = new EntityWrapper<>();
        memberBamedicalEntityWrapper.eq("memberid",membermanagement.getId());
        memberBamedicalService.delete(memberBamedicalEntityWrapper);
        //保存用户健康信息
        if(!StringUtils.isEmpty(baMedicals)){
            String[] split = baMedicals.split(",");
            for(String s:split){
                MemberBamedical memberBamedical = new MemberBamedical();
                memberBamedical.setMemberid(membermanagement.getId());
                memberBamedical.setBamedicalid(Integer.parseInt(s));
                memberBamedicalService.insert(memberBamedical);
            }
        }
        return SUCCESS_TIP;
    }

    /**
     * 会员管理详情
     */
    @RequestMapping(value = "/detail/{membermanagementId}")
    @ResponseBody
    public Object detail(@PathVariable("membermanagementId") Integer membermanagementId) {
        return membermanagementService.selectById(membermanagementId);
    }

    @RequestMapping(value = "/getXieKaVal")
    @ResponseBody
    public Object getXieKaVal(String code) throws Exception {
        return getXieKaValInfo(code);
    }

    @RequestMapping(value = "/getUserInfo")
    @ResponseBody
    public Object getUserInfo(String value) {
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
        memberCardBaseEntityWrapper.eq("code", value);
        MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
        //卡片是否锁定
       if(memberCard!=null&&memberCard.getMemberid()!=null){
           Integer memberid = memberCard.getMemberid();
           Membermanagement membermanagement = membermanagementService.selectById(memberid);
           if(membermanagement!=null){
               String townshipid = membermanagement.getTownshipid();
               if(townshipid.equals("1")){
                   return "202";
               }
           }
       }
        return memberCard;
    }

    public String getXieKaValInfo(String code) throws Exception {
        String chars = "ABCDEF1234567890";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt((int) (Math.random() * 16)));
        }
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
//        memberCardBaseEntityWrapper.eq("code", sb.toString());
        memberCardBaseEntityWrapper.eq("code", code);
        int i = memberCardService.selectCount(memberCardBaseEntityWrapper);
        if (i != 0) {
            throw new Exception("失败");
//            getXieKaValInfo(code);
        }
        MemberCard memberCard = new MemberCard();
//        memberCard.setCode(sb.toString());
        memberCard.setCode(code);
        memberCard.setCreatetime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        memberCard.setDeptid(ShiroKit.getUser().getDeptId());
        memberCardService.insert(memberCard);
        return sb.toString();
    }


    /**
     * 通过id获取用户信息
     */
    @RequestMapping("getMemberInfo")
    @ResponseBody
    public Object selectMemberInfo(Integer memberId){
        Membermanagement m = membermanagementService.selectById(memberId);
        Membershipcardtype ms = membershipcardtypeService.selectById(m.getLevelID());
        Map<String, Object> memberinfo = new HashMap<>();
        memberinfo.put("cadID",m.getCadID());
        memberinfo.put("name",m.getName());
        memberinfo.put("id",m.getId());
        memberinfo.put("phone",m.getPhone());
        memberinfo.put("address",m.getAddress());
        memberinfo.put("integral",m.getIntegral());
        memberinfo.put("countPrice",m.getCountPrice());
        memberinfo.put("levelID",ms.getCardname());
        return memberinfo;
    }

    @RequestMapping("/openintroducer/{membermanagementId}")
    public String openintroducerdata(@PathVariable Integer membermanagementId, Model model) {
        model.addAttribute("memberId", membermanagementId);
        return PREFIX + "openintroducer.html";
    }

    @RequestMapping(value = "/openintroducerdata/{id}")
    @ResponseBody
    public Object openintroducerdata(String name, @PathVariable Integer id) {
        Page<Membermanagement> page = new PageFactory<Membermanagement>().defaultPage();
        EntityWrapper<Membermanagement> baseEntityWrapper = new EntityWrapper<>();
        baseEntityWrapper.eq("introducerId", id);
        Page<Map<String, Object>> mapPage = membermanagementService.selectMapsPage(page, baseEntityWrapper);
        List<Map<String, Object>> records = mapPage.getRecords();
        for (Map<String, Object> map : records) {
            String s = (String) map.get("levelID");
            Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(s);
            if (membershipcardtype != null && membershipcardtype.getUpamount() == 0) {
                //获取总签到场次次数
                BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
                qiandaoCheckinBaseEntityWrapper.eq("memberid", map.get("id"));
                qiandaoCheckinBaseEntityWrapper.isNotNull("updatetime");
                int i = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper);
                map.put("levelID", "<span style='color:red;'>还差" + (membershipcardtype.getCheckleavenum() - i) + "次签到成为普通会员</span>");
                map.put("count", i);
            } else if (membershipcardtype != null && membershipcardtype.getUpamount() != 0) {
                map.put("levelID", membershipcardtype.getCardname());
                //获取总签到场次次数
                BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
                qiandaoCheckinBaseEntityWrapper.eq("memberid", map.get("id"));
                int i = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper);
                map.put("count", i);

            }

        }
        return super.packForBT(mapPage);
    }

    /**
     * 进行挂失
     * @param model
     * @return
     */
    @RequestMapping("/guashi")
    public String guashi( Model model) {
        BaseEntityWrapper<User> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs", list);
        EntityWrapper<Dept> deptBaseEntityWrapper1 = new EntityWrapper<>();
        List depts = deptService.selectList(deptBaseEntityWrapper1);
        model.addAttribute("depts", depts);
        return PREFIX + "guashi.html";
    }
    @RequestMapping("/guashiData")
    @ResponseBody
    public Object guashiData(String memberId) {
        Membermanagement m = membermanagementService.selectById(memberId);
        m.setTownshipid("1");
        membermanagementService.updateById(m);
        return SUCCESS_TIP;
    }
    @RequestMapping("/guashiData1")
    @ResponseBody
    public Object guashiData1(String memberId) {
        Membermanagement m = membermanagementService.selectById(memberId);
        m.setTownshipid("0");
        membermanagementService.updateById(m);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户等级
     * @param memberId
     */
    public  void updateMemberLeave(String memberId){
        Membermanagement membermanagement = membermanagementService.selectById(memberId);
        Double countPrice = membermanagement.getCountPrice();
        BaseEntityWrapper<Membershipcardtype> membershipcardtypeBaseEntityWrapper = new BaseEntityWrapper<>();
        membershipcardtypeBaseEntityWrapper.orderBy("upamount",false);
        List<Membershipcardtype> list = membershipcardtypeService.selectList(membershipcardtypeBaseEntityWrapper);
        for(Membershipcardtype membershipcardtype:list){
            if(countPrice>=membershipcardtype.getUpamount()){
                membermanagement.setLevelID(membershipcardtype.getId()+"");
                membermanagementService.updateById(membermanagement);
                break;
            }
        }
    }

    @RequestMapping("export_excel")
    public void export(HttpServletResponse response, HttpServletRequest request) throws Exception{
        List<Map<String,Object>> memberExcels = new ArrayList<>();
        for(Membermanagement m : membermanagements){
            Map<String,Object> mMap = new LinkedHashMap<>();
            mMap.put("name",m.getName());
            mMap.put("sex",m.getSex());
            mMap.put("phone",m.getPhone());
            mMap.put("address",m.getAddress());
            mMap.put("integral",m.getIntegral());
            mMap.put("countPrice",m.getCountPrice());
            mMap.put("isoldsociety",m.getIsoldsociety());
            mMap.put("level",membershipcardtypeService.selectById(m.getLevelID()).getCardname());
            mMap.put("createDt",m.getCreateTime());
            memberExcels.add(mMap);
        }
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(100);
        SXSSFSheet sxssfSheet = sxssfWorkbook.createSheet();
        Map<String,Object> mapTile = memberExcels.get(0);
        //创建excel 数据列名
        SXSSFRow rowTitle = sxssfSheet.createRow(0);
        Integer j = 0;
        for (Map.Entry<String,Object> entry: mapTile.entrySet()) {
            if(entry.getKey().equals("name")){
                CellUtil.createCell(rowTitle,j,"客户名称");
            }else if (entry.getKey().equals("sex")){
                CellUtil.createCell(rowTitle,j,"性别");
            }else if (entry.getKey().equals("phone")){
                CellUtil.createCell(rowTitle,j,"联系电话");
            }else if (entry.getKey().equals("address")){
                CellUtil.createCell(rowTitle,j,"联系地址");
            }else if (entry.getKey().equals("integral")){
                CellUtil.createCell(rowTitle,j,"可用积分");
            }else if (entry.getKey().equals("countPrice")){
                CellUtil.createCell(rowTitle,j,"总积分");
            }else if (entry.getKey().equals("isoldsociety")){
                CellUtil.createCell(rowTitle,j,"是否老年协会会员");
            }else if (entry.getKey().equals("level")){
                CellUtil.createCell(rowTitle,j,"卡片等级");
            }else if (entry.getKey().equals("createDt")){
                CellUtil.createCell(rowTitle,j,"开卡时间");
            }
            j++;
        }
        for (int i = 0; i < memberExcels.size(); i++) {
            Map<String,Object> nMap = memberExcels.get(i);
            SXSSFRow row = sxssfSheet.createRow(i+1);
            // 数据
            Integer k = 0;
            for (Map.Entry<String,Object> ma: nMap.entrySet()) {
                String value = "";
                if(ma.getValue() != null){
                    value = ma.getValue().toString();
                }
                CellUtil.createCell(row,k,value);
                k++;
            }
        }
        response.setHeader("content-Type","application/vnc.ms-excel;charset=utf-8");
        //文件名使用uuid，避免重复
        response.setHeader("Content-Disposition", "attachment;filename=" + "会员信息" + ".xlsx");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        try {
            sxssfWorkbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            memberExcels.clear();
            outputStream.close();
        }
    }
}
