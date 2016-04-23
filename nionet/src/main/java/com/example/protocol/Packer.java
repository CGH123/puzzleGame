package com.example.protocol;


public class Packer<T> {

    public T bean;

    public Packer() {}

    public Packer(T bean) {
        this.bean = bean;
    }

}
