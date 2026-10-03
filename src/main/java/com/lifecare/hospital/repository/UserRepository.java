package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Role;
import com.lifecare.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    Optional<User> findByFirstNameAndLastNameAndRole(String firstName, String lastName, Role role);
}
