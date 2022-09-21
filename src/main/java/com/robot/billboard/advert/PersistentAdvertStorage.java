package com.robot.billboard.advert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PersistentAdvertStorage implements AdvertStorage {
    private final AdvertRepository advertRepository;

    @Autowired
    public PersistentAdvertStorage(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    @Override
    @Transactional
    public Optional<Advert> addAdvert(Advert advert) {
        return Optional.of(advertRepository.save(advert));
    }

    @Override
    public Optional<Advert> getAdvertById(Long advertId) {
        return advertRepository.findById(advertId);
    }

    @Override
    @Transactional
    public Optional<Advert> updateAdvert(Long advertId, Advert advert) {
        Optional<Advert> searchAdvert = advertRepository.findById(advertId);
        if (searchAdvert.isEmpty()) return Optional.empty();
        if (advert.getName() != null) searchAdvert.get().setName(advert.getName());
        if (advert.getDescription() != null) searchAdvert.get().setDescription(advert.getDescription());
        if (advert.getPhone() != null) searchAdvert.get().setPhone(advert.getPhone());
        if (advert.getAvailable() != null) searchAdvert.get().setAvailable(advert.getAvailable());
        return searchAdvert;
    }

    @Override
    public List<Advert> getAdvertsByUserId(Long userId) {
        return advertRepository.findByOwnerId(userId);
    }

    @Override
    public Optional<Advert> deleteAdvertById(Long advertId) {
        Optional<Advert> advert = advertRepository.findById(advertId);
        advert.ifPresent(a -> advertRepository.deleteById(a.getId()));
        return advert;
    }
}
