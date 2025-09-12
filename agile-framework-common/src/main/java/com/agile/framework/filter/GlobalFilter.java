package com.agile.framework.filter;

import com.agile.framework.context.ContextManager;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局过滤器
 *
 * @author chenzhanshang
 */
public class GlobalFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
        try {
            chain.doFilter(req, res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ContextManager.removeContext();
        }
    }

}
