package com.project.readers_community.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for token refresh request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {
    
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
