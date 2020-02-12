package com.sinjee.admin.service.impl;

import com.sinjee.admin.dto.ProductCategoryDTO;
import com.sinjee.admin.service.ProductCategoryService;
import com.sinjee.common.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class ProductCategoryServiceImplTest {

    @Autowired
    private ProductCategoryService productCategoryService ;

    @Test
    void saveProductCategoryInfo() {

    }

    @Test
    void updateProductCategoryInfo() {
    }

    @Test
    void invalidProductCategoryInfo() {
    }

    @Test
    void selectProductCategoryInfoByPage() {
    }

    @Test
    void getAllProductCategoryDTOList() {
    }

    @Test
    void getProductCategoryDTOByNumber() {
    }

    @Test
    void upCategoryInfo() {
    }

    @Test
    void downCategoryInfo() {
    }

    @Test
    void save() {
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO() ;
        productCategoryDTO.setCategoryStatus(0);
        productCategoryDTO.setCategoryName("优惠");
        productCategoryDTO.setCategoryNumber(IdUtil.genId());
        productCategoryDTO.setUpdater("kweitan");
        productCategoryDTO.setCreator("kweitan");
        productCategoryDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        productCategoryDTO.setEnableFlag(1);
        productCategoryDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        ///pages/images/top/shucai.png
        ///pages/images/top/rouwan.png
        ///pages/images/top/shuichan.png
        ///pages/images/top/xiaochi.png
        ///pages/images/top/wumiguo.png
        productCategoryDTO.setBelongIndex(1);
        productCategoryDTO.setCategoryIcon("/pages/images/top/shuichan.png");
        productCategoryDTO.setCategoryUrl("/pages/images");
        productCategoryService.save(productCategoryDTO);
    }

    @Test
    void update() {
    }

    @Test
    void selectBySearchName() {
    }
}