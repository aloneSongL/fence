package com.bs.fence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sjx
 * @Description TODO
 * @create 2023-03-08-5:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
////测试PageInfo全部属性
////PageInfo包含了非常全面的分页属性
//assertEquals(1, page.getPageNum());
//assertEquals(10, page.getPageSize());
//assertEquals(1, page.getStartRow());
//assertEquals(10, page.getEndRow());
//assertEquals(183, page.getTotal());
//assertEquals(19, page.getPages());
//assertEquals(1, page.getFirstPage());
//assertEquals(8, page.getLastPage());
//assertEquals(true, page.isFirstPage());
//assertEquals(false, page.isLastPage());
//assertEquals(false, page.isHasPreviousPage());
//assertEquals(true, page.isHasNextPage());
    private Integer pageNum;
    private Long pageTotal;
    private Integer pageSize;
    private Integer firstPage;
    private Integer endPage;
    private Integer endRow;
    private Boolean isFirstPage;
    private Boolean isLastPage;
    private Boolean isHasPreviousPage;
    private Boolean isHasNextPage;
}
