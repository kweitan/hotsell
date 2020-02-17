package com.sinjee.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.entity.OrderDetail;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.BeanConversionUtils;
import com.sinjee.common.CacheBeanCopier;
import com.sinjee.common.HashUtil;
import com.sinjee.vo.ResultVO;
import com.sinjee.wechat.vo.WechatOrderDetailVO;
import com.sinjee.wechat.vo.WechatOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/2/16 20:56
 * @ClassName AdminOrderController
 * 描述 中台订单管理
 **/
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Value("${myWechat.salt}")
    private String salt ;

    @Autowired
    private OrderMasterService orderMasterService ;

    /**
     * 根据操作类型 订单处理 类型
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/listAll")
    public ResultVO listAll(HttpServletRequest request, @RequestParam(value = "currentPage", defaultValue = "1")
            Integer currentPage,
                                @RequestParam(value = "pageSize", defaultValue = "10")
                                        Integer pageSize,
                                @RequestParam(value = "searchType", defaultValue = "ALL")
                                        String searchType,
                            @RequestParam(value = "orderNumber", defaultValue = "")
                                        String orderNumber){
        IPage<OrderMasterDTO> orderMasterDTOIPage = orderMasterService.findOrderAllList(currentPage,pageSize,searchType,orderNumber);

        //从分页中获取List
        List<OrderMasterDTO> orderMasterDTOList = orderMasterDTOIPage.getRecords() ;
        List<WechatOrderVO> wechatOrderVOList = new ArrayList<>() ;
        //遍历放到productInfoVOList中
        if(null != orderMasterDTOList && orderMasterDTOList.size()>0){
            orderMasterDTOList.stream().forEach(orderMasterDTO -> {
                //根据商品编码生成唯一HASH编码
                String hashNumber = HashUtil.sign(orderMasterDTO.getOrderNumber(),salt);
                WechatOrderVO wechatOrderVO = new WechatOrderVO() ;
                CacheBeanCopier.copy(orderMasterDTO,wechatOrderVO);
                wechatOrderVO.setHashNumber(hashNumber);
                List<OrderDetail> orderDetailList = orderMasterDTO.getOrderDetailList();
                wechatOrderVO.setOrderDetailList(BeanConversionUtils
                        .copyToAnotherList(WechatOrderDetailVO.class,orderDetailList));
                wechatOrderVOList.add(wechatOrderVO) ;
            });
        }

        //返回前端
        ResultVO resultVO = new ResultVO();
        resultVO.setData(wechatOrderVOList);
        resultVO.setCurrentPage(currentPage);
        resultVO.setTotalSize(orderMasterDTOIPage.getTotal());
        resultVO.setPageTotal(orderMasterDTOIPage.getPages());
        resultVO.setCode(0);
        resultVO.setMessage("成功");

        return resultVO ;
    }

    /**
     * 填写运单号
     */
}
