package com.wenjun.recsys.request;

import lombok.Data;

/**
 * 分页查询第page页，每页显示size条数据
 * @Author: wenjun
 * @Date: 2020/1/4 16:40
 */
@Data
public class PageQuery {
    private Integer page = 1;
    private Integer size = 20;
}
