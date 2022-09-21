package com.robot.billboard.advert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertRepository extends JpaRepository<Advert, Long> {
    List<Advert> findByOwnerId(Long ownerId);
}
