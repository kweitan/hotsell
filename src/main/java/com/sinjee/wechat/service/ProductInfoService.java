package com.sinjee.wechat.service;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.sinjee.wechat.dto.ProductInfoDTO;
import org.springframework.data.domain.Page;


public interface ProductInfoService {
    //根据status分页查找商品
    Page<ProductInfoDTO> selectProductInfosByPage(Pagination page, Integer status);
}
