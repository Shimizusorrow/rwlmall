package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/12 10:21
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByOpenid(String openid);

    User findByCountryContainingAndPhoneContaining(String country, String phone);

    Integer countByIdNotNull();

    Integer countByConsumeStatusAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(Integer status, Long startTime, Long endTime, String country, String province, String city);

    Page<User> findByConsumeStatusAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(Integer status, Long startTime, Long endTime, String country, String province, String city, Pageable pageable);

    Page<User> findByCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(Long startTime, Long endTime, String country, String province, String city, Pageable pageable);

    Optional<User> findByPhone(String phone);

    boolean existsByOpenid(String openid);

    Integer countByDepositIsTrueAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(Long startTime, Long endTime, String country, String province, String city);

    Integer countByBindingCardIsTrueAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(Long startTime, Long endTime, String country, String province, String city);

    Page<User> findByBindingCardIsTrueAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(Long startTime, Long endTime, String country, String province, String city, Pageable pageable);

    Page<User> findByDepositIsTrueAndCreatetimeBetweenAndCountryContainingAndProvinceContainingAndCityContaining(Long startTime, Long endTime, String country, String province, String city, Pageable pageable);


    User findByRegisterPhone(String resisterPhone);

}
