package com.example.salesboard.controllers;

import com.example.salesboard.dtos.AdvertDto;
import com.example.salesboard.entities.Advert;
import com.example.salesboard.mappers.AdvertMapper;
import com.example.salesboard.services.AdvertService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/adverts")
public class AdvertController {
    private final AdvertService advertService;
    private final AdvertMapper advertMapper;

    @PostMapping
    public ResponseEntity<AdvertDto> createAdvert(
            UriComponentsBuilder uriBuilder,
            @RequestBody AdvertDto advertDto,
            @AuthenticationPrincipal Long userId
    ){
        AdvertDto createdAdvert = advertService.createAdvert(advertDto, userId);
        if(createdAdvert == null){
            return ResponseEntity.badRequest().build();
        }
        var uri = uriBuilder.path("/adverts/{id}").buildAndExpand(createdAdvert.getId()).toUri();
        return ResponseEntity.created(uri).body(createdAdvert);
    }

    @GetMapping
    public ResponseEntity<List<AdvertDto>> getAllAdverts(){
        List<Advert> adverts = advertService.getAllAdverts();
        List<AdvertDto> advertsDto = adverts.stream().map(advertMapper::toDto).toList();
        return ResponseEntity.ok(advertsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertDto> findById(@PathVariable Long id){
        Advert advert = advertService.getAdvertById(id);
        if(advert == null){
            return ResponseEntity.notFound().build();
        }
        AdvertDto advertDto = advertMapper.toDto(advert);
        return ResponseEntity.ok(advertDto);
    }
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AdvertDto>> getMyAdverts(@AuthenticationPrincipal Long userId ){
        List<Advert> myAdverts = advertService.getMyAdverts(userId);
        List<AdvertDto> myAdvertsDto = myAdverts.stream().map(advertMapper::toDto).toList();
        return ResponseEntity.ok(myAdvertsDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertDto> updateAdvert(
            @PathVariable("id") Long id,
            @RequestBody AdvertDto advertDto
    ){
        AdvertDto updatedAdvert = advertService.updateAdvert(id, advertDto);
        if(updatedAdvert == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedAdvert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvert(@PathVariable Long id){
        boolean deleted = advertService.deleteAdvert(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}