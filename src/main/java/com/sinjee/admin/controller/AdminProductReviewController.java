package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.vo.AdminProductReviewVO;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.ProductReviewDTO;
import com.sinjee.wechat.service.ProductReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2020 - 02 -24
 * 中台评论管理
 * @author kweitan
 */
@RestController
    @RequestMapping("/admin/productReview")
@Slf4j
public class AdminProductReviewController {

    @Autowired
    private ProductReviewService productReviewService ;

    @Value("${myWechat.salt}")
    private String salt ;

    /**
     * 产品评价列表
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "currentPage", defaultValue = "1")
                                    Integer currentPage,
                            @RequestParam(value = "pageSize", defaultValue = "10")
                                    Integer pageSize,
                            @RequestParam(value = "productName", defaultValue = "")
                                    String productName,
                         @RequestParam(value = "personName", defaultValue = "")
                                     String personName){

        IPage<ProductReviewDTO> page = productReviewService.findListByPage(currentPage,pageSize,productName,personName) ;

        //从分页中获取List
        List<ProductReviewDTO> productReviewDTOList = page.getRecords() ;
        List<AdminProductReviewVO> adminProductReviewVOList = new ArrayList<>() ;
        //遍历放到productInfoVOList中
        if(null != productReviewDTOList && productReviewDTOList.size()>0){
            productReviewDTOList.stream().forEach(productReviewDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(productReviewDTO.getProductNumber(),salt);
                AdminProductReviewVO adminProductReviewVO = new AdminProductReviewVO() ;
                CacheBeanCopier.copy(productReviewDTO,adminProductReviewVO);
                adminProductReviewVO.setHashNumber(hashNumber);
                adminProductReviewVOList.add(adminProductReviewVO);
            });
        }

        return ResultVOUtil.success(currentPage,page.getTotal(),adminProductReviewVOList);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/deleteProductReview")
    public ResultVO deleteProductReview(@RequestParam String productNumber,
                                        @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"数据不一致!") ;
        }

        Integer result =  productReviewService.deleteProductReview(productNumber);
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"删除评论失败") ;
        }

    }

    @CrossOrigin(origins = "*")
    @GetMapping("/modifyProductReview")
    public ResultVO modifyProductReview(@RequestParam String productNumber,
                                        @RequestParam String hashNumber,
                                        @RequestParam String reviewContent,
                                        @RequestParam Integer reviewLevel){
        //取得类目编码和哈希
        if(!HashUtil.verify(productNumber,salt,hashNumber)){
            return ResultVOUtil.error(101,"数据不一致!") ;
        }

        Integer result =  productReviewService.modifyProductReview(productNumber,reviewContent,reviewLevel);
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"修改评论失败") ;
        }

    }
}
