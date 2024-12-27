package org.hik.items.Item;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
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

    @Column(name = "userId", nullable = false)
    private String userId;


    public Item(String name, String description, int quantity, String userId) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Item setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    public Item setUserId(String userId) {
        this.userId = userId;
        return this;
    }


}
