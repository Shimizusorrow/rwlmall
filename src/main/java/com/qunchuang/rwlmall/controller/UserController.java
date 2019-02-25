package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.domain.User;
import com.qunchuang.rwlmall.service.ConsumeRecordService;
import com.qunchuang.rwlmall.service.OldOrderService;
import com.qunchuang.rwlmall.service.UserService;
import com.qunchuang.rwlmall.utils.CardPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Curtain
 * @date 2018/3/13 15:30
 */

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConsumeRecordService consumeRecordService;


    /*查找所有用户*/
    @RequestMapping("/findall")
    public Object findAll(@RequestParam(name = "size", required = false) Integer size,
                          @RequestParam(name = "page", required = false) Integer page) {
        if (size == null || page == null) {
            return userService.findAll();
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return userService.findAll(pageable);
    }

    /*通过id  查找用户*/
    @RequestMapping("/findone")
    public Object findOne(@RequestParam(name = "userid", defaultValue = "") String userId, Principal principal) {
        if ("".equals(userId)) {
            return userService.findOne(principal.getName());
        } else {
            return userService.findOne(userId);
        }

    }

    /*修改  只是做信息的修改   并不能改动到余额*/
    @PostMapping("/update")
    public Object update(Principal principal, @RequestBody User user) {
        return userService.findOne(principal.getName());   //暂时没有需要修改功能
    }

    /*查找用户记录*/
    @RequestMapping("/obtainuserrecord")
    public Object obtainUserRecord(@RequestParam(name = "userid", defaultValue = "") String userId,
                                   @RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                   @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                   @RequestParam(name = "size", required = false) Integer size,
                                   @RequestParam(name = "page", required = false) Integer page,
                                   Principal principal) {

        Sort sort = Sort.by(Sort.Order.desc("createtime"));

        if ("".equals(userId)) {
            userId = principal.getName();
        }

        if (size == null || page == null) {
            return consumeRecordService.findByUserIdAndTime(userId, startTime, endTime, sort);
        }

        Pageable pageable = PageRequest.of(page-1, size, sort);
        return consumeRecordService.findByUserIdAndTime(userId, startTime, endTime, pageable);
    }

    //统计用户总数
    @RequestMapping("/count")
    public Object count() {
        return userService.count();
    }

    /*查找用户的订单  时间降序*/
    @RequestMapping("/findbyuserid")
    public Object findByUserId(Principal principal) {
        return userService.userOrders(principal.getName());
    }

    /*按消费次数 时间  区域查找用户*/
    @RequestMapping("/findbyconsumestatusandtimeandarea")
    public Object findByConsumeStatusAndTimeAndArea(@RequestParam("status") Integer status,
                                                    @RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                                    @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                                    @RequestParam(name = "country", defaultValue = "") String country,
                                                    @RequestParam(name = "province", defaultValue = "") String province,
                                                    @RequestParam(name = "city", defaultValue = "") String city,
                                                    @RequestParam(name = "size", defaultValue = "8") Integer size,
                                                    @RequestParam(name = "page", defaultValue = "1") Integer page) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userService.findByConsumeStatusAndTimeAndArea(status, startTime, endTime, country, province, city, pageRequest);
    }

    @RequestMapping("/findbytimeandarea")
    public Object findByTimeAndArea(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                    @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                    @RequestParam(name = "country", defaultValue = "") String country,
                                    @RequestParam(name = "province", defaultValue = "") String province,
                                    @RequestParam(name = "city", defaultValue = "") String city,
                                    @RequestParam(name = "size", defaultValue = "8") Integer size,
                                    @RequestParam(name = "page", defaultValue = "1") Integer page) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userService.findByTimeAndArea(startTime, endTime, country, province, city, pageRequest);
    }

    /*通过查询查询用户*/
    @RequestMapping("/findbyphone")
    public Object findByPhone(@RequestParam("phone") String phone) {
        return userService.findByPhone(phone);
    }

    /*用户完结订单*/
    @RequestMapping("/finishorder")
    public Object finishOrder(Principal principal) {
        return userService.finishOrder(principal.getName());
    }

    /*用户进行中订单*/
    @RequestMapping("/carryonorder")
    public Object carryOnOrder(Principal principal) {
        return userService.carryOnOrder(principal.getName());
    }

    /*修改手机号*/
    @RequestMapping("/changephone")
    public Object changePhone(@RequestParam("phonenumber") String phoneNumber, Principal principal) {
        userService.changePhone(principal.getName(), phoneNumber);
        return null;
    }

    /*获取用户信息*/
    @RequestMapping("/getuserinfo")
    public Object getUserInfo(Principal principal) {
        return userService.findOne(principal.getName());
    }

    /*绑定卡*/
    @RequestMapping("/bindingcard")
    public Object bindingCard(String phone, String cno, String code, Principal principal) {
        userService.bindingCard(phone, cno, code, principal.getName());
        return null;
    }

    /*查找已绑卡的用户信息*/
    @RequestMapping("/findbybindindcard")
    public Object findByBindingCardAndTimeAndArea(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                                  @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                                  @RequestParam(name = "country", defaultValue = "") String country,
                                                  @RequestParam(name = "province", defaultValue = "") String province,
                                                  @RequestParam(name = "city", defaultValue = "") String city,
                                                  @RequestParam(name = "size", defaultValue = "8") Integer size,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userService.findByBindingCardAndTimeAndArea(startTime, endTime, country, province, city, pageable);
    }

    @RequestMapping("/findbyregisterphone")
    public Object findByRegisterPhone(@RequestParam("registerphone") String registerPhone){
        return userService.findByRegisterPhone(registerPhone);
    }

    /*查找已绑卡的用户信息*/
    @RequestMapping("/findbydeposit")
    public Object findByDepositAndTimeAndArea(@RequestParam(name = "starttime", defaultValue = "0") Long startTime,
                                                  @RequestParam(name = "endtime", defaultValue = "4099651200000") Long endTime,
                                                  @RequestParam(name = "country", defaultValue = "") String country,
                                                  @RequestParam(name = "province", defaultValue = "") String province,
                                                  @RequestParam(name = "city", defaultValue = "") String city,
                                                  @RequestParam(name = "size", defaultValue = "8") Integer size,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userService.findByDepositAndTimeAndArea(startTime, endTime, country, province, city, pageable);
    }

//    @RequestMapping("/initconsumerecord")
//    public Object initConsumeRecord(){
//        userService.initConsumeRecord();
//        return null;
//    }

    @RequestMapping("findbalancebycno")
    public Object findBalanceByCNO(@RequestParam("cno")String cno){
        return CardPayUtil.getBalance(cno);
    }
}
