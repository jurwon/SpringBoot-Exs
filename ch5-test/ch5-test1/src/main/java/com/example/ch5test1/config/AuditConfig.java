package com.example.ch5test1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
//JPA의 Auditing기능 활성화
@EnableJpaAuditing
public class AuditConfig {

    //AuditorAware(등록자, 수정자 처리)을 빈으로 등록
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}