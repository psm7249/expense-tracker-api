package com.springboot.restapi.appilaction.expensetrackerapi.filter;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.springboot.restapi.appilaction.expensetrackerapi.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends GenericFilterBean{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse =(HttpServletResponse) response;
        
        String authHeader = httpServletRequest.getHeader("Authorization");
        if(authHeader != null){
            String[] authHeaderArr = authHeader.split("Bearer ");
            if(authHeaderArr.length>1 && authHeaderArr[1] != null){
                String token = authHeaderArr[1];
                Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(token).getBody();
                httpServletRequest.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));
            } else{
                httpServletResponse.sendError(HttpStatus.FORBIDDEN.value() ,"Auth token expired or invalid");
            }
        } else {
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value() ,"Pass Authorizatio Header");
            return;
        }
        chain.doFilter(request,response);
    }
}
