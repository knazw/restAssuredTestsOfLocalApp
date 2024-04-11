package org.dataProviders;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterSingleton {

    AtomicInteger atomicInteger;
    private static volatile CounterSingleton instance;

    public String value;

    private CounterSingleton(String value) {
        this.value = value;
        atomicInteger = new AtomicInteger(3);
    }
    public static CounterSingleton getInstance(String value) {
        CounterSingleton result = instance;

        if(result != null) {
            return result;
        }

        synchronized (CounterSingleton.class) {
            if(instance == null) {
                instance = new CounterSingleton(value);
            }
            return instance;
        }
    }

    public int decreaseAndGetValue() {
        return this.atomicInteger.decrementAndGet();
    }

    public int getValue() {
        return atomicInteger.get();
    }


}
