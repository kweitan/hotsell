package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.sinjee.wechat.dto.ProductInfoDTO;
import com.sinjee.wechat.entity.ProductInfoEntity;
import com.sinjee.wechat.mapper.ProductInfoMapper;
import com.sinjee.wechat.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/12/15 16:38
 * @ClassName ProductInfoServiceImpl
 * 描述 商品信息服务类
 **/
@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper ;

    @Override
    public Page<ProductInfoDTO> selectProductInfosByPage(Pagination page, Integer status) {
        Page<ProductInfoEntity> pageEntity = productInfoMapper.selectProductInfosByPage(page,status);
        List<ProductInfoEntity> productInfoEntityList = pageEntity.getContent() ;
        return null ;


    }
}
