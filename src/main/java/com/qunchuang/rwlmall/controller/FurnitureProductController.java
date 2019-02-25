package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.FurnitureProduct;
import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.FurnitureProductService;
import com.qunchuang.rwlmall.utils.ProductComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/13 15:31
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/furnitureproduct")
public class FurnitureProductController {

    @Autowired
    private FurnitureProductService furnitureProductService;

    /*根据商品id  查询商品*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("productid") String productId) {
        return furnitureProductService.findOne(productId);
    }

    /*根据商品编号 查询商品*/
    @RequestMapping("/findbynumber")
    public Object findByNumber(@RequestParam("number") String number) {
        return furnitureProductService.findByNumber(number);
    }

    /*根据商品状态 是否上架  查询产品*/
    @RequestMapping("/findbystatus")
    public Object findByStatus(@RequestParam("status") Integer status,
                               @RequestParam(name = "size", required = false) Integer size,
                               @RequestParam(name = "page", required = false) Integer page) {
        if (size == null || page == null) {
            List<FurnitureProduct> furnitureProductList = furnitureProductService.findByStatus(status);
            furnitureProductList.sort(new ProductComparator());
            return furnitureProductList;
        }
        Sort sort = Sort.by(Sort.Order.desc("sort"));
        Pageable pageable = PageRequest.of(page - 1, size,sort);
        return furnitureProductService.findByStatus(status, pageable);
    }

    /*保存商品*/
    @PostMapping("/save")
    @RoleAuthority(RoleAuthorityFunctionEnum.PRODUCT_FURNITURE)
    public Object save(@RequestBody FurnitureProduct furnitureProduct, Principal principal) {
        return furnitureProductService.save(furnitureProduct);
    }

    /*修改商品*/
    @PostMapping("/update")
    @RoleAuthority(RoleAuthorityFunctionEnum.PRODUCT_FURNITURE)
    public Object update(@RequestParam("productid") String productId, @RequestBody FurnitureProduct furnitureProduct, Principal principal) {
        return furnitureProductService.update(productId, furnitureProduct);
    }

    /*上下架商品*/
    @RequestMapping("/upoffshelf")
    @RoleAuthority(RoleAuthorityFunctionEnum.PRODUCT_FURNITURE)
    public Object upOffShelf(@RequestParam("productid") String productId, Principal principal) {
        furnitureProductService.upOffShelf(productId);
        return null;
    }

    @RequestMapping("/findall")
    public Object findAll() {
        return furnitureProductService.findAll();
    }


}
