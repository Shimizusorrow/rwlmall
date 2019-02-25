package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.enums.ProductStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Curtain
 * @date 2018/4/8 9:16
 */

@Entity
@Bostype("A09")
@Table(name = "T_MallProduct")
@Getter
@Setter
public class MallProduct extends BaseEntity implements Product{

    /*名称*/
    private String name;

    /*原价  单位分*/
    private long oldPrice;

    /*现价  单位分*/
    private long price;

    /*轮播图*/
    @Column(length = 512)
    private String sowingMap;

    /*商品首图*/
    private String logo;

    /*商品详情图*/
    @Column(length = 512)
    private String image;

    /*商品库存*/
    private int stock;

    /*商品生效日期*/
    private long date;

    /*商品排序（显示优先级）*/
    private int sort;

    /*商品类别*/
    private Integer category;

    /*规格*/
    private String standard;

    /*产品参数*/
    private String productParameters;

    /*快递运费  单位分*/
    private Long express;

    /*商品是否上架*/
    private Integer status = ProductStatusEnum.DOWN.getCode();

    /*是否自动上架*/
    private boolean autoStatus = false;


}
