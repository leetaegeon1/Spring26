package com.sparta.spring26.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spring26.dto.BaseResponseDto;
import com.sparta.spring26.exception.BaseException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Order(1)
public class ServletFilterExceptionHandler extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) throws ServletException, IOException {
        try {
            fc.doFilter(req, res);
        } catch (BaseException e) {
            res.setStatus(e.getResponse().getStatus());
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter()
                    .write(objectMapper.writeValueAsString(new BaseResponseDto(e.getResponse())));
        }
    }
}
