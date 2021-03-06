package com.apigateway.security.auth;


import com.apigateway.model.Permission;
import com.apigateway.model.Role;
import com.apigateway.model.User;
import com.apigateway.security.util.TokenUtils;
import com.apigateway.service.UserDetailsGrpcService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import proto.PermissionProto;
import proto.RoleProto;
import proto.UserDetailsResponseProto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Filter koji ce presretati SVAKI zahtev klijenta ka serveru 
// (sem nad putanjama navedenim u WebSecurityConfig.configure(WebSecurity web))
// Filter proverava da li JWT token postoji u Authorization header-u u zahtevu koji stize od klijenta
// Ukoliko token postoji, proverava se da li je validan. Ukoliko je sve u redu, postavlja se autentifikacija
// u SecurityContext holder kako bi podaci o korisniku bili dostupni u ostalim delovima aplikacije gde su neophodni
public class TokenAuthenticationFilter extends OncePerRequestFilter {


    private final TokenUtils tokenUtils;


    private final UserDetailsGrpcService userDetailsGrpcService;

    protected final Log myLogger = LogFactory.getLog(getClass());

    public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsGrpcService userDetailsGrpcService) {
        this.tokenUtils = tokenHelper;
        this.userDetailsGrpcService = userDetailsGrpcService;

    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        String username;

        String authToken = tokenUtils.getAPIToken(request);

        if (authToken == null) {
            authToken = tokenUtils.getToken(request);
        }
        try {

            if (authToken != null) {

                // 2. Citanje korisnickog imena iz tokena
                username = tokenUtils.getUsernameFromToken(authToken);

                if (username != null) {


                    User userDetails = getUserDetails(username);
                    // 4. Provera da li je prosledjeni token validan
                    if (Boolean.TRUE.equals(tokenUtils.validateToken(authToken, userDetails))) {

                        // 5. Kreiraj autentifikaciju
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

        } catch (ExpiredJwtException ex) {
            myLogger.debug("Token expired!");
        }

        // prosledi request dalje u sledeci filter
        chain.doFilter(request, response);
    }

    public User getUserDetails(String username) {
        UserDetailsResponseProto proto = userDetailsGrpcService.getUserDetails(username);
        return new User(proto.getId(), proto.getUsername(), proto.getPassword(), getRoles(proto));
    }

    private List<Role> getRoles(UserDetailsResponseProto proto) {
        List<Role> roles = new ArrayList<>();
        for (RoleProto roleProto : proto.getRoleList()) {
            Set<Permission> permissions = new HashSet<>();
            for (PermissionProto permProto : roleProto.getPermissionsList()) {
                permissions.add(new Permission(permProto.getId(), permProto.getName()));
            }
            Role role = new Role(roleProto.getId(), roleProto.getName(), permissions);
            roles.add(role);
        }
        return roles;
    }

}
