package com.example.food.entity;


import com.example.food.gender.ActivityLevel;
import com.example.food.gender.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@Data
@Entity
@Table(name = "user1")
@AllArgsConstructor
@ToString(exclude = {"dailyCalories", "password"})
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column
    private String password;
    @Enumerated(EnumType.STRING)
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
    @OneToMany (cascade = CascadeType.ALL, mappedBy = "user")
    private List <DailyCalories> dailyCalories;

    @NonNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

