package ru.satird;

public class Main {

    public static void main(String[] args) {
//        B b = new B();
//        b.returnLetterB();
//        ClassWhoCallSomeAbstractClass myClass = new ClassWhoCallSomeAbstractClass();
        SomeAbstractClass someAbstractClass = new ClassWhoCallSomeAbstractClass();
    }
}

//class A {
//
//    static {
//        System.out.println("class A static block");
//    }
//    {
//        System.out.println("class A not static block");
//    }
//    public A() {
//        System.out.println("class A constructor");
//    }
//}
//
//class B extends A {
//
//    static {
//        System.out.println("class B static block");
//    }
//    {
//        System.out.println("class B not static block");
//    }
//    public B() {
//        System.out.println("class B constructor");
//    }
//    void returnLetterB() {
//        System.out.println("B");
//    }
//}