package com.fedexu.user.auth.user;

import com.fedexu.user.auth.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@RestController
@RequestMapping(value = "/")
public class UserController {

    @Autowired
    UserMapper userMapper;

    private final String BEARER = "Bearer";

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<AuthenticateResponse> login(@RequestBody User user){
        //Check if the user is OK
        User dbUser = userMapper.findByUsername(user.getUsername());
        if(isNull(dbUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if(!dbUser.getPassword().equals(user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        //Create the JWT
        dbUser.setJwt(jwtUtils.generateToken(dbUser));
        userMapper.updateJwt(dbUser);

        //Set Header with JWT
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(dbUser.getJwt());

        return ResponseEntity.ok().headers(headers).body(AuthenticateResponse.builder().jwttoken(dbUser.getJwt()).build());
    }

    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticateResponse> authenticate(){
        //Check if there is a JWT
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        if (isNull(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        token = token.replace(BEARER, "").trim();

        if(token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        //Check if the user is OK
        User dbUser = userMapper.findByUsername(jwtUtils.getUsernameFromToken(token));
        if(isNull(dbUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if(!dbUser.getJwt().equals(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if(!jwtUtils.validateToken(token, dbUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        AuthenticateResponse response = AuthenticateResponse.builder()
                .jwttoken(token)
                .roles(userMapper.getRoles(jwtUtils.getUsernameFromToken(token)).stream()
                        .map(UserRole::getRole)
                        .collect(Collectors.joining(",")))
                .build();

        //Set Header with JWT
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(dbUser.getJwt());
        return ResponseEntity.ok().headers(headers).body(response);
    }

}
