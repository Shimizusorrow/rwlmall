package com.qunchuang.rwlmall.bos;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Created by liutim on 2017/11/25.
 */


@MappedSuperclass
@Access(AccessType.FIELD)
public class Entry extends CoreObject implements IEntry {

    @Transient
    private ICoreObject parent;

    @Override
    public void setParent(ICoreObject parent) {
        this.parent = parent;
    }

    protected ICoreObject getInnerParent() {
        return parent;
    }
}
