package com.example.hmrback.mapper;

import com.example.hmrback.mapper.base.BaseMapper;
import com.example.hmrback.mapper.config.GlobalMapperConfig;
import com.example.hmrback.mapper.utils.DateMapper;
import com.example.hmrback.mapper.utils.UuidMapper;
import com.example.hmrback.model.User;
import com.example.hmrback.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    config = GlobalMapperConfig.class,
    uses = {
        DateMapper.class,
        RoleMapper.class,
        UuidMapper.class
    })
public interface UserMapper extends BaseMapper<User, UserEntity> {

    @Override
    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "localDateToString")
    @Mapping(source = "inscriptionDate", target = "inscriptionDate", qualifiedByName = "localDateToString")
    User toModel(UserEntity entity);

    @Override
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "stringToLocalDate")
    @Mapping(source = "inscriptionDate", target = "inscriptionDate", qualifiedByName = "stringToLocalDate")
    UserEntity toEntity(User model);

    @Override
    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inscriptionDate", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromModel(User model,  @MappingTarget UserEntity entity);
}
