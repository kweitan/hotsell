package com.sinjee.admin.service.impl;

import com.sinjee.admin.dto.ProductCategoryMidDTO;
import com.sinjee.admin.service.ProductCategoryMidService;
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
class ProductCategoryMidServiceImplTest {

    @Autowired
    ProductCategoryMidService productCategoryMidService ;

    @Test
    void saveProductCategoryMidInfo() {
        ProductCategoryMidDTO productCategoryMidDTO = new ProductCategoryMidDTO() ;
        productCategoryMidDTO.setCategoryNumber("455445455555");
        productCategoryMidDTO.setCreator("kweitan");
        productCategoryMidDTO.setUpdater("kweitan");
        productCategoryMidDTO.setProductNumber("5656565555");
        productCategoryMidService.saveProductCategoryMidInfo(productCategoryMidDTO) ;
    }

    @Test
    void updateProductCategoryMidInfo() {
    }

    @Test
    void invalidProductCategoryMidInfo() {
    }

    @Test
    void selectProductCategoryMidInfoByPage() {
    }
}