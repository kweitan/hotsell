package com.sinjee.admin.service.impl;

import com.sinjee.admin.dto.ProductDetailInfoDTO;
import com.sinjee.admin.service.ProductDetailInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class ProductDetailInfoServiceImplTest {

    @Autowired
    ProductDetailInfoService productDetailInfoService ;

    @Test
    void save() {
        ProductDetailInfoDTO productDetailInfoDTO = new ProductDetailInfoDTO() ;
        productDetailInfoDTO.setProductNumber("56856454545");
        productDetailInfoDTO.setProductDetailField("55656565665655555");
        productDetailInfoDTO.setProductDetailIcon("xxx.jpg;ejwejsjf.jpg");
        productDetailInfoDTO.setProductDetailDescription("hahhahahhahahahha");
        productDetailInfoDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        productDetailInfoDTO.setCreator("kweitan");
        productDetailInfoDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        productDetailInfoDTO.setUpdater("kweitan");
        productDetailInfoDTO.setEnableFlag(1);
        productDetailInfoService.save(productDetailInfoDTO);
    }

    @Test
    void findDetailByProductNumber() {
        productDetailInfoService.findDetailByProductNumber("56856454545");
    }

    @Test
    void update(){
        ProductDetailInfoDTO productDetailInfoDTO = new ProductDetailInfoDTO() ;
        productDetailInfoDTO.setProductNumber("56856454545");
        productDetailInfoDTO.setProductDetailDescription("hello");
        productDetailInfoService.update(productDetailInfoDTO) ;
    }
}