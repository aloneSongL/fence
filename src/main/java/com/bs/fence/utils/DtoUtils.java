package com.bs.fence.utils;

import com.bs.fence.dto.Page;
import com.bs.fence.entity.Trail;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;

import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-7:15
 */
public class DtoUtils {

    public static Page pages(PageInfo pageInfo){
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
