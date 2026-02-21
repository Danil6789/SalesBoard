package com.example.salesboard.services;

import com.example.salesboard.dtos.AdvertDto;
import com.example.salesboard.entities.Advert;
import com.example.salesboard.entities.Category;
import com.example.salesboard.entities.User;
import com.example.salesboard.mappers.AdvertMapper;
import com.example.salesboard.repositories.AdvertRepository;
import com.example.salesboard.repositories.CategoryRepository;
import com.example.salesboard.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AdvertService {
    private final AdvertRepository advertRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AdvertMapper advertMapper;

    public AdvertDto createAdvert(AdvertDto advertDto, Long id) {
        Category category = categoryRepository.findById(advertDto.getCategoryId()).orElse(null);
        if(category == null){
            return null;
        }
        Advert advert = advertMapper.toEntity(advertDto);
        advert.setCategory(category);
        advert.setUser(userRepository.findById(id).orElse(null));
        advertRepository.save(advert);
        advertDto.setId(advert.getId());
        advertDto.setStatus(advert.getStatus().getDisplayName());
        return advertDto;
    }

    public List<Advert> getAllAdverts() {
        return advertRepository.findAll();
    }
    public List<Advert> getMyAdverts(Long id){
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return advertRepository.findByUserOrderByCreatedAtDesc(currentUser);
    }


    public Advert getAdvertById(Long id) {
        return advertRepository.findById(id).orElse(null);
    }


    public AdvertDto updateAdvert(Long id, AdvertDto advertDto) {
        Category category = categoryRepository.findById(advertDto.getCategoryId()).orElse(null);
        if(category == null){
            return null;
        }
        Advert advert = advertRepository.findById(id).orElse(null);
        if(advert == null){
            return null;
        }
        advert.setCategory(category);
        advert.setUser(userRepository.findById(7L).orElse(null));
        advertMapper.update(advertDto, advert);
        advertRepository.save(advert);
        advertDto.setId(advert.getId());
        return advertDto;
    }

    public boolean deleteAdvert(Long id) {
        Advert advert = advertRepository.findById(id).orElse(null);
        if (advert == null) {
            return false;
        }
        advertRepository.delete(advert);
        return true;
    }
}