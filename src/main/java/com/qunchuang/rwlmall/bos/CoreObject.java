package com.qunchuang.rwlmall.bos;

import com.qunchuang.rwlmall.domain.Role;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by liutim on 2017/11/25.
 */


@MappedSuperclass
@Access(AccessType.FIELD)
public class CoreObject implements ICoreObject,Serializable {


    private String id;

    @Id
    @GeneratedValue(generator="bosidgenerator")
    @GenericGenerator(name="bosidgenerator",strategy="com.qunchuang.rwlmall.bos.BosidGenerator")
    @Column(name="id",nullable=false,length=25)
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return this.id;
    }

    private void setId(String id) {
        this.id=id;
    }

    @Override
   final public boolean equals(Object obj) {

        //todo: 因为是final  所以要只能在这里来修改Role对应的equals  来完成登录的角色是否相同判断  （避免相同用户多次登录）
        if (obj instanceof Role){
            return this.toString().equals(obj.toString());
        }

        if(this.id==null || obj==null || !(obj instanceof ICoreObject)){
            return false;
        }else{
            return Objects.equals(this.id,((ICoreObject)obj).getId());
        }
    }

    @Override
   final public int hashCode() {

        return (this.id==null)?13: Objects.hash(this.id);
    }
}
