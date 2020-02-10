package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.form.ProductInfoForm;
import com.sinjee.admin.service.ProductDetailInfoService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.admin.vo.ProductInfoVO;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2019 - 12 -22
 * 商品信息控制层
 * @author kweitan
 */
@RestController
@RequestMapping("/admin/product")
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
                         @RequestParam(value = "pageSize", defaultValue = "8")
                                 Integer pageSize,
                         @RequestParam(value = "selectName", defaultValue = "")
                                     String selectName){
        //1.加载页数不超过20页
        if (currentPage > 20){
            currentPage = 20 ;
        }

        if(pageSize > 10){
            pageSize = 10 ;
        }

        //2.查询数据
        IPage<ProductInfoDTO> page = productInfoService.selectProductInfosByPage(currentPage,pageSize,selectName);

        //从分页中获取List
        List<ProductInfoDTO> productInfoDTOList = page.getRecords() ;
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

        return ResultVOUtil.success(currentPage,page.getTotal(),productInfoVOList);
    }


    @CrossOrigin(origins = "*")
    @PostMapping(value = "/save")
    @CacheEvict(cacheNames = "productList", key = "productList123")
    public ResultVO saveProductInfo(@RequestBody @Valid ProductInfoForm productInfoForm, BindingResult bindingResult) {
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
        List<String> categoryList = productInfoForm.getAllCategoryLists() ;
        if (null != categoryList && categoryList.size() > 0){
            for (String content : categoryList){
                String[] arr = content.split("&") ;
                String categoryNumber = arr[0] ;
                String categoryHashNumber = arr[1] ;
                numberLists.add(categoryNumber) ;
                if(!HashUtil.verify(categoryNumber,salt,categoryHashNumber)){
                    return ResultVOUtil.error(101,"类目编码不一致!") ;
                }
            }
        }


        //3.保存商品信息
        CacheBeanCopier.copy(productInfoForm,productInfoDTO);
        CacheBeanCopier.copy(productInfoForm,productDetailInfoDTO);


        productInfoDTO.setCreator("kweitan");
        productInfoDTO.setUpdater("kweitan");
        productDetailInfoDTO.setCreator("kweitan");
        productDetailInfoDTO.setUpdater("kweitan");
        productDetailInfoDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        productDetailInfoDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));

        productInfoDTO.setAllCategoryLists(numberLists);


        Integer result = productInfoService.save(productInfoDTO,productDetailInfoDTO) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"保存商品信息失败") ;
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    @CacheEvict(cacheNames = "productList", key = "productList123")
    public ResultVO updateProductInfo(@RequestBody @Valid ProductInfoForm productInfoForm, BindingResult bindingResult){
        //1.校验参数
        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        //2.校验商品类目信息 "8493753985938948&hahhhfahghahgh"
        List<String> numberLists = new ArrayList<>() ;
        List<String> categoryList = productInfoForm.getAllCategoryLists() ;
        if (null != categoryList && categoryList.size() > 0){
            for (String content : categoryList){
                String[] arr = content.split("&") ;
                String categoryNumber = arr[0] ;
                String categoryHashNumber = arr[1] ;
                numberLists.add(categoryNumber) ;
                if(!HashUtil.verify(categoryNumber,salt,categoryHashNumber)){
                    return ResultVOUtil.error(101,"类目编码不一致!") ;
                }
            }
        }

        ProductInfoDTO productInfoDTO = new ProductInfoDTO() ;
        ProductDetailInfoDTO productDetailInfoDTO = new ProductDetailInfoDTO() ;

        CacheBeanCopier.copy(productInfoForm,productInfoDTO);
        CacheBeanCopier.copy(productInfoForm,productDetailInfoDTO);

        productInfoDTO.setAllCategoryLists(numberLists);
        productInfoDTO.setUpdater("kweitan");
        productDetailInfoDTO.setUpdater("kweitan");
        productDetailInfoDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));

        //4.更新
        Integer result = productInfoService.update(productInfoDTO,productDetailInfoDTO) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"保存商品类目失败") ;
        }

    }

    @CrossOrigin(origins = "*")
    @GetMapping("/upProductInfo")
    @CacheEvict(cacheNames = "productList", key = "productList123")
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

    @CrossOrigin(origins = "*")
    @GetMapping("/downProductInfo")
    @CacheEvict(cacheNames = "productList", key = "productList123")
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
    @CacheEvict(cacheNames = "productList", key = "productList123")
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
            CacheBeanCopier.copy(productInfoDTO,productDetailInfoDTO);
            return ResultVOUtil.success(productInfoVO);
        }else {
            return ResultVOUtil.error(101,"查找商品信息失败") ;
        }
    }

}
