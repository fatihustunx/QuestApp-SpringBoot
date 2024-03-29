package com.project.questapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.questapp.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long > {

	RefreshToken getByUserId(Long id);

	RefreshToken findByUserId(Long userId);

}
