package com.sinjee.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.wechat.dto.ProductInfoDTO;
import com.sinjee.wechat.entity.ProductInfo;
import com.sinjee.wechat.mapper.ProductInfoMapper;
import com.sinjee.wechat.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author 小小极客
 * 时间 2019/12/15 16:38
 * @ClassName ProductInfoServiceImpl
 * 描述 商品信息服务类
 **/
@Service
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper ;

    @Override
    public IPage<ProductInfoDTO> selectProductInfosByPage(Integer currentPage,Integer pageSize,Integer productStatus) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_status",productStatus);
        Page<ProductInfo> page = new Page<ProductInfo>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<ProductInfo> mapPage = productInfoMapper.selectProductInfosByPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductInfo> productInfoEntityList = mapPage.getRecords() ;
        List<ProductInfoDTO> productInfoDTOList = BeanConversionUtils.CopyToAnotherList(ProductInfoDTO.class,productInfoEntityList);

        Page<ProductInfoDTO> productInfoDTOPage = new Page<>(currentPage,pageSize) ;
        productInfoDTOPage.setPages(mapPage.getPages()); //设置总页数
        productInfoDTOPage.setTotal(mapPage.getTotal()); //设置总数
        productInfoDTOPage.setRecords(productInfoDTOList) ; //设置内容
        return productInfoDTOPage;
    }

    @Override
    public IPage<ProductInfoDTO> selectProductInfosByProductStatus(Integer currentPage, Integer pageSize, Integer productStatus) {
        Page<ProductInfo> page = new Page<ProductInfo>(currentPage,pageSize) ;
        IPage<ProductInfo> mapPage = productInfoMapper.selectProductInfosByProductStatus(page,productStatus);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductInfo> productInfoEntityList = mapPage.getRecords() ;
        productInfoEntityList.forEach(System.out::println);

        return null;
    }

    @Override
    @Transactional
    public int saveProductInfo(ProductInfoDTO productInfoDTO) {
        ProductInfo productInfoEntity = new ProductInfo() ;
        //拷贝属性
        CacheBeanCopier.copy(productInfoDTO,productInfoEntity);
        return productInfoMapper.saveProductInfo(productInfoEntity);
    }
}
