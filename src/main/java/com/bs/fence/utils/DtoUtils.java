package com.bs.fence.utils;

import com.bs.fence.dto.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-7:15
 */
public class DtoUtils {

    public static Page Pages(PageInfo pageInfo){
        Page page = new Page();
        page.setPageNum(pageInfo.getPageNum());
        page.setPageTotal(pageInfo.getTotal());
        page.setPageSize(pageInfo.getPages());
        page.setFirstPage(1);
        page.setEndPage(pageInfo.getPages());
        page.setEndRow(((Number) pageInfo.getEndRow()).intValue());
        page.setIsFirstPage(pageInfo.isIsFirstPage());
        page.setIsLastPage(pageInfo.isIsLastPage());
        page.setIsHasPreviousPage(pageInfo.isHasPreviousPage());
        page.setIsHasNextPage(pageInfo.isHasNextPage());
        return page;
    }
}
