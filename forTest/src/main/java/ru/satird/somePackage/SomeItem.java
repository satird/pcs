package ru.satird.somePackage;

public class SomeItem {

    private long id;

    public SomeItem() {
    }

    public SomeItem(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return "SomeItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
