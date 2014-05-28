package com.tcbook.ws.util;

/**
 * Created by caiouvini on 5/27/14.
 */
public class Pair<T, F> {

    private T left;
    private F right;

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public F getRight() {
        return right;
    }

    public void setRight(F right) {
        this.right = right;
    }

}
