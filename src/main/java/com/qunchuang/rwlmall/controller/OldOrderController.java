package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.service.OldOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author Curtain
 * @date 2018/9/3 15:47
 */

@RequestMapping("/oldorder")
@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@Slf4j
public class OldOrderController {

    @Autowired
    private OldOrderService oldOrderService;


    /*根据商品编号 查询订单*/
    @RequestMapping("/findbynumber")
    public Object findByNumber(@RequestParam(name = "number") String number) {
        return oldOrderService.findByNumber(number);
    }

    /*查询所有订单  分页*/
    @RequestMapping("/findall")
    public Object findAll(@RequestParam(name = "size") Integer size,
                          @RequestParam(name = "page") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return oldOrderService.findAll(pageable);
    }

}
