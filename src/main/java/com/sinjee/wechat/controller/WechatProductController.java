package com.sinjee.wechat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.service.ProductCategoryMidService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.vo.ProductInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2020 - 01 -09
 * 微信小程序产品服务控制层
 * @author kweitan
 */
@RestController
@RequestMapping("wechat/product")
public class WechatProductController {

    @Autowired
    private ProductInfoService productInfoService ;

    @Value("${myWechat.salt}")
    private String salt ;

    @CrossOrigin(origins = "*")
    @GetMapping("/number/list")
    public ResultVO listByProductNumber(@RequestParam(value = "currentPage", defaultValue = "1")
                                 Integer currentPage,
                         @RequestParam(value = "pageSize", defaultValue = "8")
                                 Integer pageSize,@RequestParam String categoryNumber){

        IPage<ProductInfoDTO> productInfoDTOIPage = productInfoService.
                selectProductInfosByCategoryNumber(1,30,categoryNumber);
        List<ProductInfoVO> productInfoVOList = new ArrayList<>() ;
        if (null != productInfoDTOIPage){
            List<ProductInfoDTO> productInfoDTOList = productInfoDTOIPage.getRecords();
            if(null != productInfoDTOList && productInfoDTOList.size()>0){
                productInfoDTOList.stream().forEach(productInfoDTO -> {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    CacheBeanCopier.copy(productInfoDTO,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                });
            }
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(productInfoVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        return resultVO;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "currentPage", defaultValue = "1")
                                     Integer currentPage,
                         @RequestParam(value = "pageSize", defaultValue = "6")
                                 Integer pageSize,
                         @RequestParam(value = "productName", defaultValue = "")
                                     String productName){
        //1.加载页数不超过20页
        if (currentPage > 20){
            currentPage = 20 ;
        }

        if(pageSize > 10){
            pageSize = 10 ;
        }

        if(StringUtils.isBlank(productName)){
            productName = "" ;
        }
        //2.查询数据
        Integer productStatus = 0 ; //1-表示已上架 0-表示下架

        IPage<ProductInfoDTO> page = productInfoService.
                selectProductInfosByProductStatus(currentPage,pageSize,productStatus,productName);

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
                productInfoVO.setHashNumber(hashNumber);
                productInfoVOList.add(productInfoVO);
            });
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(productInfoVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setTotalSize(page.getTotal());
        resultVO.setPageTotal(page.getPages());
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO;
    }
}
