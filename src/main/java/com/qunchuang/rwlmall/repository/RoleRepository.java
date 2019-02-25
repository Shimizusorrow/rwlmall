package com.qunchuang.rwlmall.repository;

import com.qunchuang.rwlmall.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Curtain
 * @date 2018/3/19 9:13
 */

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {

    Optional<Role> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Role> findByRoleNumberAndAccountId(Integer number, String accountId);

    List<Role> findByRoleNumber(Integer number);

}
