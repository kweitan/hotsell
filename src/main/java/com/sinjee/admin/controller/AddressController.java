package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.vo.AddressVO;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.AddressInfoDTO;
import com.sinjee.wechat.service.AddressInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2020 - 01 -19
 * 地址控制层
 * @author kweitan
 */
@RestController
@RequestMapping("/admin/address")
public class AddressController {


    @Autowired
    private AddressInfoService addressInfoService ;

    @Value("${myWechat.salt}")
    private String salt ;

    @CrossOrigin(origins = "*")
    @GetMapping("/getAddressById")
    public ResultVO getAddressById(@RequestParam Integer addressId,
                                   @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(addressId+"",salt,hashNumber)){
            return ResultVOUtil.error(101,"地址ID不一致!") ;
        }

        AddressInfoDTO addressInfoDTO = addressInfoService.getAddressById(addressId);
        if (null != addressInfoDTO){
            return ResultVOUtil.success(addressInfoDTO);
        }else {
            return ResultVOUtil.error(101,"查找地址信息失败") ;
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "currentPage", defaultValue = "1")
                                 Integer currentPage,
                         @RequestParam(value = "pageSize", defaultValue = "8")
                                 Integer pageSize,
                         @RequestParam(value = "selectName", defaultValue = "")
                                 String selectName){
        //1.加载页数不超过20页
        if (currentPage > 20){
            currentPage = 20 ;
        }

        if(pageSize > 10){
            pageSize = 10 ;
        }


        IPage<AddressInfoDTO> page = addressInfoService.selectAddressByPage(currentPage,pageSize,selectName);

        //从分页中获取List
        List<AddressInfoDTO> addressInfoDTOList = page.getRecords() ;
        List<AddressVO> addressVOList= new ArrayList<>() ;
        //遍历放到productInfoVOList中
        if(null != addressInfoDTOList && addressInfoDTOList.size()>0){
            addressInfoDTOList.stream().forEach(addressInfoDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(addressInfoDTO.getAddressId()+"",salt);
                AddressVO addressVO = new AddressVO() ;
                CacheBeanCopier.copy(addressInfoDTO,addressVO);
                addressVO.setHashNumber(hashNumber);
                addressVOList.add(addressVO);
            });
        }

        return ResultVOUtil.success(currentPage,page.getTotal(),addressVOList);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/delete")
    public ResultVO delete(@RequestParam Integer addressId,
                                   @RequestParam String hashNumber){
        //取得类目编码和哈希
        if(!HashUtil.verify(addressId+"",salt,hashNumber)){
            return ResultVOUtil.error(101,"地址ID不一致!") ;
        }
        Integer result = addressInfoService.delete(addressId) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"删除地址信息失败") ;
        }
    }
}
