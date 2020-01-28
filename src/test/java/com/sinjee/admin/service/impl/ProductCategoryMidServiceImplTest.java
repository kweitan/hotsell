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
    void save(){
        ProductCategoryMidDTO productCategoryMidDTO = new ProductCategoryMidDTO() ;
        productCategoryMidDTO.setCategoryNumber("1328091225915424");
        productCategoryMidDTO.setCreator("kweitan");
        productCategoryMidDTO.setUpdater("kweitan");
        productCategoryMidDTO.setProductNumber("00001327481590120672");
        productCategoryMidDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        productCategoryMidDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        productCategoryMidService.save(productCategoryMidDTO) ;
    }

    @Test
    void saveProductCategoryMidInfo() {
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

    @Test
    void saveProductCategoryMidInfo1() {
    }

    @Test
    void updateProductCategoryMidInfo1() {
    }

    @Test
    void invalidProductCategoryMidInfo1() {
    }

    @Test
    void selectProductCategoryMidInfoByPage1() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getListByProductNumber() {
    }

    @Test
    void getListByProductCategoryNumber() {
    }

    @Test
    void deleteByProductNumber() {
    }

    @Test
    void deleteByCategoryNumber() {
    }
}