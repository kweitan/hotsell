package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.form.DeleteAllProductForm;
import com.sinjee.admin.form.ProductCategoryForm;
import com.sinjee.admin.service.ProductCategoryService;
import com.sinjee.admin.vo.ProductCategoryVO;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小小极客
 * 时间 2020/1/5 10:41
 * @ClassName CategoryInfoController
 * 描述 商品类目控制层
 **/
@RestController
@RequestMapping("/admin/productCategory")
public class CategoryInfoController {

    @Autowired
    private ProductCategoryService productCategoryService ;

    @Value("${myWechat.salt}")
    private String salt ;

    @CrossOrigin(origins = "*")
    @GetMapping("/getCategoryByNumber")
    public ResultVO getCategoryByNumber(@RequestParam String categoryNumber,
                         @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(categoryNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        ProductCategoryDTO productCategoryDTO = productCategoryService.getProductCategoryDTOByNumber(categoryNumber);
        if (null != productCategoryDTO){
            return ResultVOUtil.success(productCategoryDTO);
        }else {
            return ResultVOUtil.error(101,"查找商品类目失败") ;
        }
    }

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
        Integer productStatus = 1 ; //1-表示已上架 0-表示下架

        IPage<ProductCategoryDTO> page = productCategoryService.selectProductCategoryInfoByPage(currentPage,pageSize,selectName);

        //从分页中获取List
        List<ProductCategoryDTO> productCategoryDTOList = page.getRecords() ;
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>() ;
        //遍历放到productInfoVOList中
        if(null != productCategoryDTOList && productCategoryDTOList.size()>0){
            productCategoryDTOList.stream().forEach(productCategoryDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(productCategoryDTO.getCategoryNumber()+"",salt);
                ProductCategoryVO productCategoryVO = new ProductCategoryVO() ;
                CacheBeanCopier.copy(productCategoryDTO,productCategoryVO);
                productCategoryVO.setHashNumber(hashNumber);
                productCategoryVOList.add(productCategoryVO);
            });
        }

        return ResultVOUtil.success(currentPage,page.getTotal(),productCategoryVOList);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/listAll")
    public ResultVO listAll(){

        List<ProductCategoryDTO> productCategoryDTOList = productCategoryService.getAllProductCategoryDTOList() ;
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>() ;
        //遍历放到productInfoVOList中
        if(null != productCategoryDTOList && productCategoryDTOList.size()>0){
            productCategoryDTOList.stream().forEach(productCategoryDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(productCategoryDTO.getCategoryId()+"",salt);
                ProductCategoryVO productCategoryVO = new ProductCategoryVO() ;
                CacheBeanCopier.copy(productCategoryDTO,productCategoryVO);
                productCategoryVO.setHashNumber(hashNumber);
                productCategoryVOList.add(productCategoryVO);
            });
        }

