package com.sparta.spring26.domain.user.entity;

//import com.sparta.spring26.domain.restaurant.entity.Restaurant;

import com.sparta.spring26.global.config.PasswordEncoder;
import com.sparta.spring26.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    //회원탈퇴
    private boolean isDeleted = false;

    public User(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private User(Long id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public User() {

    }


    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public void deleteUser() {
        this.isDeleted = true;
    }

    public boolean isPasswordMatch(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }


//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
//    private List<Restaurant> restaurantList = new ArrayList<>();
}
