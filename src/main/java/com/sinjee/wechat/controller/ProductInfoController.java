package com.sinjee.wechat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.vo.ProductInfoVO;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.ProductInfoDTO;
import com.sinjee.wechat.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2019 - 12 -22
 * 商品信息控制层
 * @author kweitan
 */
@RestController
@RequestMapping("/wechat/api")
public class ProductInfoController {

    @Autowired
    private ProductInfoService productInfoService ;

    @Value("${myWechat.salt}")
    private String salt ;

    /***
     * 微信小程序前端获取 商品信息列表
     * @return
     */
    @GetMapping("/getProductInfosByPage")
    public ResultVO getProductInfosByPage(@RequestParam(value = "currentPage", defaultValue = "0")
                                                      Integer currentPage){

        Integer productStatus = 0 ; //1-表示已上架
        Integer pageSize = 6 ; //默认最大页数是6

        //最大到1000页
        if (currentPage > 1000){
            currentPage = 1000 ;
        }

        //分页获取上架的商品
        IPage<ProductInfoDTO> page = productInfoService.selectProductInfosByPage
                (currentPage,pageSize,productStatus) ;

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
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        return resultVO;

    }

    /**根据商品编号获取商品详细信息**/

}
