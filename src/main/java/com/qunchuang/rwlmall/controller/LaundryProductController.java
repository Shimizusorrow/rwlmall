package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.anntations.RoleAuthority;
import com.qunchuang.rwlmall.domain.LaundryProduct;
import com.qunchuang.rwlmall.enums.RoleAuthorityFunctionEnum;
import com.qunchuang.rwlmall.service.LaundryProductService;
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
@RequestMapping("/laundryproduct")
public class LaundryProductController {

    @Autowired
    private LaundryProductService laundryProductService;

    /*根据商品id  查询商品*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("productid") String productId){
        return laundryProductService.findOne(productId);
    }

    /*根据商品编号 查询商品*/
    @RequestMapping("/findbynumber")
    public Object findByNumber(@RequestParam("number") String number){
        return laundryProductService.findByNumber(number);
    }


    /*查找一种所属下的所有商品  分页*/
    @RequestMapping("/findbytype")
    public Object findByNumber(@RequestParam("type") Integer type,
                               @RequestParam(name = "size", required = false) Integer size,
                               @RequestParam(name = "page", required = false) Integer page){
        if (size == null || page == null) {
            return laundryProductService.findByType(type);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return laundryProductService.findByType(pageable,type);
    }

    /*查找某种所属下的某种类别商品*/
    @RequestMapping("/findbytypeandcategory")
    public Object findByTypeAndCategory(@RequestParam("type") Integer type,
                                        @RequestParam("category") Integer category){
        return laundryProductService.findByTypeAndCategory(type,category);
    }

    /*查找某种所属下的某种类别商品*/
    @RequestMapping("/findbytypeandcategoryandstatus")
    public Object findByTypeAndCategory(@RequestParam("type") Integer type,
                                        @RequestParam("category") Integer category,
                                        @RequestParam("status") Integer status,
                                        @RequestParam(name = "size", required = false) Integer size,
                                        @RequestParam(name = "page", required = false) Integer page){
        if (size == null || page == null) {
            List<LaundryProduct> laundryProductList = laundryProductService.findByTypeAndCategoryAndStatus(type, category, status);
            laundryProductList.sort(new ProductComparator());
            return laundryProductList;
        }
        Sort sort = Sort.by(Sort.Order.desc("sort"));
        Pageable pageable = PageRequest.of(page - 1, size,sort);
        return laundryProductService.findByTypeAndCategoryAndStatus(type,category,status,pageable);
    }

    /*保存商品*/
    @PostMapping("/save")
    public Object save(@RequestBody LaundryProduct laundryProduct, Principal principal){
        return laundryProductService.save(laundryProduct);
    }

    /*修改商品*/
    @PostMapping("/update")
    public Object update(@RequestParam("productid") String productId,@RequestBody LaundryProduct laundryProduct){
        return laundryProductService.update(productId, laundryProduct);
    }

    /*上下架商品*/
    @RequestMapping("upoffshelf")
    public Object upOffShelf(@RequestParam("productid") String productId){
        laundryProductService.upOffShelf(productId);
        return null;
    }



}
