package ru.satird.somePackage;

import java.util.ArrayList;
import java.util.List;

public class SomeClass {

    List<SomeItem> itemList;

    {
        itemList = new ArrayList<>();
        itemList.add(new SomeItem(1, "first"));
        itemList.add(new SomeItem(2, "second"));
        itemList.add(new SomeItem(3, "third"));
        itemList.add(new SomeItem(4, "fourth"));
        itemList.add(new SomeItem(5, "fives"));
    }

    public SomeClass() {
    }

    public SomeClass(List<SomeItem> itemList) {
        this.itemList = itemList;
    }

    public List<SomeItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SomeItem> itemList) {
        this.itemList = itemList;
    }
}
