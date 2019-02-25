package com.qunchuang.rwlmall.bos;


import org.springframework.util.Assert;

import java.util.*;

/**
 * 父子关系在BOS中被描述为（实体(Entity)与分录(Entry)
 * 对父子关系中的集合进行一层薄薄的封装，目的有3：
 * 1.添加/删除集合元素时，维护(写入或者抹去)该元素的parent属性，去除领域模型类中的addXXX/removeXXX代码
 * 2.通过 this.get()提供后续的集合操作，避免在领域模型类中暴露getItems()/setItems方法。
 * 3.封装一些针对分录集合的常用操作，
 * @param <T>
 */
public class BosSet<T extends IEntry> implements IBosSet<T> {

    public BosSet(Set<T> set, ICoreObject parent) {
        assert set!=null;

        this.set = set;
        this.parent=parent;
    }
    private ICoreObject parent=null;

    private Set<T> set=null;

    @Override
    public int size() {
        return this.set.size();
    }

    @Override
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.set.iterator();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("please use method:toArray(T[])");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.set.toArray(a);
    }


    public boolean add(T t) {
        t.setParent(this.parent);
        return set.add(t);
    }

    @Override
    public boolean remove(Object o) {

        boolean result=this.set.remove(o);
        if(result){
            T t=(T)o;
            t.setParent(null);
        }
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        //TODO 注意设置被添加的条目其parent为parent
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        //TODO 注意设置被删除的条目其parent为null
        return this.set.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        //TODO 注意设置被删除的条目其parent为null
        return this.set.removeAll(c);
    }

    @Override
    public void clear() {
        //TODO 注意设置被删除的条目其parent为null
        this.set.clear();
    }

    @Override
    public Spliterator<T> spliterator() {
        return this.set.spliterator();
    }

    public boolean remove(T item){
        item.setParent(this.parent);
        return this.set.remove(item);
    }

    public boolean removeById(String entryid){
        Assert.notNull(entryid,"参数不能为null!");
        Optional<T> opt =this.set.stream().filter(t -> entryid.equals(t.getId())).findFirst();
        return opt.isPresent()?this.set.remove(opt.get()): false;
    }

    public Optional<T> findById(String entryid) {
        Assert.notNull(entryid,"参数不能为null!");
        return this.set.stream().filter(t -> entryid.equals(t.getId())).findFirst();
    }


    /**
     * 返回真正的set进一步操作，比如说遍历，建议采用for（x:set)（java5特性）或者stream流的形式，见java8新特性
     * 不要在采用iterator了
     */
    /*public Set<T> get(){
        return this.set;
    }*/

}
