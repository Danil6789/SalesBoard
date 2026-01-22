package com.example.salesboard.mappers;

import com.example.salesboard.dtos.RegisterUserRequest;
import com.example.salesboard.dtos.UpdateUserRequest;
import com.example.salesboard.dtos.UserDto;
import com.example.salesboard.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterUserRequest request);

    UserDto toDto(User user);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
