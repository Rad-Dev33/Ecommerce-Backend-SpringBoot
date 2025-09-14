package com.example.ecommercebackend.Security.Config;

import com.example.ecommercebackend.Security.Service.JWTService;
import com.example.ecommercebackend.Security.User.ECommerceUserDetailService;
import com.example.ecommercebackend.Security.User.EcommerceUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                String autHeader=request.getHeader("Authorization");
                String jwttoken=null;
                String username=null;

                if(autHeader!=null && autHeader.startsWith("Bearer ")){
                    jwttoken=autHeader.substring(7);
                    if(jwttoken==null || jwttoken.isBlank()){response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid JWT Token ");}
                    username=jwtService.getUsernameFromTAoken(jwttoken);
                }

                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null ){
                    UserDetails userDetails = context.getBean(ECommerceUserDetailService.class).loadUserByUsername(username);

                    try {
                        if(jwtService.validateToken(userDetails,jwttoken)){
                            UsernamePasswordAuthenticationToken aut=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            aut.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(aut);
                        }
                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
                    }
                }



        filterChain.doFilter(request,response);


    }
}
