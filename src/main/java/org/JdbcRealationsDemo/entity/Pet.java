package org.JdbcRealationsDemo.entity;


public class Pet extends NamedEntity {

    public Pet(String name) {
        this.name = name;
    }

    public Pet(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Pet() {

    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
