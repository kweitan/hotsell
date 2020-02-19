package com.sinjee.admin.service;

import com.sinjee.admin.dto.SellerInfoDTO;

public interface SellerInfoService {

    SellerInfoDTO verifyUser(String username, String password);

    SellerInfoDTO find(String sellerNumber);
}
