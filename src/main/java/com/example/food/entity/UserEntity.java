package com.example.food.entity;


import com.example.food.gender.ActivityLevel;
import com.example.food.gender.Gender;
import com.example.food.gender.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@Data
@Entity
@Table(name = "user1")
@AllArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column
    private String password;
    @Column(name = "gender")
    private Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(name = "activity")
    private ActivityLevel activity;
    @Column
    private Float weight;
    @Column
    private Float height;
    @Column
    private Integer age;
    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private Role role = Role.USER;
    @Column
    private Double caloriesNorm;
    @Column
    private Double proteinNorm;
    @Column
    private Double fatNorm;
    @Column
    private Double carbsNorm;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", gender=" + gender +
                ", weight=" + weight +
                ", height=" + height +
                ", age=" + age +
                ", role=" + role +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}