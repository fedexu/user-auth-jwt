package com.fedexu.user.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SpringBootApplication
@MapperScan("com.fedexu.user.auth")
public class UserAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAuthApplication.class, args);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void creteSchema() throws IOException {
        List<String> DDL = Arrays.stream(
                StreamUtils.copyToString(new ClassPathResource("schema.sql").getInputStream(), Charset.defaultCharset())
                        .split(";")).map(String::trim).filter(Predicate.not(String::isEmpty)).collect(Collectors.toList());
        List<String> DML = Arrays.stream(
                StreamUtils.copyToString(new ClassPathResource("secret.sql").getInputStream(), Charset.defaultCharset())
                        .split(";")).map(String::trim).filter(Predicate.not(String::isEmpty)).collect(Collectors.toList());

        DDL.stream().forEach(ddl ->jdbcTemplate.execute(ddl));
        DML.stream().forEach(dml ->jdbcTemplate.execute(dml));
    }

}
