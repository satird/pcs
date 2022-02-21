package ru.satird;

public class SomeInterfaceImpl implements SomeInterface {

    @Override
    public String apply(String s, String s2) {
        return s + " " + s2;
    }
}
