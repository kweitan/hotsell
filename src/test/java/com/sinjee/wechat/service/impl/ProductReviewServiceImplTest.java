package com.sinjee.wechat.service.impl;

import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.dto.ProductInfoDTO;
import com.sinjee.admin.service.ProductDetailInfoService;
import com.sinjee.admin.service.ProductInfoService;
import com.sinjee.common.GsonUtil;
import com.sinjee.wechat.dto.ProductReviewDTO;
import com.sinjee.wechat.service.ProductReviewService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class ProductReviewServiceImplTest {

    @Autowired
    private ProductInfoService productInfoService ;

    @Autowired
    private ProductDetailInfoService productDetailInfoService ;

    @Autowired
    private ProductReviewService productReviewService ;

    @Test
    void save() {

        List<ProductInfoDTO> productInfoDTOList = productInfoService.getList() ;
        if (productInfoDTOList != null && productInfoDTOList.size()>0){
            productInfoDTOList.stream().forEach(productInfoDTO -> {
                String productNumber = productInfoDTO.getProductNumber();
                ProductDetailInfoDTO productDetailInfoDTO = new ProductDetailInfoDTO() ;
                productDetailInfoDTO.setProductDetailDescription("商品详情XXXXXXX");
                productDetailInfoDTO.setProductNumber(productNumber);
                Map<String,Object> map = new HashMap<>() ;
                map.put("品牌","SINJEE");
                map.put("产地","广东");
                map.put("存储条件","冷冻");
                productDetailInfoDTO.setProductDetailField(GsonUtil.getInstance().toStr(map));
                productDetailInfoDTO.setProductDetailIcon("/pages/images/banner01.jpg&/pages/images/banner02.jpg&/pages/images/banner03.jpg");
                productDetailInfoDTO.setCreator("kweitan");
                productDetailInfoDTO.setUpdater("kweitan");
                productDetailInfoDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
                productDetailInfoDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
                productDetailInfoService.save(productDetailInfoDTO);

                for (int i=0;i<101;i++){
                    ProductReviewDTO productReviewDTO = new ProductReviewDTO() ;
                    productReviewDTO.setBuyerReviewId(1);
                    productReviewDTO.setPersonIcon("/pages/images/huagua.jpg");
                    productReviewDTO.setPersonName("kweitan");
                    productReviewDTO.setProductNumber(productNumber);
                    productReviewDTO.setProductReviewLevel(1);
                    productReviewDTO.setProductReviewContent("好吃，好香！！！！");
                    productReviewDTO.setCreator("kweitan");
                    productReviewDTO.setUpdater("kweitan");
                    productReviewDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
                    productReviewDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
                    productReviewService.save(productReviewDTO);
                }

            });
        }
    }

    @Test
    void update() {
    }

    @Test
    void selectProductReviewByPage() {
    }

    @Test
    void delete() {
    }

    @Test
    void selectProductReviewByPageProductNumber() {
    }

    @Test
    void selecOne() {
    }

    @Test
    void productReviewCount() {
    }
}