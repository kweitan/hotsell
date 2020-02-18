package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.form.ProductCategoryInfoForm;
import com.sinjee.admin.form.ProductInfoForm;
import com.sinjee.admin.service.ProductCategoryMidService;
import com.sinjee.admin.service.ProductDetailInfoService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.admin.vo.ProductCategoryVO;
import com.sinjee.admin.vo.ProductDetailInfoVO;
import com.sinjee.admin.vo.ProductInfoVO;
import com.sinjee.common.*;
import com.sinjee.exceptions.MyException;
import com.sinjee.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 创建时间 2019 - 12 -22
 * 中台商品信息控制层
 * @author kweitan
 */
@RestController
@RequestMapping("/admin/product")
@Slf4j
public class ProductInfoController {

    @Autowired
    private ProductInfoService productInfoService ;

    @Autowired
    private ProductDetailInfoService productDetailInfoService ;

    @Value("${myWechat.salt}")
    private String salt ;

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "currentPage", defaultValue = "1")
                                 Integer currentPage,
                         @RequestParam(value = "pageSize", defaultValue = "10")
                                 Integer pageSize,
                         @RequestParam(value = "selectName", defaultValue = "")
                                     String selectName){
        //1.加载页数不超过20页
//        if (currentPage > 20){
//            currentPage = 20 ;
//        }
//
//        if(pageSize > 10){
//            pageSize = 10 ;
//        }

        //2.查询数据
        IPage<ProductInfoDTO> page = productInfoService.selectProductInfosByPage(currentPage,pageSize,selectName);

        //从分页中获取List
        List<ProductInfoDTO> productInfoDTOList = page.getRecords() ;
//        log.info("ProductInfoDTO={}",GsonUtil.getInstance().toStr(productInfoDTOList));
        List<ProductInfoVO> productInfoVOList = new ArrayList<>() ;
        //遍历放到productInfoVOList中
        if(null != productInfoDTOList && productInfoDTOList.size()>0){
            productInfoDTOList.stream().forEach(productInfoDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(productInfoDTO.getProductNumber(),salt);
                ProductInfoVO productInfoVO = new ProductInfoVO() ;
                CacheBeanCopier.copy(productInfoDTO,productInfoVO);
                productInfoVO.setSelectName(selectName);
                productInfoVO.setHashNumber(hashNumber);
                productInfoVOList.add(productInfoVO);
            });
        }

