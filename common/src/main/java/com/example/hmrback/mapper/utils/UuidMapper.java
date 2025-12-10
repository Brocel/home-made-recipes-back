package com.example.hmrback.mapper.utils;

import com.example.hmrback.mapper.config.GlobalMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(config = GlobalMapperConfig.class)
public interface UuidMapper {

    @Named("uuidToString")
    default String asString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    default UUID asUuid(String str) {
        return str != null ? UUID.fromString(str) : null;
    }
}
