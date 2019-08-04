package com.stylefeng.guns.core.common.constant.factory;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.state.Order;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.core.util.ToolUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengshuonan
 * @date 2017-04-05 22:25
 */
public class PageFactory<T> {

    public Page<T> defaultPage() {
        HttpServletRequest request = HttpKit.getRequest();
        int limit =10;
        int offset =1;
        String sort="";
        String order="";
         try{
             limit = Integer.valueOf(request.getParameter("limit"));     //每页多少条数据
             offset = Integer.valueOf(request.getParameter("offset"));   //每页的偏移量(本页当前有多少条)
             sort = request.getParameter("sort");         //排序字段名称
             order = request.getParameter("order");       //asc或desc(升序或降序)
         }catch (Exception e){

         }
        if (ToolUtil.isEmpty(sort)) {
            Page<T> page = new Page<>((offset / limit + 1), limit);
            page.setOpenSort(false);
            return page;
        } else {
            Page<T> page = new Page<>((offset / limit + 1), limit, sort);
            if (Order.ASC.getDes().equals(order)) {
                page.setAsc(true);
            } else {
                page.setAsc(false);
            }
            return page;
        }
    }
    public Page<T> defaultPage(int offset,int limit) {
        String sort="";
        String order="";
        if (ToolUtil.isEmpty(sort)) {
            System.out.println("--"+offset);
            Page<T> page = new Page<>((offset / limit + 1), limit);
            page.setOpenSort(false);
            return page;
        } else {
            Page<T> page = new Page<>((offset / limit + 1), limit, sort);
            if (Order.ASC.getDes().equals(order)) {
                page.setAsc(true);
            } else {
                page.setAsc(false);
            }
            return page;
        }
    }
}
