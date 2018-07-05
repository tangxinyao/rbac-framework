package cn.tangxinyao.thrift.api.security.service;
import cn.tangxinyao.thrift.api.security.domain.JwtUser;
import cn.tangxinyao.thrift.api.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserService userDetailService;

    private String tokenKey = "Authorization";
    private String tokenPrefix = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(tokenKey);
        if(tokenHeader != null){
            tokenHeader = tokenHeader.replace("%20"," ");
        }
        if (tokenHeader != null && tokenHeader.startsWith(tokenPrefix)) {
            final String token = tokenHeader.substring(tokenPrefix.length());
            String username = JwtUtil.getUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                JwtUser jwtUser = userDetailService.loadUserByUsername(username);
                if (jwtUser.isEnabled() && JwtUtil.isNotExpired(token)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            jwtUser, null, jwtUser.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }


}
