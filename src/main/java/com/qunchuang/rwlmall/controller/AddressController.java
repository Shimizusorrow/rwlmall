package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.domain.Address;
import com.qunchuang.rwlmall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/3/13 15:30
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /*添加地址*/
    @PostMapping("/save")
    public Object addAddress(Principal principal, @RequestBody Address address) {
        addressService.save(principal.getName(), address);

        return null;
    }

    /*删除地址*/
    @RequestMapping("/delete")
    public Object deleteAddress(Principal principal, @RequestParam("addressid") String addressId) {
        addressService.deleteAddress(principal.getName(), addressId);

        return null;
    }

    /*获取当前用户的所有地址*/
    @RequestMapping("/getlist")
    public Object getList(Principal principal) {
        return addressService.findByUserId(principal.getName());
    }

    /*通过id 查找一条地址*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam("addressid") String addressId) {
        return addressService.findOne(addressId);
    }

    /*修改默认地址*/
    @RequestMapping("/changestatus")
    public Object changeStatus(Principal principal, @RequestParam("addressid") String addressId) {
        addressService.changeStatus(principal.getName(), addressId);
        return null;
    }

    /*修改地址*/
    @PostMapping("/update")
    public Object update(@RequestParam("addressid") String addressId, @RequestBody Address address) {
        return addressService.update(addressId, address);
    }

    @RequestMapping("/getdefaultaddress")
    public Object userDefaultAddress(Principal principal) {
        return addressService.userDefaultAddress(principal.getName());
    }
}
