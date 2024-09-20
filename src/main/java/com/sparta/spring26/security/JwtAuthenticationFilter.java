package com.sparta.spring26.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spring26.dto.BaseResponseDto;
import com.sparta.spring26.dto.UserLogin;
import com.sparta.spring26.enums.BaseResponseEnum;
import com.sparta.spring26.exception.HandleNotFoundException;
import com.sparta.spring26.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        log.info("로그인 시도");
        try {

            UserLogin userLogin = objectMapper.readValue(req.getInputStream(), UserLogin.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getEmail(),
                            userLogin.getPassword(),
                            null
                    )
            );

        } catch (Exception e) {
            HandleNotFoundException exception = new HandleNotFoundException(BaseResponseEnum.USER_NOT_FOUND);
            req.setAttribute("exception", exception);
            log.error(exception.getResponse().getMessage());
            throw new HandleNotFoundException(BaseResponseEnum.USER_NOT_FOUND);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetailsImpl user = (UserDetailsImpl) authResult.getPrincipal();
        String jwt = jwtUtil.createToken(user.getEmail(),user.getUser().getRole());
        jwtUtil.addJwtToHeader(jwt, res);
        log.info("JWT 인증 로직 시도 끝");
        log.info("로그인 시도 끝");
        BaseResponseEnum responseEnum = BaseResponseEnum.USER_LOGIN_SUCCESS;
        res.setStatus(responseEnum.getStatus());
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter()
                .write(objectMapper.writeValueAsString(new BaseResponseDto(responseEnum)));
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {
        log.error("로그인 실패로직 시작");
        res.setStatus(401);
    }



}
