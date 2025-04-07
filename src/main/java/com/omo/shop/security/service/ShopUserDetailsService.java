package com.omo.shop.security.service;

import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.security.userprincipal.ShopUserDetails;
import com.omo.shop.user.model.User;
import com.omo.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return ShopUserDetails.buildUserDetails(user);
    }
}
