package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.dto.ProductCategoryMidDTO;
import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.entity.ProductDetailInfo;
import com.sinjee.admin.entity.ProductInfo;
import com.sinjee.admin.mapper.ProductInfoMapper;
import com.sinjee.admin.service.ProductCategoryMidService;
import com.sinjee.admin.service.ProductDetailInfoService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;

import com.sinjee.common.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

    @Autowired
    private ProductCategoryMidService productCategoryMidService ;

    @Autowired
    private ProductDetailInfoService productDetailInfoService ;

    @Override
    public IPage<ProductInfoDTO> selectProductInfosByPage(Integer currentPage, Integer pageSize,String goodsName) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).like("product_name",goodsName);
        Page<ProductInfo> page = new Page<ProductInfo>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<ProductInfo> mapPage = productInfoMapper.selectProductInfosByPage(page,wrapper);
        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductInfo> productInfoEntityList = mapPage.getRecords() ;
        List<ProductInfoDTO> productInfoDTOList = BeanConversionUtils.copyToAnotherList(ProductInfoDTO.class,productInfoEntityList);

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
    public Integer saveProductInfo(ProductInfoDTO productInfoDTO) {
        ProductInfo productInfo = new ProductInfo() ;
        //拷贝属性
        CacheBeanCopier.copy(productInfoDTO,productInfo);
        return productInfoMapper.saveProductInfo(productInfo);
    }

    @Override
    @Transactional
    public Integer save(ProductInfoDTO productInfoDTO,ProductDetailInfoDTO productDetailInfoDTO) {
        ProductInfo productInfo = new ProductInfo() ;

        //商品编码
        String productNumber = IdUtil.genId() ;
        //拷贝属性
        CacheBeanCopier.copy(productInfoDTO,productInfo);
        productInfo.setProductNumber(productNumber);

        //保存商品中间表
        List<String> numberLists = productInfoDTO.getAllCategoryLists() ;
        for (String number: numberLists){
            ProductCategoryMidDTO productCategoryMidDTO = new ProductCategoryMidDTO() ;
            productCategoryMidDTO.setCategoryNumber(number);
            productCategoryMidDTO.setProductNumber(productNumber);
            productCategoryMidDTO.setCreator(productInfoDTO.getCreator());
            productCategoryMidDTO.setUpdater(productInfoDTO.getUpdater());
            productCategoryMidService.saveProductCategoryMidInfo(productCategoryMidDTO) ;
        }

        //保存商品明细
        productDetailInfoDTO.setProductNumber(productNumber);
        productDetailInfoDTO.setEnableFlag(1);
        productDetailInfoService.save(productDetailInfoDTO);

        //保存商品信息
        return productInfoMapper.saveProductInfo(productInfo);
    }

    @Override
    @Transactional
    public Integer update(ProductInfoDTO productInfoDTO,ProductDetailInfoDTO productDetailInfoDTO) {

        ProductInfo productInfo = new ProductInfo() ;
        //拷贝属性
        CacheBeanCopier.copy(productInfoDTO,productInfo);


        //商品编码
        String productNumber = productInfoDTO.getProductNumber() ;

        //前端页面传过来的类目编码
        List<String> categoryNumberLists = productInfoDTO.getAllCategoryLists() ;

        //根据商品编码从商品类目信息中间表取得数据
        List<ProductCategoryMidDTO> productCategoryMidDTOList = productCategoryMidService.
                getListByProductNumber(productNumber);

        if (categoryNumberLists != null && categoryNumberLists.size() > 0 && productCategoryMidDTOList != null){

            List<String> dbLists = productCategoryMidDTOList.stream().map(dto->dto.getCategoryNumber()).collect(Collectors.toList());
            List<String> tempLists = new ArrayList<>(categoryNumberLists);

            //1.页面增加的 增加
            categoryNumberLists.removeAll(dbLists) ;
            //categoryNumberLists 增加
            if(categoryNumberLists.size()> 0){
                for (String categoryNum: categoryNumberLists){
                    ProductCategoryMidDTO dtos = new ProductCategoryMidDTO() ;
                    dtos.setProductNumber(productNumber);
                    dtos.setCategoryNumber(categoryNum);
                    dtos.setCreator(productInfoDTO.getCreator());
                    dtos.setUpdater(productInfoDTO.getUpdater());
                    productCategoryMidService.saveProductCategoryMidInfo(dtos) ;
                }
            }

            //2.数据库表增加的 删除
            dbLists.removeAll(tempLists) ;
            //dbLists 删除中间表数据
            if (dbLists.size() > 0){
                for (String categoryNum: dbLists){
                    ProductCategoryMidDTO dtos = new ProductCategoryMidDTO() ;
                    dtos.setProductNumber(productNumber);
                    dtos.setCategoryNumber(categoryNum);
                    productCategoryMidService.delete(dtos) ;
                }
            }
        }

        //更新商品明细
        productDetailInfoService.update(productDetailInfoDTO) ;

        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productInfo.getProductNumber());

        //更新商品
        return productInfoMapper.update(productInfo,wrapper);
    }

    @Override
    public Integer upProductInfo(String productNumber) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productNumber);
        ProductInfo productInfo = new ProductInfo() ;
        productInfo.setProductStatus(1);
        return productInfoMapper.update(productInfo,wrapper);
    }

    @Override
    public Integer downProductInfo(String productNumber) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productNumber);
        ProductInfo productInfo = new ProductInfo() ;
        productInfo.setProductStatus(0);
        return productInfoMapper.update(productInfo,wrapper);
    }

    @Override
    @Transactional
    public Integer deleteProductInfo(String productNumber) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productNumber);
        ProductInfo productInfo = new ProductInfo() ;
        productInfo.setEnableFlag(0);

        productCategoryMidService.deleteByProductNumber(productNumber) ;

        return productInfoMapper.update(productInfo,wrapper);
    }

    @Override
    public ProductInfoDTO findByNumber(String productNumber) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productNumber);
        ProductInfo productInfo = productInfoMapper.selectOne(wrapper) ;
        ProductInfoDTO productInfoDTO = new ProductInfoDTO() ;
        CacheBeanCopier.copy(productInfo,productInfoDTO);
        return productInfoDTO;
    }
}
