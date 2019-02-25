package com.qunchuang.rwlmall.service;

import com.qunchuang.rwlmall.domain.Advertisement;

/**
 * @author Curtain
 * @date 2018/3/14 10:44
 */
public interface AdvertisementService {

    /*查询所属类别的广告*/
    Advertisement findByType(Integer type);

    /*保存一条广告*/
    Advertisement save(Advertisement advertisement);

    /*修改一条广告*/
    Advertisement update(String advertisementId, Advertisement advertisement);

    /*删除一条广告*/
    void delete(String advertisementId);

    /*查找一条广告*/
    Advertisement findOne(String advertisementId);



}
