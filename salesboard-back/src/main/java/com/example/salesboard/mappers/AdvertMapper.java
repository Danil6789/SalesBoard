package com.example.salesboard.mappers;

import com.example.salesboard.dtos.AdvertDto;
import com.example.salesboard.entities.Advert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdvertMapper {
    @Mapping(source = "category.id", target = "categoryId")
    AdvertDto toDto(Advert advert);

    Advert toEntity(AdvertDto request);

    @Mapping(target = "id", ignore = true)
    void update(AdvertDto advertDto, @MappingTarget Advert advert);
}
