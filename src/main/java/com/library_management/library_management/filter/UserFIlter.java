package com.library_management.library_management.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library_management.library_management.utility.ApiResponse;
import com.library_management.library_management.utility.JwtUtil;
import com.library_management.library_management.utility.UserInfo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserFIlter implements Filter{


    @Autowired
    JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException{

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        String userToken = httpRequest.getHeader("Authorization");
        // System.out.print
        if(userToken != null){
            try {
                UserInfo data = jwtUtil.extractUserData(userToken);
                httpRequest.setAttribute("userData", data);
            } 
            catch (ExpiredJwtException e) {
                // Handle token expiration with ApiResponse
                ApiResponse<String> response = new ApiResponse<>();
                response.setSuccess(false);
                response.setMessage("Token Expired");
                
                // Set response status to 401 Unauthorized and content type to JSON
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write(objectMapper.writeValueAsString(response)); // Serialize response to JSON
                httpResponse.flushBuffer();
                return;
            }
            catch(JwtException e){
                // Handle invalid token with ApiResponse
                ApiResponse<String> response = new ApiResponse<>();
                response.setSuccess(false);
                response.setMessage("Invalid Token");

                 // Set response status to 401 Unauthorized and content type to JSON
                 httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                 httpResponse.setContentType("application/json");
                 httpResponse.getWriter().write(objectMapper.writeValueAsString(response)); // Serialize response to JSON
                 httpResponse.flushBuffer();
                 return;
            }
        }
        else{
            // Authorization header missing with ApiResponse
            ApiResponse<String> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("Authorization Header Missing");

            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write(objectMapper.writeValueAsString(response)); // Serialize response to JSON
            httpResponse.flushBuffer();
            return; 
        }
        chain.doFilter(req, res);
    }

}
