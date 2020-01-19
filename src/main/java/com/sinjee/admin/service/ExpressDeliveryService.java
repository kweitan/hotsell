package com.sinjee.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.ExpressDeliveryDTO;

/**
 * 创建时间 2020 - 01 -06
 *
 * @author kweitan
 */
public interface ExpressDeliveryService {

    Integer save(ExpressDeliveryDTO expressDeliveryDTO) ;

    Integer update(ExpressDeliveryDTO expressDeliveryDTO) ;

    ExpressDeliveryDTO findByOrderNumber(String orderNumber);

    ExpressDeliveryDTO findByStrackingNumber(String trackingNumber);

    IPage<ExpressDeliveryDTO> selectExpressDeliveryByPage(
            Integer currentPage,Integer pageSize,String selectName);
}
