package com.fedexu.user.auth.user;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {
    String username;
    String password;
    String jwt;
    Date lastActivity;
}
