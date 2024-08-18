package com.llm.design.singleton;

public class Singleton {

    private volatile static Singleton singleton = null;

    private Singleton(){
    }

    public static Singleton getSingleton(){
        if (singleton == null){
            synchronized (Singleton.class){
                if (singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
