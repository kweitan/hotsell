package com.sinjee.wechat.service.impl;

import com.sinjee.admin.service.ProductCategoryService;
import com.sinjee.common.IdUtil;
import com.sinjee.admin.dto.ProductCategoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class ProductCategoryServiceImplTest {

    @Autowired
    private ProductCategoryService productCategoryService ;

    @Test
    void saveProductCategoryInfo() {
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO() ;
        productCategoryDTO.setCategoryName("12.12");
        productCategoryDTO.setCategoryNumber(IdUtil.nextId()+"");
        productCategoryDTO.setCreator("kweitan");
        productCategoryDTO.setUpdater("kweitan");
        Integer status = productCategoryService.saveProductCategoryInfo(productCategoryDTO) ;
        if (status > 0){
            log.info("成功!");
        }
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
    void selectAll(){
        List<ProductCategoryDTO> list = productCategoryService.getAllProductCategoryDTOList() ;
        log.info("大小size="+list.size());
    }
}