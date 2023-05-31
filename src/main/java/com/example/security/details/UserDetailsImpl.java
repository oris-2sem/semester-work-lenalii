package com.example.security.details;

import com.example.model.constant.Status;
import com.example.model.entity.AccountEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails{

    @Getter
    private AccountEntity account;

    public UserDetailsImpl(AccountEntity account) {
        this.account = account;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleAsAuthority = account.getRole().toString();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleAsAuthority);
        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return account.getHashPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account.getStatus().equals(Status.BLOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.getStatus().equals(Status.CONFIRMED);
    }

}
