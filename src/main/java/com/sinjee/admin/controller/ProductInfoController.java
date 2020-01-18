package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.admin.vo.ProductInfoVO;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

        IPage<ProductInfoDTO> page = productInfoService.selectProductInfosByPage(currentPage,pageSize,productStatus);

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
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO;
    }

    /**根据商品编号获取商品详细信息**/

}
