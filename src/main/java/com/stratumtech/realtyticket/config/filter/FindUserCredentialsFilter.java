package com.stratumtech.realtyticket.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FindUserCredentialsFilter extends OncePerRequestFilter {
    private static final String HEADER_USER_ROLE = "X-USER-ROLE";
    private static final String HEADER_USER_UUID = "X-USER-UUID";
    private static final String HEADER_ADMIN_REGION_ID = "X-ADMIN-REGION-ID";
    private static final String HEADER_ADMIN_REFERRAL_CODE = "X-ADMIN-REFERRAL-CODE";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String roleHeader = request.getHeader(HEADER_USER_ROLE);
        String uuidHeader = request.getHeader(HEADER_USER_UUID);
        String regionIdHeader = request.getHeader(HEADER_ADMIN_REGION_ID);
        String referralCodeHeader = request.getHeader(HEADER_ADMIN_REFERRAL_CODE);


        if ((roleHeader != null && !roleHeader.isBlank()) &&
            (uuidHeader != null && !uuidHeader.isBlank())) {
            String role = "ROLE_" + roleHeader.toUpperCase();

            var authority = new SimpleGrantedAuthority(role);
            Map<String, Object> details = new HashMap<>();

            if(role.endsWith("REGION_ADMIN")){
                details.put("adminRegionId", regionIdHeader);
                details.put("adminReferralCode", referralCodeHeader);
            }

            var authentication = new UsernamePasswordAuthenticationToken(
                    UUID.fromString(uuidHeader), null, Collections.singletonList(authority));
            authentication.setDetails(details);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
} 