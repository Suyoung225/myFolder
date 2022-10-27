package com.example.loginlivesession2.repository;


import com.example.loginlivesession2.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/*public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}*/

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(String userId);
}
