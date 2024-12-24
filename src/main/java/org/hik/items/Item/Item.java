package org.hik.items.Item;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull
    @NotEmpty(message = "Item name is required.")
    private String name;

    @Column(name = "description", nullable = false)
    @NotNull
    @NotEmpty(message = "Item description is required.")
    private String description;

    @Column(name = "quantity", nullable = false)
    @NotNull
    @Min(value = 0, message = "There can only be a positive amount of items.")
    private int quantity;

    @Column(nullable = false)
    private String userId;

    public Item() {
    }

    public Item(String name, String description, int quantity, String userId) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Item setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }


    public Item createItem() {
        return new Item(name, description, quantity, userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", userId='" + userId + '\'' +
                '}';
    }
}