        return ResultVOUtil.success(productCategoryVOList);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/save")
    @CacheEvict(cacheNames = "indexList",allEntries=true)
    public ResultVO saveCategory(@RequestBody @Valid ProductCategoryForm productCategoryForm, BindingResult bindingResult){
        //1.校验参数
        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO() ;

        //校验参数
        if (StringUtils.isNotBlank(productCategoryForm.getBelongIndex()) &&
                MathUtil.isInteger(productCategoryForm.getBelongIndex())){
            productCategoryDTO.setBelongIndex(Integer.valueOf(productCategoryForm.getBelongIndex()));
        }

        Integer res = productCategoryService.countIndexNumber() ;
        if (res >= 5){
            return ResultVOUtil.error(101,"首页数据只能5条") ;
        }

        productCategoryDTO.setCategoryName(productCategoryForm.getCategoryName());
        productCategoryDTO.setCategoryIcon(productCategoryForm.getCategoryIcon());


        //2.取出当前用户
        productCategoryDTO.setCreator("kweitan");
        productCategoryDTO.setUpdater("kweitan");
        productCategoryDTO.setCreateTime(DateUtils.getTimestamp());
        productCategoryDTO.setUpdateTime(DateUtils.getTimestamp());

        //3.生成商品类目编码
        productCategoryDTO.setCategoryNumber(IdUtil.nextId()+"");
        //默认上架状态
        productCategoryDTO.setCategoryStatus(1);

        //4.保存
        Integer result = productCategoryService.saveProductCategoryInfo(productCategoryDTO) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"保存商品类目失败") ;
        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    @CacheEvict(cacheNames = "indexList",allEntries=true)
    public ResultVO updateCategory(@RequestBody @Valid ProductCategoryForm productCategoryForm, BindingResult bindingResult){
        //1.校验参数
        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        //2.取得类目编码和哈希
        if(!HashUtil.verify(productCategoryForm.getCategoryNumber(),salt,productCategoryForm.getHashNumber())){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        //3.取出当前用户
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO() ;
        productCategoryDTO.setUpdater("kweitan");

        productCategoryDTO.setCategoryName(productCategoryForm.getCategoryName());
        productCategoryDTO.setCategoryNumber(productCategoryForm.getCategoryNumber());
        productCategoryDTO.setCategoryStatus(productCategoryForm.getCategoryStatus());

        //4.更新
        Integer result = productCategoryService.updateProductCategoryInfo(productCategoryDTO) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"保存商品类目失败") ;
        }

    }

    @CrossOrigin(origins = "*")
    @GetMapping("/upCategory")
    public ResultVO upCategory(@RequestParam String categoryNumber,
                                   @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(categoryNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        Integer result =  productCategoryService.upCategoryInfo(categoryNumber);
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"上架失败") ;
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/downCategory")
    public ResultVO downCategory(@RequestParam String categoryNumber,
                               @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(categoryNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        Integer result =  productCategoryService.upCategoryInfo(categoryNumber);
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"下架失败") ;
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/delete")
    @CacheEvict(cacheNames = "indexList",allEntries=true)
    public ResultVO deleteCategory(@RequestParam String categoryNumber,
                                   @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(categoryNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

        Integer result =  productCategoryService.invalidProductCategoryInfo(categoryNumber);
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"删除品类目失败") ;
        }

    }


    @CrossOrigin(origins = "*")
    @PostMapping("/deleteAll")
    public ResultVO deleteAllCategory(@RequestBody DeleteAllProductForm deleteAllProductForm){

        boolean veritify = false;

        List<String> numberLists = new ArrayList<>() ;

        if(null != deleteAllProductForm && deleteAllProductForm.getAllCategoryLists() != null
                && deleteAllProductForm.getAllCategoryLists().size()>0){
            for (ProductCategoryForm productCategoryForm : deleteAllProductForm.getAllCategoryLists()){
                if(!HashUtil.verify(productCategoryForm.getCategoryNumber(),salt,productCategoryForm.getHashNumber())){
                    veritify = true ;
                    break;
                }else {
                    numberLists.add(productCategoryForm.getCategoryNumber()) ;
                }

            }
        }else {
            return ResultVOUtil.error(101,"参数为空!") ;
        }

        List<String> successLists = new ArrayList<>() ;
        List<String> errorLists = new ArrayList<>() ;

        if (!veritify){
            if (numberLists.size() > 0){
                for (String categoryNumber: numberLists){
                    Integer result = productCategoryService.invalidProductCategoryInfo(categoryNumber);
                    if (result > 0){
                        successLists.add(categoryNumber) ;
                    }else {
                        errorLists.add(categoryNumber) ;
                    }
                }
            }
            if (successLists.size() > 0){
                String str = "成功有："+successLists.size()+"次;失败有："+ errorLists.size()+"次！";
                return ResultVOUtil.success(str);
            }else{
                return ResultVOUtil.error(101,"更新失败!") ;
            }

        }else {
            return ResultVOUtil.error(101,"类目编码不一致!") ;
        }

    }

    //判断是否只有5条首页数据
    @CrossOrigin(origins = "*")
    @GetMapping("/countIndex")
    public ResultVO countIndex(){
        Integer res = productCategoryService.countIndexNumber() ;
        Map<String,Object> map = new HashMap<>() ;
        if (res >= 5){
            return ResultVOUtil.error(101,"首页数据只能5条") ;
        }else {
            return ResultVOUtil.success() ;
        }
    }
}
