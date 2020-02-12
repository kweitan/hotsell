package com.sinjee.wechat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.service.ProductDetailInfoService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.ProductReviewDTO;
import com.sinjee.wechat.service.ProductReviewService;
import com.sinjee.wechat.vo.ProductDetailInfoVO;
import com.sinjee.wechat.vo.ProductInfoVO;
import com.sinjee.wechat.vo.WechatProductReviewVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    private ProductDetailInfoService productDetailInfoService ;

    @Autowired
    private ProductReviewService productReviewService ;

    @Value("${myWechat.salt}")
    private String salt ;

    @CrossOrigin(origins = "*")
    @GetMapping("/review/productNumber")
    public ResultVO getProductReviewByProductNumber(@RequestParam(value = "currentPage", defaultValue = "1")
                                                              Integer currentPage,
                                                  @RequestParam(value = "pageSize", defaultValue = "8")
                                                              Integer pageSize,@RequestParam String productNumber){
        //1.加载页数不超过20页
        if (currentPage > 20){
            currentPage = 20 ;
        }

        if(pageSize > 10){
            pageSize = 10 ;
        }

        IPage<ProductReviewDTO> productReviewDTOIPage = productReviewService.selectProductReviewByPageProductNumber(currentPage,pageSize,productNumber);
        List<ProductReviewDTO> productReviewDTOList = productReviewDTOIPage.getRecords() ;
        List<WechatProductReviewVO> wechatProductReviewVOList = new ArrayList<>() ;
        if(null != productReviewDTOList && productReviewDTOList.size()>0){
            productReviewDTOList.stream().forEach(productReviewDTO-> {
                WechatProductReviewVO wechatProductReviewVO = new WechatProductReviewVO() ;
                CacheBeanCopier.copy(productReviewDTO, wechatProductReviewVO);
                wechatProductReviewVOList.add(wechatProductReviewVO);
            });
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(wechatProductReviewVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setTotalSize(productReviewDTOIPage.getTotal());
        resultVO.setPageTotal(productReviewDTOIPage.getPages());
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        return resultVO ;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/product/productNumber")
    public ResultVO getProductInfoByProductNumber(@RequestParam String productNumber){
        ProductInfoDTO productInfoDTO = productInfoService.findByNumber(productNumber);
        ProductInfoVO productInfoVO = new ProductInfoVO() ;
        CacheBeanCopier.copy(productInfoDTO,productInfoVO);
        return ResultVOUtil.success(productInfoVO);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/number/list")
    @Cacheable(cacheNames = "categoryList", key = "#currentPage+'-'+#pageSize+'-'+#categoryNumber", unless = "#result.getCode() != 0")
    public ResultVO listByProductNumber(@RequestParam(value = "currentPage", defaultValue = "1")
                                 Integer currentPage,
                         @RequestParam(value = "pageSize", defaultValue = "8")
                                 Integer pageSize,@RequestParam String categoryNumber){

        if(null == categoryNumber || "undefined".equals(categoryNumber)){
            return ResultVOUtil.error(123,"类别编码无效");
        }

        IPage<ProductInfoDTO> productInfoDTOIPage = productInfoService.
                selectProductInfosByCategoryNumber(currentPage,pageSize,categoryNumber);
        List<ProductInfoVO> productInfoVOList = new ArrayList<>() ;
        if (null != productInfoDTOIPage){
            List<ProductInfoDTO> productInfoDTOList = productInfoDTOIPage.getRecords();
            if(null != productInfoDTOList && productInfoDTOList.size()>0){
                productInfoDTOList.stream().forEach(productInfoDTO -> {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    String hashNumber = HashUtil.sign(productInfoDTO.getProductNumber(),salt) ;
                    CacheBeanCopier.copy(productInfoDTO,productInfoVO);
                    productInfoVO.setHashNumber(hashNumber);
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
        resultVO.setTotalSize(productInfoDTOIPage.getTotal());
        resultVO.setPageTotal(productInfoDTOIPage.getPages());
        return resultVO;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/detailByProductNumber")
    @Cacheable(cacheNames = "productInfoDetail", key = "#productNumber", unless = "#result.getCode() != 0")
    public ResultVO detailByProductNumber(@RequestParam String productNumber){
        ProductDetailInfoVO productDetailInfoVO = new ProductDetailInfoVO() ;
        ProductInfoDTO productInfoDTO = productInfoService.findByNumber(productNumber) ;
        CacheBeanCopier.copy(productInfoDTO,productDetailInfoVO);

        ProductReviewDTO productReviewDTO = productReviewService.selecOne(productNumber);
        CacheBeanCopier.copy(productReviewDTO,productDetailInfoVO);
        //获取评论数量
        Integer count = productReviewService.productReviewCount(productNumber) ;
        productDetailInfoVO.setProductReviewCount(count);

        ProductDetailInfoDTO productDetailInfoDTO = productDetailInfoService.findDetailByProductNumber(productNumber) ;
        CacheBeanCopier.copy(productDetailInfoDTO,productDetailInfoVO);
        String hashNumber = HashUtil.sign(productInfoDTO.getProductNumber(),salt);
        productDetailInfoVO.setHashNumber(hashNumber);

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(productDetailInfoVO);
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    @Cacheable(cacheNames = "productList", key = "#currentPage+'-'+#pageSize",unless = "#result.getCode() != 0" )
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
