package com.sinjee.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.admin.dto.ProductCategoryMidDTO;
import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.entity.ProductInfo;
import com.sinjee.admin.mapper.ProductInfoMapper;
import com.sinjee.admin.service.ProductCategoryMidService;
import com.sinjee.admin.service.ProductDetailInfoService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;

import com.sinjee.common.DateUtils;
import com.sinjee.common.IdUtil;
import com.sinjee.exceptions.MyException;
import com.sinjee.wechat.form.ShopCartModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private IPage<ProductInfoDTO> returnPageByWrapper(Integer currentPage, Integer pageSize,QueryWrapper<ProductInfo> wrapper){
        Page<ProductInfo> page = new Page<ProductInfo>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<ProductInfo> mapPage = productInfoMapper.selectProductInfosByPage(page,wrapper);



        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<ProductInfo> productInfoEntityList = mapPage.getRecords() ;
        List<ProductInfoDTO> productInfoDTOList = BeanConversionUtils.copyToAnotherList(ProductInfoDTO.class,productInfoEntityList);

//        log.info("ProductInfo={}",productInfoEntityList);
//        log.info("ProductInfoDTO={}",productInfoDTOList);
        Page<ProductInfoDTO> productInfoDTOPage = new Page<>(currentPage,pageSize) ;
        productInfoDTOPage.setPages(mapPage.getPages()); //设置总页数
        productInfoDTOPage.setTotal(mapPage.getTotal()); //设置总数
        productInfoDTOPage.setRecords(productInfoDTOList) ; //设置内容
        return productInfoDTOPage;
    }

    @Override
    public IPage<ProductInfoDTO> selectProductInfosByPage(Integer currentPage, Integer pageSize,String goodsName) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).like("product_name",goodsName).orderByAsc("sequence_id");
        return returnPageByWrapper(currentPage,pageSize,wrapper);
    }

    @Override
    public IPage<ProductInfoDTO> selectProductInfosByProductStatus(Integer currentPage, Integer pageSize, Integer productStatus,String productName) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_status",productStatus).like("product_name",productName).orderByAsc("sequence_id");
        return returnPageByWrapper(currentPage,pageSize,wrapper);
    }

    @Override
    @Transactional
    public Integer saveProductInfo(ProductInfoDTO productInfoDTO) {
        ProductInfo productInfo = new ProductInfo() ;
        //拷贝属性
        CacheBeanCopier.copy(productInfoDTO,productInfo);

        productInfo.setSequenceId(findSequenceId());

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

        //设置未上架
        productInfo.setProductStatus(0);

        //保存商品中间表
        List<String> numberLists = productInfoDTO.getAllCategoryLists() ;
        for (String number: numberLists){
            ProductCategoryMidDTO productCategoryMidDTO = new ProductCategoryMidDTO() ;
            productCategoryMidDTO.setCategoryNumber(number);
            productCategoryMidDTO.setProductNumber(productNumber);
            productCategoryMidDTO.setCreator(productInfoDTO.getCreator());
            productCategoryMidDTO.setUpdater(productInfoDTO.getUpdater());

            productCategoryMidDTO.setCreateTime(DateUtils.getTimestamp());
            productCategoryMidDTO.setUpdateTime(DateUtils.getTimestamp());
            productCategoryMidService.saveProductCategoryMidInfo(productCategoryMidDTO) ;
        }

        //保存商品明细
        productDetailInfoDTO.setProductNumber(productNumber);
        productDetailInfoDTO.setEnableFlag(1);
        productDetailInfoService.save(productDetailInfoDTO);

        //设置序列号
        productInfo.setSequenceId(findSequenceId());

        //保存商品信息
        return productInfoMapper.insert(productInfo);
    }

    @Override
    @Transactional
    public Integer update(ProductInfoDTO productInfoDTO,ProductDetailInfoDTO productDetailInfoDTO) {

        ProductInfo productInfo = new ProductInfo() ;
        //拷贝属性
        CacheBeanCopier.copy(productInfoDTO,productInfo);


        //商品编码
        String productNumber = productInfoDTO.getProductNumber() ;
        productDetailInfoDTO.setProductNumber(productNumber);

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
        wrapper.eq("enable_flag",1).eq("product_number",productNumber);

        //更新商品
        return productInfoMapper.update(productInfo,wrapper);
    }

    @Override
    @Transactional
    public Integer upProductInfo(String productNumber) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productNumber).eq("product_status",0);
        ProductInfo productInfo = new ProductInfo() ;
        productInfo.setProductStatus(1);
        return productInfoMapper.update(productInfo,wrapper);
    }

    @Override
    @Transactional
    public Integer downProductInfo(String productNumber) {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("product_number",productNumber).eq("product_status",1);
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

    @Override
    public IPage<ProductInfoDTO> selectProductInfosByCategoryNumber(Integer currentPage, Integer pageSize, String categoryNumber) {
        log.info("currentPage={},pageSize={}",currentPage,pageSize);
        Page<ProductInfo> page = new Page<ProductInfo>(currentPage,pageSize) ;
        Map<String,Object> map = new HashMap<>() ;
        map.put("categoryNumber",categoryNumber);
        //从数据库分页获取数据
        IPage<ProductInfo> curPage = productInfoMapper.selectProductInfosByCategoryNumber(page,map);
        log.info("总页数"+curPage.getPages());
        log.info("总记录数"+curPage.getTotal());
        List<ProductInfo> productInfoEntityList = curPage.getRecords() ;
        List<ProductInfoDTO> productInfoDTOList = BeanConversionUtils.copyToAnotherList(ProductInfoDTO.class,productInfoEntityList);

        Page<ProductInfoDTO> productInfoDTOPage = new Page<>(currentPage,pageSize) ;
        productInfoDTOPage.setPages(curPage.getPages()); //设置总页数
        productInfoDTOPage.setTotal(curPage.getTotal()); //设置总数
        productInfoDTOPage.setRecords(productInfoDTOList) ; //设置内容
        return productInfoDTOPage;
    }

    @Override
    public List<ProductInfoDTO> getList() {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).orderByAsc("sequence_id");
        List<ProductInfo> productInfoList = productInfoMapper.selectList(wrapper);
        return BeanConversionUtils.copyToAnotherList(ProductInfoDTO.class,productInfoList);
    }

    @Override
    @Transactional
    public void increaseStock(List<ShopCartModel> shopCartModelList) {
        for (ShopCartModel shopCartModel: shopCartModelList) {
            ProductInfo productInfo = productInfoMapper.selectProductByNumber(shopCartModel.getProductNumber());
            if (productInfo == null || StringUtils.isBlank(productInfo.getProductNumber())) {
                throw new MyException(255,"商品不存在");
            }

            Map<String, Object> params = new HashMap<>() ;
            params.put("productNumber",shopCartModel.getProductNumber());
            params.put("stock",Integer.valueOf(shopCartModel.getProductCount()));

            //库存退回
            Integer res = productInfoMapper.increase(params);
            if (res <= 0){
                throw new MyException(256,shopCartModel.getProductNumber()+"商品库存退回出错");
            }
//            Integer result = productInfo.getProductStock() + Integer.valueOf(shopCartModel.getProductCount());
//            productInfo.setProductStock(result);
//            productInfoMapper.updateProductInfo(productInfo);
        }

    }

    /**
     * 预扣库存
     * @param shopCartModelList
     */
    @Override
    @Transactional
    public void decreaseStock(List<ShopCartModel> shopCartModelList) {
        for (ShopCartModel shopCartModel: shopCartModelList) {

            ProductInfo productInfo = productInfoMapper.selectProductByNumber(shopCartModel.getProductNumber());
            if (productInfo == null || StringUtils.isBlank(productInfo.getProductNumber())) {
                throw new MyException(255,"商品不存在");
            }


//            Integer r = Integer.valueOf(shopCartModel.getProductCount()) ;
            Map<String, Object> params = new HashMap<>() ;
            params.put("productNumber",shopCartModel.getProductNumber());
            params.put("stock",Integer.valueOf(shopCartModel.getProductCount()));

            //预扣库存
            Integer res = productInfoMapper.decrease(params);
            if (res <= 0){
                throw new MyException(256,shopCartModel.getProductNumber()+"商品库存不足");
            }


//            ProductInfo productInfo = productInfoMapper.selectProductByNumber(shopCartModel.getProductNumber());
//            if (productInfo == null || StringUtils.isBlank(productInfo.getProductNumber())) {
//                throw new MyException(255,"商品不存在");
//            }
//
//            Integer result = productInfo.getProductStock() - Integer.valueOf(shopCartModel.getProductCount());
//            if (result < 0) {
//                throw new MyException(256,"商品库存不对");
//            }
//
//            productInfo.setProductStock(result);
//
//            QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
//            wrapper.eq("enable_flag",1).eq("product_number",productInfo.getProductNumber());
//
//            productInfoMapper.update(productInfo,wrapper);
        }
    }

    @Override
    public Integer moveProductInfo(String productNumber, Integer type,Integer sequenceId) {
        Map<String,Object> map = new HashMap<>() ;
        map.put("sequenceId",sequenceId) ;
        map.put("type",type);
//        map.put("productNumber",productNumber) ;
        ProductInfo productInfo = productInfoMapper.selectMoveProductInfo(map) ;
        if (null == productInfo && type == 1){
            return -1 ; //-1表示 到顶了
        }else if(null == productInfo && type == 0){
            return -2 ; //-1表示 到底了
        }

        QueryWrapper<ProductInfo> oldWrapper = new QueryWrapper();
        oldWrapper.eq("enable_flag",1).eq("product_number",productNumber);
        ProductInfo oldProductInfo = new ProductInfo();
        oldProductInfo.setSequenceId(productInfo.getSequenceId());
        Integer res1 = productInfoMapper.update(oldProductInfo,oldWrapper);

        QueryWrapper<ProductInfo> newWrapper = new QueryWrapper();
        newWrapper.eq("enable_flag",1).eq("product_number",productInfo.getProductNumber());
        ProductInfo newProductInfo = new ProductInfo();
        newProductInfo.setSequenceId(sequenceId);
        Integer res2 = productInfoMapper.update(newProductInfo,newWrapper) ;
        if (res1 > 0 && res2 >0){
            return 1 ; //成功
        }else {
            //回滚事务
            throw new MyException(101,"上下移动更新失败") ;
        }

    }

    @Override
    public Integer findSequenceId() {
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).orderByDesc("sequence_id");
        List<ProductInfo> productInfoList = productInfoMapper.selectList(wrapper) ;
        if (null == productInfoList || productInfoList.size() == 0){
            return 1 ;
        }

        return productInfoList.get(0).getSequenceId() + 1;
    }
}
