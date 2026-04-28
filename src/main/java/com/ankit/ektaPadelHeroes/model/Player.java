package com.ankit.ektaPadelHeroes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Display name cannot be blank")
    @Column(nullable = false)
    private String displayName;

    private String displayImage;

    // Constructor without id - for request bodies
    public Player(String name, String displayName, String displayImage) {
        this.name = name;
        this.displayName = displayName;
        this.displayImage = displayImage;
    }

    // Constructor without id and displayImage - for minimal request bodies
    public Player(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }
}