//        log.info("productInfoVO={}",GsonUtil.getInstance().toStr(productInfoVOList));

        return ResultVOUtil.success(currentPage,page.getTotal(),productInfoVOList);
    }


    @CrossOrigin(origins = "*")
    @PostMapping(value = "/save")
    @Caching(evict = {
            @CacheEvict(cacheNames = "productList",allEntries=true),
            @CacheEvict(cacheNames = "categoryList",allEntries=true),
            @CacheEvict(cacheNames = "productInfoDetail",allEntries=true)})
    public ResultVO saveProductInfo( @Valid @RequestBody ProductInfoForm productInfoForm, BindingResult bindingResult) {
        log.info("ProductInfoForm2={}", GsonUtil.getInstance().toStr(productInfoForm));

        //1.校验参数
        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        //校验
//        if(!HashUtil.verify(productInfoForm.getProductNumber(),salt,productInfoForm.getHashNumber())){
//            return ResultVOUtil.error(101,"商品编码不一致!") ;
//        }

        ProductInfoDTO productInfoDTO = new ProductInfoDTO() ;
        ProductDetailInfoDTO productDetailInfoDTO = new ProductDetailInfoDTO() ;

        //3.校验商品类目信息 "8493753985938948&hahhhfahghahgh"
        List<String> numberLists = new ArrayList<>() ;
        List<ProductCategoryInfoForm> categoryList = productInfoForm.getCategoryNewArrs() ;
        if (null != categoryList && categoryList.size() > 0){
            for (ProductCategoryInfoForm productCategoryInfoForm : categoryList){
                String categoryNumber = productCategoryInfoForm.getCategoryNumber() ;
                String categoryHashNumber =productCategoryInfoForm.getGoodHashNumber() ;
                numberLists.add(categoryNumber) ;
                if(!HashUtil.verify(categoryNumber,salt,categoryHashNumber)){
                    return ResultVOUtil.error(101,"类目编码不一致!") ;
                }
            }
        }

        //校验参数
        checkParams(productInfoForm);

        //生成商品编码
//        String productNumber = IdUtil.genId() ;

        //3.保存商品信息
        productInfoDTO.setProductIcon(productInfoForm.getProductIcon());
        productInfoDTO.setProductLabels(productInfoForm.getProductLabels());
        productInfoDTO.setProductName(productInfoForm.getProductName());
        productInfoDTO.setProductPrice(new BigDecimal(productInfoForm.getProductPrice()));
        productInfoDTO.setProductDescription(productInfoForm.getProductDesc());
        productInfoDTO.setProductStock(Integer.valueOf(productInfoForm.getProductStock()));
        productInfoDTO.setProductTips(productInfoForm.getProductTips());
        productInfoDTO.setProductUnit(productInfoForm.getProductUnit());
        productInfoDTO.setProductStandard(productInfoForm.getProductStandard());
//        productInfoDTO.setProductNumber(productNumber);


        //4.保存商品明细信息
        productDetailInfoDTO.setProductDetailDescription(productInfoForm.getProductDetailDesc());
        productDetailInfoDTO.setProductDetailIcon(productInfoForm.getProductDetailIcon());
        productDetailInfoDTO.setProductDetailField(productInfoForm.getProductDetailField());
//        productDetailInfoDTO.setProductNumber(productNumber);

        //保存登录用户信息
        productInfoDTO.setCreator("kweitan");
        productInfoDTO.setUpdater("kweitan");
        productDetailInfoDTO.setCreator("kweitan");
        productDetailInfoDTO.setUpdater("kweitan");

        productInfoDTO.setCreateTime(DateUtils.getTimestamp());
        productInfoDTO.setUpdateTime(DateUtils.getTimestamp());
        productDetailInfoDTO.setCreateTime(DateUtils.getTimestamp());
        productDetailInfoDTO.setUpdateTime(DateUtils.getTimestamp());

        productInfoDTO.setAllCategoryLists(numberLists);


        Integer result = productInfoService.save(productInfoDTO,productDetailInfoDTO) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"保存商品信息失败") ;
        }
    }


    private void checkParams(ProductInfoForm productInfoForm){

        if (!MathUtil.isInteger(productInfoForm.getProductStock())){
            throw new MyException(101,"库存非数字");
        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    @Caching(evict = {
            @CacheEvict(cacheNames = "productList",allEntries=true),
            @CacheEvict(cacheNames = "categoryList",allEntries=true),
            @CacheEvict(cacheNames = "productInfoDetail",allEntries=true)})
    public ResultVO updateProductInfo( @Valid @RequestBody ProductInfoForm productInfoForm, BindingResult bindingResult){

        log.info("ProductInfoForm2={}", GsonUtil.getInstance().toStr(productInfoForm));

        //1.校验参数
        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        //校验
        if(!HashUtil.verify(productInfoForm.getProductNumber(),salt,productInfoForm.getHashNumber())){
            return ResultVOUtil.error(101,"商品编码不一致!") ;
        }

        ProductInfoDTO productInfoDTO = new ProductInfoDTO() ;
        ProductDetailInfoDTO productDetailInfoDTO = new ProductDetailInfoDTO() ;

        //3.校验商品类目信息 "8493753985938948&hahhhfahghahgh"
        List<String> numberLists = new ArrayList<>() ;
        List<ProductCategoryInfoForm> categoryList = productInfoForm.getCategoryNewArrs() ;
        if (null != categoryList && categoryList.size() > 0){
            for (ProductCategoryInfoForm productCategoryInfoForm : categoryList){
                String categoryNumber = productCategoryInfoForm.getCategoryNumber() ;
                String categoryHashNumber =productCategoryInfoForm.getGoodHashNumber() ;
                numberLists.add(categoryNumber) ;
                if(!HashUtil.verify(categoryNumber,salt,categoryHashNumber)){
                    return ResultVOUtil.error(101,"类目编码不一致!") ;
                }
            }
        }

        //校验参数
        checkParams(productInfoForm);

        //3.保存商品信息
        productInfoDTO.setProductIcon(productInfoForm.getProductIcon());
        productInfoDTO.setProductLabels(productInfoForm.getProductLabels());
        productInfoDTO.setProductName(productInfoForm.getProductName());
        productInfoDTO.setProductPrice(new BigDecimal(productInfoForm.getProductPrice()));
        productInfoDTO.setProductDescription(productInfoForm.getProductDesc());
        productInfoDTO.setProductStock(Integer.valueOf(productInfoForm.getProductStock()));
        productInfoDTO.setProductTips(productInfoForm.getProductTips());
        productInfoDTO.setProductUnit(productInfoForm.getProductUnit());
        productInfoDTO.setProductStandard(productInfoForm.getProductStandard());
        productInfoDTO.setProductNumber(productInfoForm.getProductNumber());


        //4.保存商品明细信息
        productDetailInfoDTO.setProductDetailDescription(productInfoForm.getProductDetailDesc());
        productDetailInfoDTO.setProductDetailIcon(productInfoForm.getProductDetailIcon());
        productDetailInfoDTO.setProductDetailField(productInfoForm.getProductDetailField());
//        productDetailInfoDTO.setProductNumber(productNumber);

        //保存登录用户信息
        productInfoDTO.setCreator("kweitan");
        productInfoDTO.setUpdater("kweitan");
        productDetailInfoDTO.setCreator("kweitan");
        productDetailInfoDTO.setUpdater("kweitan");

        productInfoDTO.setCreateTime(DateUtils.getTimestamp());
        productInfoDTO.setUpdateTime(DateUtils.getTimestamp());
        productDetailInfoDTO.setCreateTime(DateUtils.getTimestamp());
        productDetailInfoDTO.setUpdateTime(DateUtils.getTimestamp());

        //可以用
        productDetailInfoDTO.setEnableFlag(1);
        productInfoDTO.setEnableFlag(1);

        //未上架
        productInfoDTO.setProductStatus(0);

        productInfoDTO.setAllCategoryLists(numberLists);


        Integer result = productInfoService.update(productInfoDTO,productDetailInfoDTO) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"保存商品信息失败") ;
        }


    }

    /**
     * 上架 /admin/product/upProductInfo
     * @param productNumber
     * @param hashNumber
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/upProductInfo")
    @Caching(evict = {
            @CacheEvict(cacheNames = "productList",allEntries=true),
            @CacheEvict(cacheNames = "categoryList",allEntries=true),
            @CacheEvict(cacheNames = "productInfoDetail",allEntries=true)})
    public ResultVO upProductInfo(@RequestParam String productNumber,
                               @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        Integer result =  productInfoService.upProductInfo(productNumber);
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"上架失败") ;
        }
    }

    /**
     * 下架 /admin/product/downProductInfo
     * @param productNumber
     * @param hashNumber
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/downProductInfo")
    @Caching(evict = {
            @CacheEvict(cacheNames = "productList",allEntries=true),
            @CacheEvict(cacheNames = "categoryList",allEntries=true),
            @CacheEvict(cacheNames = "productInfoDetail",allEntries=true)})
    public ResultVO downProductInfo(@RequestParam String productNumber,
                                 @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        Integer result =  productInfoService.downProductInfo(productNumber);
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"下架失败") ;
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/delete")
    @Caching(evict = {
            @CacheEvict(cacheNames = "productList",allEntries=true),
            @CacheEvict(cacheNames = "categoryList",allEntries=true),
            @CacheEvict(cacheNames = "productInfoDetail",allEntries=true)})
    public ResultVO deleteProductInfo(@RequestParam String productNumber,
                                   @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        Integer result =  productInfoService.deleteProductInfo(productNumber);
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"删除品类目失败") ;
        }

    }

    //查看明细
    @CrossOrigin(origins = "*")
    @GetMapping("/getProductByNumber")
    public ResultVO getProductByNumber(@RequestParam String productNumber,
                                        @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        ProductInfoDTO productInfoDTO = productInfoService.findByNumber(productNumber);
        ProductDetailInfoDTO productDetailInfoDTO = productDetailInfoService.findDetailByProductNumber(productNumber);
        if (null != productInfoDTO && null != productDetailInfoDTO){
            ProductInfoVO productInfoVO = new ProductInfoVO() ;
            CacheBeanCopier.copy(productInfoDTO,productInfoVO);
            CacheBeanCopier.copy(productDetailInfoDTO,productInfoVO);
            return ResultVOUtil.success(productInfoVO);
        }else {
            return ResultVOUtil.error(101,"查找商品信息失败") ;
        }
    }

    //根据商品编码 查看商品明细
    @CrossOrigin(origins = "*")
    @GetMapping("/getProductDetailInfoByNumber")
    public ResultVO getProductDetailInfoByNumber(@RequestParam String productNumber,
                                       @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        ProductDetailInfoDTO productDetailInfoDTO = productDetailInfoService.findDetailByProductNumber(productNumber);
        if (null != productDetailInfoDTO && StringUtils.isNotBlank(productDetailInfoDTO.getProductNumber())){
            ProductDetailInfoVO productDetailInfoVO = new ProductDetailInfoVO() ;
            CacheBeanCopier.copy(productDetailInfoDTO,productDetailInfoVO);
            return ResultVOUtil.success(productDetailInfoVO);
        }else {
            return ResultVOUtil.error(101,"查找商品信息失败") ;
        }
    }

    //根据产品编码 查找所属类目信息
    @CrossOrigin(origins = "*")
    @GetMapping("/getCategoryInfoByNumber")
    public ResultVO getCategoryInfoByNumber(@RequestParam String productNumber,
                                                 @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"商品编码不一致!") ;
        }
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>() ;
        List<ProductCategoryDTO>  productCategoryDTOList = productDetailInfoService.findCategoryInfoByProductNumber(productNumber);
        if (null != productCategoryDTOList && productCategoryDTOList.size()>0){
            for (ProductCategoryDTO dto: productCategoryDTOList){
                ProductCategoryVO productCategoryVO = new ProductCategoryVO() ;
                CacheBeanCopier.copy(dto,productCategoryVO);
                productCategoryVOList.add(productCategoryVO) ;
            }
            return ResultVOUtil.success(productCategoryVOList);
        }else {
            return ResultVOUtil.error(101,"查找类目信息失败") ;
        }
    }

    //根据产品编码 删除产品
    @CrossOrigin(origins = "*")
    @GetMapping("/deleteProductInfoByNumber")
    public ResultVO delete(@RequestParam String productNumber,
                                            @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"商品编码不一致!") ;
        }

        Integer res = productInfoService.deleteProductInfo(productNumber) ;
        if (res > 0){
            return ResultVOUtil.success() ;
        }else {
            return ResultVOUtil.error(101,"删除失败!") ;
        }

    }

    //根据上移 下移
    @CrossOrigin(origins = "*")
    @GetMapping("/moveProductInfo")
    public ResultVO moveProductInfo(@RequestParam String productNumber,
                           @RequestParam String hashNumber, @RequestParam Integer type){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"商品编码不一致!") ;
        }

        ProductInfoDTO productInfoDTO = productInfoService.findByNumber(productNumber) ;
        if (null == productInfoDTO || StringUtils.isBlank(productInfoDTO.getProductNumber())){
            return ResultVOUtil.error(101,"产品不存在") ;
        }else {
            Integer res = productInfoService.moveProductInfo(productInfoDTO.getProductNumber(),type,productInfoDTO.getSequenceId());
            if (res == -1){
                return ResultVOUtil.error(101,"到顶了") ;
            }else if(res == -2){
                return ResultVOUtil.error(101,"到底了") ;
            }else{
                return ResultVOUtil.success() ;
            }
        }

    }

}
