package com.example.hmrback.mapper.dto;

import com.example.hmrback.model.Step;
import com.example.hmrback.model.request.CreateStepRequest;
import com.example.hmrback.model.request.UpdateStepRequest;
import com.example.hmrback.model.response.StepResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps Step domain models to/from StepResponse and Step request DTOs.
 *
 * This utility mapper provides stateless, static transformations for Step entities:
 * - CreateStepRequest → Step (with id=null for new steps)
 * - UpdateStepRequest → Step (with id preserved from request)
 * - Step → StepResponse (for API responses)
 * - Batch operations for collections
 *
 * All methods are null-safe and handle empty collections gracefully.
 *
 * @since 1.0
 */
public final class StepDTOMapper {

    private StepDTOMapper() {
        // Utility class, no instantiation
    }

    /**
     * Converts a CreateStepRequest to a Step domain model.
     *
     * The resulting Step will have id=null (for new steps).
     *
     * @param request the CreateStepRequest to convert (may be null)
     * @return Step domain model with id=null, or null if input is null
     */
    public static Step toDomainModel(CreateStepRequest request) {
        if (request == null) {
            return null;
        }

        return new Step(
                null,                      // id is null for new steps
                request.description(),
                request.order()
        );
    }

    /**
     * Converts an UpdateStepRequest to a Step domain model.
     *
     * The resulting Step will have the id preserved from the request.
     *
     * @param request the UpdateStepRequest to convert (may be null)
     * @return Step domain model with id preserved, or null if input is null
     */
    public static Step toDomainModel(UpdateStepRequest request) {
        if (request == null) {
            return null;
        }

        return new Step(
                request.id(),              // id is preserved from request
                request.description(),
                request.order()
        );
    }

    /**
     * Converts a Step domain model to a StepResponse DTO.
     *
     * All fields are preserved: id, description, and order.
     *
     * @param step the Step domain model to convert (may be null)
     * @return StepResponse DTO, or null if input is null
     */
    public static StepResponse toDtoResponse(Step step) {
        if (step == null) {
            return null;
        }

        return new StepResponse(
                step.id(),
                step.description(),
                step.order()
        );
    }

    /**
     * Converts a list of CreateStepRequest objects to Step domain models.
     *
     * @param requests the list of CreateStepRequest objects (may be null or empty)
     * @return list of Step domain models, or empty list if input is null/empty
     */
    public static List<Step> toDomainModels(List<CreateStepRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream()
                .map(StepDTOMapper::toDomainModel)
                .toList();
    }

    /**
     * Converts a list of UpdateStepRequest objects to Step domain models.
     *
     * @param requests the list of UpdateStepRequest objects (may be null or empty)
     * @return list of Step domain models, or empty list if input is null/empty
     */
    public static List<Step> toDomainModelsFromUpdate(List<UpdateStepRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream()
                .map(StepDTOMapper::toDomainModel)
                .toList();
    }

    /**
     * Converts a list of Step domain models to StepResponse DTOs.
     *
     * @param steps the list of Step domain models (may be null or empty)
     * @return list of StepResponse DTOs, or empty list if input is null/empty
     */
    public static List<StepResponse> toDtoResponses(List<Step> steps) {
        if (steps == null || steps.isEmpty()) {
            return new ArrayList<>();
        }

        return steps.stream()
                .map(StepDTOMapper::toDtoResponse)
                .toList();
    }
}

