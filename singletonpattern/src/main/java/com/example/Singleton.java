package com.example;

/**
 * 单例模式
 * 定义：一个类仅有一个实例，而且自行实例化并向整个系统提供整个实例。
 * 要素：1.构造方法私有化（保证客户端无法去实例化该对象）
 * 2.指向自己实例的私有静态引用
 * 3.以自己实例为返回值静态的公有的方法
 * 创建方式：
 * 1.饿汉式
 * 2.懒汉式
 */

//一般来说：单例模式只有五种：懒汉式、饿汉式、双重校验锁、枚举、静态内部类

/**
 * 第一种：懒汉，线程不安全
 * 缺点：在多线程不能正常工作
 */
public class Singleton {
    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

/**
 * 第二种：懒汉，线程安全
 * 缺点：能够在多线程工作，但是效率低
 */
class Singleton1 {
    private static Singleton1 singleton1;

    private Singleton1() {
    }

    public static synchronized Singleton1 getInstance() {
        if (singleton1 == null) {
            singleton1 = new Singleton1();
        }
        return singleton1;
    }
}

/**
 * 第三种：饿汉
 * 这种方式基于classloader机制避免了多线程的同步问题，是在类装载时就进行了实例化，
 */
class Singleton2 {
    private static Singleton2 singleton2 = new Singleton2();

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        return singleton2;
    }
}

/**
 * 第四种：饿汉，变种
 */
class Singleton3 {
    private final static Singleton3 singleton3;

    static {
        singleton3 = new Singleton3();
    }

    private Singleton3() {
    }

    public Singleton3 getInstance() {
        return this.singleton3;
    }
}

/**
 * 第五种：静态内部类
 * 只有显示调用getInstance(),才会显示装载SingletonHolder类，从而实例化Singleton4。
 * 如果直接实例化Singleton4比较消耗资源，想让它延迟加载，这种方式很好。
 */

class Singleton4 {
    private Singleton4() {
    }

    public static Singleton4 getInstance() {
        return SingletonHolder.SINGLETON_4;
    }

    private static class SingletonHolder {
        private static Singleton4 SINGLETON_4 = new Singleton4();
    }
}

/**
 * 第六种：枚举
 * Effective Java 作者Josh Bloch提倡方式，
 * 不仅能够避免多线程同步问题，
 * 还能够防止反序列化重新创建新对象，
 */
enum Singleton5 {
    INSTANCE;

    public void whateverMethod() {

    }
}

/**
 * 第七种：双重校验锁(双重检查锁定)
 * 适用于jdk1.5之后
 */
class Singleton6 {
    private volatile static Singleton6 singleton6;

    private Singleton6() {
    }

    public static Singleton6 getInstance() {
        if (singleton6 == null) {
            synchronized (Singleton6.class) {
                if (singleton6 == null) {
                    singleton6 = new Singleton6();
                }
            }
        }
        return singleton6;
    }
}

/**
 * 总结：
 * 1.如果单例由不同的类装载器装入，那便有可能存在多个单例类的实例。假定不是远端存取，例如一些
 * servlet容器对每一个servlet使用完全不同的类装载器，如果有两个servlet访问一个单例类，它们就会
 * 都有各自的实例。
 * 2.如果singleton实现了java.io.Serializable接口，那么这个类的实例就可能被序列化和复原。
 * 如果你序列化一个单例类的对象，接下来复原多个那个对象，就会有多个单例类的实例。
 */

//问题一修复：
class fix1 {
    private static Class getClass(String classname)
            throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null)
            classLoader = Singleton.class.getClassLoader();

        return (classLoader.loadClass(classname));
    }
}

//问题二修复：
class SingletonFix implements java.io.Serializable {
    public static SingletonFix INSTANCE = new SingletonFix();

    protected SingletonFix() {

    }
    private Object readResolve() {
        return INSTANCE;
    }
}
