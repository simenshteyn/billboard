package com.robot.billboard.advert;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertRepository extends JpaRepository<Advert, Long> {
    List<Advert> findByOwnerId(Long ownerId);

    List<Advert> findAllByAvailableOrderByIdDesc(Boolean available, Pageable pageable);
}
