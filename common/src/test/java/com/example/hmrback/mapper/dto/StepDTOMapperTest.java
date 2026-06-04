package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.Step;
import com.example.hmrback.model.request.CreateStepRequest;
import com.example.hmrback.model.request.UpdateStepRequest;
import com.example.hmrback.model.response.StepResponse;
import com.example.hmrback.utils.test.DtoTestUtils;
import com.example.hmrback.utils.test.ModelTestUtils;
import com.example.hmrback.utils.test.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StepDTOMapperTest")
class StepDTOMapperTest {

    @Test
    @DisplayName("Should convert CreateStepRequest to Step with id=null")
    void shouldConvertCreateStepRequestToStepWithNullId() {
        // Given
        CreateStepRequest request = DtoTestUtils.buildCreateStepRequest(TestConstants.NUMBER_1);

        // When
        Step step = StepDTOMapper.toDomainModel(request);

        // Then
        assertNotNull(step);
        assertNull(step.id());
        assertEquals(request.description(), step.description());
        assertEquals(request.order(), step.order());
    }

    @Test
    @DisplayName("Should convert UpdateStepRequest to Step with id preserved")
    void shouldPreserveIdWhenConvertingUpdateStepRequest() {
        // Given
        UpdateStepRequest request = DtoTestUtils.buildUpdateStepRequest(2L);

        // When
        Step step = StepDTOMapper.toDomainModel(request);

        // Then
        assertNotNull(step);
        assertEquals(request.id(), step.id());
        assertEquals(request.description(), step.description());
        assertEquals(request.order(), step.order());
    }

    @Test
    @DisplayName("Should convert Step to StepResponse with all fields preserved")
    void shouldConvertStepToStepResponse() {
        // Given
        Step step = ModelTestUtils.buildStep(1L, false);

        // When
        StepResponse response = StepDTOMapper.toDtoResponse(step);

        // Then
        assertNotNull(response);
        assertEquals(step.id(), response.id());
        assertEquals(step.description(), response.description());
        assertEquals(step.order(), response.order());
    }

    @Test
    @DisplayName("Should handle null CreateStepRequest gracefully")
    void shouldHandleNullCreateStepRequest() {
        // When
        Step step = StepDTOMapper.toDomainModel((CreateStepRequest) null);

        // Then
        assertNull(step);
    }

    @Test
    @DisplayName("Should handle null UpdateStepRequest gracefully")
    void shouldHandleNullUpdateStepRequest() {
        // When
        Step step = StepDTOMapper.toDomainModel((UpdateStepRequest) null);

        // Then
        assertNull(step);
    }

    @Test
    @DisplayName("Should handle null Step gracefully in toResponse")
    void shouldHandleNullStepInToResponse() {
        // When
        StepResponse response = StepDTOMapper.toDtoResponse(null);

        // Then
        assertNull(response);
    }

    @Test
    @DisplayName("Should convert list of CreateStepRequest to Steps with id=null")
    void shouldConvertCreateStepRequestListToSteps() {
        // Given
        List<CreateStepRequest> requests = DtoTestUtils.buildStepListForCreation(3);

        // When
        List<Step> steps = StepDTOMapper.toDomainModels(requests);

        // Then
        assertNotNull(steps);
        assertEquals(3, steps.size());
        steps.forEach(step -> assertNull(step.id()));
    }

    @Test
    @DisplayName("Should convert list of UpdateStepRequest to Steps with id preserved")
    void shouldConvertUpdateStepRequestListToSteps() {
        // Given
        List<UpdateStepRequest> requests = DtoTestUtils.buildStepListForUpdate(2);

        // When
        List<Step> steps = StepDTOMapper.toDomainModelsFromUpdate(requests);

        // Then
        assertNotNull(steps);
        assertEquals(2, steps.size());
        assertEquals(1L, steps.get(0).id());
        assertEquals(2L, steps.get(1).id());
    }

    @Test
    @DisplayName("Should convert list of Steps to StepResponses")
    void shouldConvertStepListToResponses() {
        // Given
        List<Step> steps = ModelTestUtils.buildStepList(3, false);

        // When
        List<StepResponse> responses = StepDTOMapper.toDtoResponses(steps);

        // Then
        assertNotNull(responses);
        assertEquals(3, responses.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(steps.get(i).id(), responses.get(i).id());
            assertEquals(steps.get(i).description(), responses.get(i).description());
        }
    }

    @Test
    @DisplayName("Should handle null list in toDomainModels - return empty list")
    void shouldHandleNullListInToDomainModels() {
        // When
        List<Step> steps = StepDTOMapper.toDomainModels(null);

        // Then
        assertNotNull(steps);
        assertTrue(steps.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty list in toDomainModels - return empty list")
    void shouldHandleEmptyListInToDomainModels() {
        // When
        List<Step> steps = StepDTOMapper.toDomainModels(List.of());

        // Then
        assertNotNull(steps);
        assertTrue(steps.isEmpty());
    }

    @Test
    @DisplayName("Should handle null list in toDomainModelsFromUpdate - return empty list")
    void shouldHandleNullListInToDomainModelsFromUpdate() {
        // When
        List<Step> steps = StepDTOMapper.toDomainModelsFromUpdate(null);

        // Then
        assertNotNull(steps);
        assertTrue(steps.isEmpty());
    }

    @Test
    @DisplayName("Should handle null list in toDtoResponses - return empty list")
    void shouldHandleNullListInToDtoResponses() {
        // When
        List<StepResponse> responses = StepDTOMapper.toDtoResponses(null);

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
}

