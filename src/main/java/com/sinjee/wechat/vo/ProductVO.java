package com.sinjee.wechat.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/12/15 12:11
 * @ClassName ProductVO
 * 描述 商品(包含类目)
 **/
@Data
public class ProductVO {
    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
