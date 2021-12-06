package com.fedexu.user.auth.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRole {
    private String username;
    private String role;
}
