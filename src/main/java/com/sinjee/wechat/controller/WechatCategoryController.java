package com.sinjee.wechat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.service.ProductCategoryService;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.WechatSearchKeywordDTO;
import com.sinjee.wechat.service.WechatSearchKeywordService;
import com.sinjee.wechat.vo.CategoryIndexVO;
import com.sinjee.wechat.vo.CategoryInfoVO;
import com.sinjee.wechat.vo.WechatSearchKeywordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2020 - 01 -25
 * 微信小程序首页分类
 * @author kweitan
 */
@RestController
@RequestMapping("wechat/category")
public class WechatCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService ;

    @Autowired
    private WechatSearchKeywordService wechatSearchKeywordService ;

    @Value("${myWechat.salt}")
    private String salt ;

    @CrossOrigin(origins = "*")
    @GetMapping("/searchKeyword/list")
    public ResultVO searchKeywordList(){

        List<WechatSearchKeywordDTO> wechatSearchKeywordDTOList = wechatSearchKeywordService.getList();
        List<WechatSearchKeywordVO> wechatSearchKeywordVOList = new ArrayList<>() ;
        if (null != wechatSearchKeywordDTOList && wechatSearchKeywordDTOList.size()>0){
            wechatSearchKeywordDTOList.stream().forEach(wechatSearchKeywordDTO -> {
                WechatSearchKeywordVO wechatSearchKeywordVO = new WechatSearchKeywordVO() ;
                wechatSearchKeywordVO.setSearchKeywordName(wechatSearchKeywordDTO.getSearchKeywordName());
                wechatSearchKeywordVO.setSearchKeywordNumber(wechatSearchKeywordDTO.getSearchKeywordNumber());
                wechatSearchKeywordVOList.add(wechatSearchKeywordVO) ;
            });
        }
        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(wechatSearchKeywordVOList);
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public ResultVO list(){

        //2.查询数据
        Integer productStatus = 0 ; //1-表示已上架 0-表示下架

        IPage<ProductCategoryDTO> page = productCategoryService.selectProductCategoryBySearchName(1,5,"");

        //从分页中获取List
        List<ProductCategoryDTO> productCategoryList = page.getRecords() ;
        List<CategoryInfoVO> categoryInfoVOList = new ArrayList<>() ;
        if(null != productCategoryList && productCategoryList.size()>0){
            productCategoryList.stream().forEach(productCategoryDTO -> {
                CategoryInfoVO categoryInfoVO = new CategoryInfoVO() ;
                CacheBeanCopier.copy(productCategoryDTO,categoryInfoVO);
                categoryInfoVOList.add(categoryInfoVO);
            });
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(categoryInfoVOList);
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/indexList")
    public ResultVO index(){

        //2.查询数据
        Integer productStatus = 0 ; //1-表示已上架 0-表示下架

        IPage<ProductCategoryDTO> page = productCategoryService.selectProductCategoryBySearchName(1,5,"");

        //从分页中获取List
        List<ProductCategoryDTO> productCategoryList = page.getRecords() ;
        List<CategoryIndexVO> productCategoryVOList = new ArrayList<>() ;
        if(null != productCategoryList && productCategoryList.size()>0){
            productCategoryList.stream().forEach(productCategoryDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(productCategoryDTO.getCategoryNumber(),salt);
                CategoryIndexVO productCategoryVO = new CategoryIndexVO() ;
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
