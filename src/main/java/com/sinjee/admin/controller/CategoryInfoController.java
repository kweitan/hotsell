package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.service.ProductCategoryService;
import com.sinjee.admin.vo.ProductCategoryVO;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "currentPage", defaultValue = "1")
                                 Integer currentPage,
                         @RequestParam(value = "pageSize", defaultValue = "8")
                                 Integer pageSize){
        //1.加载页数不超过20页
        if (currentPage > 20){
            currentPage = 20 ;
        }

        if(pageSize > 10){
            pageSize = 10 ;
        }

        //2.查询数据
        Integer productStatus = 0 ; //1-表示已上架 0-表示下架

        IPage<ProductCategoryDTO> page = productCategoryService.selectProductCategoryInfoByPage(currentPage,pageSize);

        //从分页中获取List
        List<ProductCategoryDTO> productCategoryDTOList = page.getRecords() ;
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

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(productCategoryVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setTotalSize(page.getTotal());
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO;
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

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(productCategoryVOList);
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO;
    }
}
