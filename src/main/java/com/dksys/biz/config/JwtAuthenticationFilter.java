//package com.dksys.biz.config;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
//
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends GenericFilterBean {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//    	HttpServletRequest req = (HttpServletRequest) request;
//    	HttpServletResponse res = (HttpServletResponse) response;
//        // 헤더에서 JWT 를 받아옵니다.
//    	String[] excludeURL = {"login", "join", "static", "noAuth", "/error"};
//    	boolean passUrl = false;
//    	String relUrl = req.getRequestURI();
//    	for (int i = 0; i < excludeURL.length; i++) {
//     		if(relUrl.contains(excludeURL[i])) {
//    			passUrl = true;
//    		}
//		}
//        String token = jwtTokenProvider .resolveToken(req);
//        // 유효한 토큰인지 확인합니다.
//        if (passUrl) {
//        	
//    	} else if (token != null && jwtTokenProvider.validateToken(token)) {
//            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            // SecurityContext 에 Authentication 객체를 저장합니다.
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } else {
//        	res.sendRedirect("/noAuth");
//        	return;
//        }
//        chain.doFilter(request, response);
//    }
//}
