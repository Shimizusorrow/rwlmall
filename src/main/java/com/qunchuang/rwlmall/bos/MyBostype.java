package com.qunchuang.rwlmall.bos;

 class MyBostype implements IBostype {
    private final String bt;

    public MyBostype(String bt) {
        this.bt=bt;
    }

    @Override
    public String  toString(){
        return this.bt;
    }
}
