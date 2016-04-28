package com.lanxiao.doapp.entity;

/**
 * Created by Thinkpad on 2015/10/31.
 */
public class Orientations {
    public enum Orientation {
        Ordered, Disordered;

        public static Orientation fromIndex(int index) {
            Orientation[] values = Orientation.values();
            if(index < 0 || index >= values.length) {
                throw new IndexOutOfBoundsException();
            }
            return values[index];
        }
    }
}
