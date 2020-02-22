package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.vo.AdminBuyerInfoVO;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.service.BuyerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小小极客
 * 时间 2020/2/21 22:23
 * @ClassName AdminBuyerController
 * 描述 AdminBuyerController
 **/
@RestController
@RequestMapping("/admin/buyer")
public class AdminBuyerController {

    @Value("${myWechat.salt}")
    private String salt ;

    @Autowired
    private BuyerInfoService buyerInfoService ;

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "currentPage", defaultValue = "1")
                                 Integer currentPage,
                         @RequestParam(value = "pageSize", defaultValue = "10")
                                 Integer pageSize,
                         @RequestParam(value = "selectName", defaultValue = "")
                                 String selectName){
        //2.查询数据
        IPage<BuyerInfoDTO> page = buyerInfoService.selectBuyerByPage(currentPage,pageSize,selectName);

        //从分页中获取List
        List<BuyerInfoDTO> buyerInfoDTOList = page.getRecords() ;
        List<AdminBuyerInfoVO> adminBuyerInfoVOList = new ArrayList<>() ;
        if(null != buyerInfoDTOList && buyerInfoDTOList.size()>0){
            buyerInfoDTOList.stream().forEach(buyerInfoDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(buyerInfoDTO.getOpenId(),salt);
                AdminBuyerInfoVO adminBuyerInfoVO = new AdminBuyerInfoVO() ;
                CacheBeanCopier.copy(buyerInfoDTO,adminBuyerInfoVO);
                adminBuyerInfoVO.setSelectName(selectName);
                adminBuyerInfoVO.setHashNumber(hashNumber);
                adminBuyerInfoVOList.add(adminBuyerInfoVO);
            });
        }

        return ResultVOUtil.success(currentPage,page.getTotal(),adminBuyerInfoVOList);

    }

    @CrossOrigin(origins = "*")
    @GetMapping("/fetchBuyerInfo")
    public ResultVO fetchBuyerInfoByOpenid(
            @RequestParam String openid,
            @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(openid,salt,hashNumber)){
            return ResultVOUtil.error(101,"数据不一致!") ;
        }

        BuyerInfoDTO buyerInfoDTO = buyerInfoService.find(openid) ;
        Map<String,Object> map = new HashMap<>() ;
        map.put("buyerId",buyerInfoDTO.getBuyerName());

        return ResultVOUtil.success(map) ;
    }
}
