package com.qunchuang.rwlmall.domain;

import com.qunchuang.rwlmall.bos.BaseEntity;
import com.qunchuang.rwlmall.bos.Bostype;
import com.qunchuang.rwlmall.enums.RoleEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Curtain
 * @date 2018/3/19 9:06
 */

@Entity
@Bostype("A08")
@Table(name = "T_Role")
@Getter
@Setter
public class Role extends BaseEntity implements UserDetails {

    /*用户名*/
    private String username;

    /*密码*/
    private String password;

    /*部门*/
    private String department;

    /*联系电话*/
    private String phone;

    /*角色权限*/
    private String roleAuthority;

    /*角色权限对应的数字*/
    private Integer roleNumber;

    /*功能权限*/
    private String functionAuthority;

    /*角色id  代理商  或者 门店  */
    private String accountId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list=new ArrayList<>();
        for(String role : roleAuthority.split(",")){
            list.add(new SimpleGrantedAuthority(role));
        }
        return list;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public String toString() {
        return this.username;
    }


//    public int hashCode() {
//        return username.hashCode();
//    }
//
//
//    public boolean equals(Object obj) {
//        return this.toString().equals(obj.toString());
//    }
}
