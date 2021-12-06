package com.fedexu.user.auth.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateResponse {
    private final String jwttoken;
    private final String roles;
}
