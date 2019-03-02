package org.JdbcRealationsDemo.entity;


public class Owner extends NamedEntity {

    public Owner(String name) {
        this.name = name;
    }

    public Owner(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Owner() {

    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
