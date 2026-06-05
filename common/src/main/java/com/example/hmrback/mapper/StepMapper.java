package com.example.hmrback.mapper;

import com.example.hmrback.mapper.base.BaseMapper;
import com.example.hmrback.mapper.config.GlobalMapperConfig;
import com.example.hmrback.model.Step;
import com.example.hmrback.persistence.entity.StepEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface StepMapper extends BaseMapper<Step, StepEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    void updateEntityFromModel(Step step, @MappingTarget StepEntity entity);
}
