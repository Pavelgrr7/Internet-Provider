package com.pavelryzh.provider.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Table(name = "Subscribers")
@Entity
@Getter
@Setter
public class Subscriber implements UserDetails {
    @Id
    private long id;

    private String passportSeriesNumber;

    @NotBlank(message = "Название тарифа не может быть пустым")
    @Size(max = 50, message = "Имя абонента не может превышать 50 символов")
    private String firstName;

    @NotBlank(message = "Необходимо указать заявленную скорость")
    @Size(max = 50,  message = "Фамилия абонента не может превышать 50 символов")
    private String middleName;

    @Size(max = 50, message = "Отчество абонента не может превышать 50 символов")
    private String lastName;

    private String phoneNumber;

    private String email;

    private String login;

    private String password_hash;

    private String role;

    public void setPasswordHash(String hashedPassword) {
        this.password_hash = hashedPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getPassword() {
        return password_hash;
    }

    @Override
    public String getUsername() {
        return login;
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
