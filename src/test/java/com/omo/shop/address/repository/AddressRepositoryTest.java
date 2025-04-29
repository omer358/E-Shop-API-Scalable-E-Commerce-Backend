package com.omo.shop.address.repository;

import com.omo.shop.address.model.Address;
import com.omo.shop.user.model.User;
import com.omo.shop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;


    private Address address;
    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .build();

        address = Address.builder()
                .city("Khartoum")
                .country("Sudan")
                .state("Khartoum")
                .street("60 street")
                .zipCode("11111")
                .user(user)
                .build();

        userRepository.save(user);
        addressRepository.save(address);

    }

    @Test
    @DisplayName("should return a list of address when exist")
    void findByUserId_shouldReturnListOfUserAddresses_whenExist() {
        List<Address> result = addressRepository.findByUserId(user.getId());

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("should return a list of address when exist")
    void findByUserId_shouldReturnEmptyList() {
        List<Address> result = addressRepository.findByUserId(11L);

        assertThat(result).hasSize(0);
    }
}