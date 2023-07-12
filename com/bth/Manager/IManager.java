package com.bth.Manager;
/**
 *
 * @author Heroes x BTH
 * 
 */
public interface IManager<E> {
    void find(E e);
    void add(E e);
    void remove(E e);
}
