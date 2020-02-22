package com.sinjee.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.wechat.dto.BuyerInfoDTO;

public interface BuyerService {

    IPage<BuyerInfoDTO> selectBuyerInfoByPage(
            Integer currentPage,Integer pageSize,String selectName);

}
