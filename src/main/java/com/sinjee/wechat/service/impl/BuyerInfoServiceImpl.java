package com.sinjee.wechat.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sinjee.common.*;
import com.sinjee.exceptions.MyException;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.entity.BuyerInfo;
import com.sinjee.wechat.mapper.BuyerInfoMapper;
import com.sinjee.wechat.service.BuyerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建时间 2020 - 01 -06
 * 买家信息
 * @author kweitan
 */
@Service
@Slf4j

public class BuyerInfoServiceImpl implements BuyerInfoService {

    @Autowired
    private BuyerInfoMapper buyerInfoMapper;

    @Value("${wechat.md5Salt}")
    private String md5Salt ;

    @Autowired
    private WxMaService wxMaService ;

    @Override
    public Integer save(BuyerInfoDTO buyerInfoDTO) {

        BuyerInfo buyerInfo = new BuyerInfo() ;
        CacheBeanCopier.copy(buyerInfoDTO,buyerInfo);

        return buyerInfoMapper.insert(buyerInfo);
    }

    @Override
    public Integer update(BuyerInfoDTO buyerInfoDTO) {

        BuyerInfo buyerInfo = new BuyerInfo() ;
        CacheBeanCopier.copy(buyerInfoDTO,buyerInfo);

        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.eq("open_id",buyerInfoDTO.getOpenId());
        return buyerInfoMapper.update(buyerInfo,wrapper);
    }

    @Override
    public BuyerInfoDTO find(String openId) {
        BuyerInfoDTO buyerInfoDTO = new BuyerInfoDTO();
        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.eq("open_id",openId);
        BuyerInfo buyerInfo = buyerInfoMapper.selectOne(wrapper) ;
        CacheBeanCopier.copy(buyerInfo,buyerInfoDTO);
        return buyerInfoDTO;
    }

    @Override
    public Integer updateBlack(String openId) {
        BuyerInfo buyerInfo = new BuyerInfo() ;
        buyerInfo.setEnableFlag(0);
        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.eq("enable_flag",1).eq("open_id",openId);
        return buyerInfoMapper.update(buyerInfo,wrapper);
    }

    @Override
    public IPage<BuyerInfoDTO> selectBuyerByPage(Integer currentPage, Integer pageSize, String selectName) {
        QueryWrapper<BuyerInfo> wrapper = new QueryWrapper();
        wrapper.like("buyer_name",selectName);
        Page<BuyerInfo> page = new Page<>(currentPage,pageSize) ;
        //从数据库分页获取数据
        IPage<BuyerInfo> mapPage = buyerInfoMapper.selectPage(page,wrapper);

        log.info("总页数"+mapPage.getPages());
        log.info("总记录数"+mapPage.getTotal());
        List<BuyerInfo> buyerInfoList = mapPage.getRecords() ;
        List<BuyerInfoDTO> buyerInfoDTOList = BeanConversionUtils.copyToAnotherList(BuyerInfoDTO.class,buyerInfoList);

        Page<BuyerInfoDTO> buyerInfoDTOPage = new Page<>(currentPage,pageSize) ;
        buyerInfoDTOPage.setPages(mapPage.getPages());
        buyerInfoDTOPage.setTotal(mapPage.getTotal());
        buyerInfoDTOPage.setRecords(buyerInfoDTOList) ;
        return buyerInfoDTOPage ;
    }

    @Override
    @Transactional
    public boolean saveOrUpdate(BuyerInfoDTO buyerInfoDTO) {
        return false;
    }

    @Override
    @Transactional
    public String login(String code) {

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid() ;
            String sessionKey = session.getSessionKey() ;
            log.info("接收来自微信客户端的session:{}", GsonUtil.getInstance().toStr(session));
            log.info("接收来自微信客户端的openid:{}",session.getOpenid());

            //1.先查询是否存在openid
            BuyerInfoDTO selectBuyerInfo = find(openid) ;
            if(null == selectBuyerInfo || selectBuyerInfo.getOpenId() == null){
                BuyerInfoDTO buyerInfoDTO = new BuyerInfoDTO() ;
                buyerInfoDTO.setOpenId(openid);
                buyerInfoDTO.setSessionKey(sessionKey);
                buyerInfoDTO.setCreator(IdUtil.genId());
                buyerInfoDTO.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
                buyerInfoDTO.setUpdater(IdUtil.genId());
                buyerInfoDTO.setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
                Integer success = save(buyerInfoDTO);
                if (!(success>0)){
                    throw new MyException(101,"获取openid失败!") ;
                }

            }else{
                selectBuyerInfo.setSessionKey(sessionKey);
                Integer success = update(selectBuyerInfo) ;
                if (!(success>0)){
                    throw new MyException(101,"更新user失败") ;
                }
            }


            // 可以增加自己的逻辑，关联业务相关数据
            return sessionKey ;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MyException(101,"获取openid失败!") ;
        }

    }
}
