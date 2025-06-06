package com.enotes.Repository;

import com.enotes.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByStatus_ResetPasswordCode(Integer statusResetPasswordCode);
}
