package com.sparta.spring26.security;

import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = user.getRole(); // USER
        String authority = role.getAuthority(); // ROLE_USER

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 이메일을 사용자 이름으로 사용
    }


    public String getEmail() {
        return user.getEmail();
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았음을 나타냅니다.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠겨있지 않았음을 나타냅니다.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호)이 만료되지 않았음을 나타냅니다.
    }

    @Override
    public boolean isEnabled() {
        return !user.isDeleted(); // 계정이 활성화되었는지 확인합니다.
    }
}
