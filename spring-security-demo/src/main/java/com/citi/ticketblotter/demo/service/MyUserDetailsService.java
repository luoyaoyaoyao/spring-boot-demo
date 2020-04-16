package com.citi.ticketblotter.demo.service;

import com.citi.ticketblotter.demo.mapper.UserMapper;
import com.citi.ticketblotter.demo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        user.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
        return user;
    }

    private List<GrantedAuthority> generateAuthorities(String roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] roleArray = roles.split(";");
        if (roles != null && !"".equals(roles)) {
            for (String role : roleArray) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorities;
    }
}
