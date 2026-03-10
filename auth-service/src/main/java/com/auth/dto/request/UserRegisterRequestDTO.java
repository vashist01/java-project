package com.auth.dto.request;

import lombok.Builder;

@Builder
public record UserRegisterRequestDTO(String firstName, String email, String phoneNumber) {
}
