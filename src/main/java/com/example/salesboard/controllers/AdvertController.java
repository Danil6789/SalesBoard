package com.example.salesboard.controllers;

import com.example.salesboard.dtos.AdvertDto;
import com.example.salesboard.entities.Advert;
import com.example.salesboard.entities.Category;
import com.example.salesboard.mappers.AdvertMapper;
import com.example.salesboard.repositories.AdvertRepository;
import com.example.salesboard.repositories.CategoryRepository;
import com.example.salesboard.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/adverts")
public class AdvertController {
    private AdvertRepository advertRepository;
    private CategoryRepository categoryRepository;
    private AdvertMapper advertMapper;
    private UserRepository userRepository;
    @PostMapping
    public ResponseEntity<AdvertDto> createAdvert(
            UriComponentsBuilder uriBuilder,
            @RequestBody AdvertDto advertDto
    ){
        Category category = categoryRepository.findById(advertDto.getCategoryId()).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().build();
        }
        Advert advert = advertMapper.toEntity(advertDto);
        advert.setCategory(category);
        advert.setUser(userRepository.findById((long)7).orElse(null));
        advertRepository.save(advert);
        advertDto.setId(advert.getId());
        advertDto.setStatus(advert.getStatus().getDisplayName());

        var uri = uriBuilder.path("/adverts/{id}").buildAndExpand(advertDto.getId()).toUri();
        return ResponseEntity.created(uri).body(advertDto);
    }

    @GetMapping
    public List<AdvertDto> getAllAdverts(){
        List<Advert> adverts = advertRepository.findAll();
        return adverts.stream().map(advertMapper::toDto).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<AdvertDto> findById(@PathVariable Long id){
        Advert advert = advertRepository.findById(id).orElse(null);
        if(advert == null){
            return ResponseEntity.badRequest().build();
        }
        AdvertDto advertDto = advertMapper.toDto(advert);
        return ResponseEntity.ok(advertDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertDto> updateAdvert(
            @PathVariable("id") Long id,
            @RequestBody AdvertDto advertDto
    ){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().build();
        }
        var advert = advertRepository.findById(id).orElse(null);
        if(advert == null){
            return ResponseEntity.badRequest().build();
        }
        advert.setCategory(category);
        advert.setUser(userRepository.findById((long)7).orElse(null));
        advertMapper.update(advertDto, advert);
        advertRepository.save(advert);
        advertDto.setId(advert.getId());

        return ResponseEntity.ok(advertDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvert(@PathVariable Long id){
        var advert = advertRepository.findById(id).orElse(null);
        if (advert == null)
        {
            return ResponseEntity.notFound().build();
        }
        advertRepository.delete(advert);
        return ResponseEntity.noContent().build();
    }
}
