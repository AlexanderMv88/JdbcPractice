package org.JdbcRealationsDemo.entity;

import lombok.Data;

@Data
public class Pet extends NamedEntity {

    private long ownerId;

    public Pet(String name) {
        this.name = name;
    }

    public Pet(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Pet(long id, String name, long ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
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
