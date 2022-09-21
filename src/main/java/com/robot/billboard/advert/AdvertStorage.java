package com.robot.billboard.advert;

import java.util.List;
import java.util.Optional;

public interface AdvertStorage {
    Optional<Advert> addAdvert(Advert advert);

    Optional<Advert> getAdvertById(Long advertId);

    Optional<Advert> updateAdvert(Long advertId, Advert advert);

    List<Advert> getAdvertsByUserId(Long userId);

    Optional<Advert> deleteAdvertById(Long advertId);
}
