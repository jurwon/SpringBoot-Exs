package com.example.ch5test1.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    
    //optional 타입 : 널체크, 반환타입이 존재하면 그 반환타입으로 받고 null반환해도 예외 발생 하지 않음
    @Override
    public Optional<String> getCurrentAuditor() {
        // 시큐리티 특성상, 인증 절차 거칠 때,
        // 멤버 서비스에서, 해당 이메일로 인증을, 로그인 했기 때문에
        // 로그인 정보를 시큐리티가 가지고 있음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }

}