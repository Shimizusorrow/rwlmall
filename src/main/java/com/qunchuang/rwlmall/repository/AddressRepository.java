package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.Address;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/12 10:20
 */

@Repository
public interface AddressRepository extends JpaRepository<Address,String> {


    List<Address> findByUserId(String userId, Sort sort);
}
