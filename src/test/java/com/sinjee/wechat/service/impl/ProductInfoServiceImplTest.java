package com.sinjee.wechat.service.impl;

import com.sinjee.common.IdUtil;
import com.sinjee.wechat.dto.ProductInfoDTO;
import com.sinjee.wechat.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoService productInfoServiceImpl ;

    @Test
    void selectProductInfosByProductStatus(){
        productInfoServiceImpl.selectProductInfosByProductStatus(0,3,0);
    }

    @Test
    void selectProductInfosByPage() {
        productInfoServiceImpl.selectProductInfosByPage(0,10,0);
    }

    @Test
    void saveProductInfo() {
        ProductInfoDTO productInfoDTO = new ProductInfoDTO() ;
        productInfoDTO.setProductName("展翔 潮汕手打牛肉丸 500g");
        productInfoDTO.setProductDescription("精选黄牛后腿肉，汕头传统制丸工艺制成，鲜香弹牙，筋道十足");
        productInfoDTO.setProductPrice(new BigDecimal(16.8));
        productInfoDTO.setProductIcon("xxxxx.jpg");
        productInfoDTO.setCategoryNumber("1111"+IdUtil.nextId());
        productInfoDTO.setProductLabels("好吃,推荐");
        productInfoDTO.setProductStandard("3包");
        productInfoDTO.setProductStock(1000);
        productInfoDTO.setProductUnit("盒");
        productInfoDTO.setProductNumber("0000"+ IdUtil.nextId());
        productInfoDTO.setCreator("kweitan");
        productInfoDTO.setUpdater("kweitan");
        productInfoDTO.setProductTips("爆款");
        productInfoServiceImpl.saveProductInfo(productInfoDTO) ;
    }
}