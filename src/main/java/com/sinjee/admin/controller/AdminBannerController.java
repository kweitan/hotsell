package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.SellerInfoDTO;
import com.sinjee.admin.form.AdminBannerForm;
import com.sinjee.admin.vo.AdminBannerVO;
import com.sinjee.common.*;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.dto.WechatBannerDTO;
import com.sinjee.wechat.service.WechatBannerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/23 8:49
 * @ClassName AdminBannerController
 * 描述 AdminBannerController 导航栏管理
 **/
@RestController
@RequestMapping("/admin/banner")
public class AdminBannerController {

    @Value("${myWechat.salt}")
    private String salt ;

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private WechatBannerService wechatBannerService ;

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "currentPage", defaultValue = "1")
                                     Integer currentPage,
                         @RequestParam(value = "pageSize", defaultValue = "10")
                                     Integer pageSize,
                         @RequestParam(value = "selectName", defaultValue = "")
                                     String selectName){

        IPage<WechatBannerDTO> page = wechatBannerService.getBannerByPage(currentPage,pageSize,selectName);

        //从分页中获取List
        List<WechatBannerDTO> wechatBannerDTOList = page.getRecords() ;
        List<AdminBannerVO> adminBannerVOList = new ArrayList<>() ;
        if(null != wechatBannerDTOList && wechatBannerDTOList.size()>0){
            wechatBannerDTOList.stream().forEach(wechatBannerDTO -> {
                AdminBannerVO adminBannerVO = new AdminBannerVO() ;
                String hashNumber = HashUtil.sign(String.valueOf(wechatBannerDTO.getBannerId()),salt) ;
                CacheBeanCopier.copy(wechatBannerDTO,adminBannerVO);
                adminBannerVO.setHashNumber(hashNumber);
                adminBannerVOList.add(adminBannerVO);
            });
        }

        //返回前端
        return ResultVOUtil.success(currentPage,page.getTotal(),adminBannerVOList);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/save")
    public ResultVO saveBanner(HttpServletRequest request ,@RequestBody @Valid AdminBannerForm adminBannerForm, BindingResult bindingResult){
        //1.校验参数
        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        WechatBannerDTO wechatBannerDTO = new WechatBannerDTO() ;

        //获取卖家信息
        SellerInfoDTO sellerInfoDTO = Common.getSellerInfo(request,redisUtil) ;

        wechatBannerDTO.setBannerName(adminBannerForm.getBannerName());
        wechatBannerDTO.setBannerUrl(adminBannerForm.getBannerUrl());
        wechatBannerDTO.setBannerIcon(adminBannerForm.getBannerIcon());


        //2.取出当前用户
        wechatBannerDTO.setCreator(sellerInfoDTO.getSellerName());
        wechatBannerDTO.setUpdater(sellerInfoDTO.getSellerName());
        wechatBannerDTO.setCreateTime(DateUtils.getTimestamp());
        wechatBannerDTO.setUpdateTime(DateUtils.getTimestamp());

        //4.保存
        Integer result = wechatBannerService.save(wechatBannerDTO) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"保存广告栏失败") ;
        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    public ResultVO updateBanner(HttpServletRequest request ,@RequestBody @Valid AdminBannerForm adminBannerForm, BindingResult bindingResult){
        //1.校验参数
        if (bindingResult.hasErrors()){
            return ResultVOUtil.error(101,bindingResult.getFieldError().getDefaultMessage()) ;
        }

        //获取卖家信息
        SellerInfoDTO sellerInfoDTO = Common.getSellerInfo(request,redisUtil) ;


        //校验
        if(!HashUtil.verify(String.valueOf(adminBannerForm.getBannerId()),salt,adminBannerForm.getHashNumber())){
            return ResultVOUtil.error(101,"数据不一致!") ;
        }


        WechatBannerDTO wechatBannerDTO = new WechatBannerDTO() ;


        wechatBannerDTO.setBannerIcon(adminBannerForm.getBannerIcon());
        wechatBannerDTO.setBannerUrl(adminBannerForm.getBannerUrl());
        wechatBannerDTO.setBannerName(adminBannerForm.getBannerName());
        wechatBannerDTO.setBannerId(adminBannerForm.getBannerId());

        //2.取出当前用户
        wechatBannerDTO.setUpdater(sellerInfoDTO.getSellerName());
        wechatBannerDTO.setUpdateTime(DateUtils.getTimestamp());


        //4.更新
        Integer result = wechatBannerService.update(wechatBannerDTO) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"修改广告栏失败") ;
        }

    }

    @CrossOrigin(origins = "*")
    @GetMapping("/lookupDetail")
    public ResultVO lookupDetail(@RequestParam Integer bannerId,
                                       @RequestParam String hashNumber){

        //取得类目编码和哈希
        if(!HashUtil.verify(String.valueOf(bannerId),salt,hashNumber)){
            return ResultVOUtil.error(101,"数据不一致!") ;
        }

        WechatBannerDTO wechatBannerDTO = wechatBannerService.selectOne(bannerId);
        if (wechatBannerDTO == null || StringUtils.isBlank(wechatBannerDTO.getBannerName())){
            return ResultVOUtil.error(101,"无明细") ;
        }
        AdminBannerVO adminBannerVO = new AdminBannerVO() ;
        CacheBeanCopier.copy(wechatBannerDTO,adminBannerVO);
        adminBannerVO.setHashNumber(hashNumber);
        return ResultVOUtil.success(adminBannerVO) ;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/delete")
    public ResultVO delete(@RequestParam Integer bannerId,
                                 @RequestParam String hashNumber){

        //取得类目编码和哈希
        if(!HashUtil.verify(String.valueOf(bannerId),salt,hashNumber)){
            return ResultVOUtil.error(101,"数据不一致!") ;
        }

        Integer result = wechatBannerService.delete(bannerId) ;
        if (result > 0){
            return ResultVOUtil.success();
        }else {
            return ResultVOUtil.error(101,"删除广告栏失败") ;
        }
    }
}
