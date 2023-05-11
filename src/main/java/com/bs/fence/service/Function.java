package com.bs.fence.service;

public interface Function<T, E> {

    T callback(E e);

}