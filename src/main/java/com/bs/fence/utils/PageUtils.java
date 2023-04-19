package com.bs.fence.utils;


import com.bs.fence.dto.PageDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-7:15
 */
public class PageUtils {
    /**
    @author sjx
    @Description 将pageInfo转换成自定义Page
    @since 2023-04-13 00-06
    */
    public static PageDto getPage(PageInfo pageInfo){
        PageDto page = new PageDto();
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

    /**
    @author sjx
    @Description 对查询的列表再次分页
    @since 2023-04-13 00-06
    */
    public static PageInfo pageHelperPlus(List list,Integer pageNum,Integer pageSize){
        Page page = new Page(pageNum, pageSize);
        int total = list.size();
        page.setTotal(total);
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize,total);
        if(startIndex>endIndex){
            page.addAll(new ArrayList());
            PageInfo pageInfo = new PageInfo<>(page);
            return pageInfo;
        }else{
            page.addAll(list.subList(startIndex,endIndex));
            PageInfo pageInfo = new PageInfo<>(page);
            return pageInfo;
        }
    }
}