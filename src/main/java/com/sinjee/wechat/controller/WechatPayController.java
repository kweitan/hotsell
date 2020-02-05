package com.sinjee.wechat.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.sinjee.admin.dto.OrderMasterDTO;
import com.sinjee.admin.service.OrderMasterService;
import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.DateUtils;
import com.sinjee.common.MathUtil;
import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @author 小小极客
 * 时间 2020/2/4 23:03
 * @ClassName WechatPayController
 * 描述 WechatPayController
 **/
@Controller
@RequestMapping("wechat/pay")
@Slf4j
public class WechatPayController {

    @Autowired
    private WxPayService wxPayService ;

    @Autowired
    private OrderMasterService orderMasterService ;

    /**
     * 微信统一下单接口使用方式如下 预支付
     * @param request
     * @param orderNumber
     * @param subject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "wxpay")
    @AccessTokenIdempotency
    public ResultVO pay(String openid, HttpServletRequest request, String orderNumber, String subject){
        try {

            OrderMasterDTO orderMasterDTO = orderMasterService.findByOrderNumber(orderNumber) ;
            if(null == orderMasterDTO || StringUtils.isBlank(orderMasterDTO.getOrderNumber())){
                ResultVOUtil.error(230,"订单不存在");
            }

            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();

            /**商品描述 例子：腾讯充值中心-QQ会员充值**/
            orderRequest.setBody(subject);

            /**要求32个字符内，只能是数字、大小写字母**/
            orderRequest.setOutTradeNo("订单号");

            /**标价金额 订单总金额，单位为分**/
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(orderMasterDTO.getOrderAmount().toString()));//元转成分

            /**用户openid**/
            orderRequest.setOpenid(openid);

            /**调用微信支付API的机器IP**/
            orderRequest.setSpbillCreateIp(request.getRemoteAddr());

            /**交易起始时间**/
            orderRequest.setTimeStart(DateUtils.getCurrentDateBy());

            /**用户支付时间 交易结束时间 设置10分钟**/
            orderRequest.setTimeExpire(DateUtils.getCurrentDateAdd(10));

            /**
             * 发起预支付 wxPayService.createOrder(orderRequest)
             * **/

            /***
             * 查询支付订单状态 wxPayService.queryOrder()
             */

            return ResultVOUtil.success(wxPayService.createOrder(orderRequest));
        } catch (Exception e) {
            log.error("微信支付失败！订单号：{},原因:{}", orderNumber, e.getMessage());
            e.printStackTrace();
            return ResultVOUtil.error(220,"支付失败，请稍后重试！");
        }
    }

    /**
     * 异步回调接口代码内部会自动校验签名，您需要做的就是拿到结果之后直接根据业务逻辑进行处 结果返回给微信
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "payNotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response){
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);

            // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            /**商户订单号**/
            String orderId = result.getOutTradeNo();

            /**微信支付订单号**/
            String tradeNo = result.getTransactionId();

            /**订单金额**/
            String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());


            OrderMasterDTO orderMasterDTO = orderMasterService.findByOrderNumber(orderId) ;
            if (null == orderMasterDTO || StringUtils.isBlank(orderMasterDTO.getOrderNumber())){
                log.error("【微信支付】异步通知, 订单不存在, orderId={}", orderId);
                //订单不存在
                return WxPayNotifyResponse.fail("查不到订单") ;
            }

            //判断金额是否一致
            if (!MathUtil.equals(orderMasterDTO.getOrderAmount().doubleValue(),new BigDecimal(totalFee).doubleValue())){
                log.error("【微信支付】异步通知, 订单金额不一致, orderId={}, 微信通知金额={}, 系统金额={}",
                        orderId,
                        totalFee,
                        orderMasterDTO.getOrderAmount());
                return WxPayNotifyResponse.fail("金额不一致") ;
            }

            orderMasterDTO.setTransactionId(tradeNo);
            orderMasterDTO.setActAmount(new BigDecimal(totalFee));

            //修改订单的支付状态
            orderMasterService.pay(orderMasterDTO) ;


        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        return WxPayNotifyResponse.success("处理成功!") ;
    }
}
