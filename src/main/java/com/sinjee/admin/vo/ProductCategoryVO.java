package com.sinjee.admin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/12/15 12:11
 * @ClassName ProductCategoryVO
 * 描述 商品(包含类目)
 **/
@Data
public class ProductCategoryVO extends BaseVO implements Serializable {

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("categoryNumber")
    private String categoryNumber ;

    /**0-未上架 1-上架*/
    @JsonProperty("categoryStatus")
    private Integer categoryStatus ;

    /** 哈希编码 **/
    @JsonProperty("goodHashNumber")
    private String hashNumber ;

}
