package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.domain.MallProduct;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.MallProductService;
import com.qunchuang.rwlmall.utils.ProductComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/13 15:31
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/mallproduct")
public class MallProductController {

    @Autowired
    private MallProductService mallProductService;

    /*根据商品id  查询商品*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("productid") String productId){
        return mallProductService.findOne(productId);
    }

    /*根据商品编号 查询商品*/
    @RequestMapping("/findbynumber")
    public Object findByNumber(@RequestParam("number") String number){
        return mallProductService.findByNumber(number);
    }

    /*根据商品状态 是否上架  查询产品*/
    @RequestMapping("/findbystatus")
    public Object findByStatus(@RequestParam("status") Integer status){
        return mallProductService.findByStatus(status);
    }

    /*查找某种类别商品*/
    @RequestMapping("/findbycategory")
    public Object findByTypeAndCategory(@RequestParam("category") Integer category){
        return mallProductService.findByCategory(category);
    }

    /*查找某类商品 同时满足 上架状态*/
    @RequestMapping("/findbycategoryandstatus")
    public Object findByCategoryAndStatus(@RequestParam("category") Integer category,
                                          @RequestParam("status") Integer status,
                                          @RequestParam(name = "size", required = false) Integer size,
                                          @RequestParam(name = "page", required = false) Integer page){
        if (size == null || page == null) {
            List<MallProduct> mallProductList = mallProductService.findByCategoryAndStatus(category, status);
            mallProductList.sort(new ProductComparator());
            return mallProductList;
        }
        Sort sort = Sort.by(Sort.Order.desc("sort"));
        Pageable pageable = PageRequest.of(page - 1, size,sort);
        return mallProductService.findByCategoryAndStatus(category,status,pageable);
    }

    /*保存商品*/
    @PostMapping("/save")
    @RoleAuthority(RoleAuthorityFunctionEnum.PRODUCT_MALL)
    public Object save(@RequestBody MallProduct mallProduct,Principal principal){
        return mallProductService.save(mallProduct);
    }

    /*修改商品*/
    @PostMapping("/update")
    @RoleAuthority(RoleAuthorityFunctionEnum.PRODUCT_MALL)
    public Object update(@RequestParam("productid") String productId,@RequestBody MallProduct mallProduct,Principal principal){
        return mallProductService.update(productId, mallProduct);
    }

    /*上下架商品*/
    @RequestMapping("upoffshelf")
    @RoleAuthority(RoleAuthorityFunctionEnum.PRODUCT_MALL)
    public Object upOffShelf(@RequestParam("productid") String productId,Principal principal){
        mallProductService.upOffShelf(productId);
        return null;
    }



}
