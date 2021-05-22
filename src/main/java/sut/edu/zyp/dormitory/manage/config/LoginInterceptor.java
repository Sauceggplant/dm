package sut.edu.zyp.dormitory.manage.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截器
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            //通过session获取用户的已登录信息
            Object user = request.getSession().getAttribute("user");
            Object type = request.getSession().getAttribute("userType");
            //已登录信息不为空，说明已登录，不需要拦截
            if (user != null && type != null) {
                return true;
            }
            //需要拦截，并配置跳转登录
            response.sendRedirect(request.getContextPath() + "/login.html");
        } catch (IOException e) {
        }
        return false;
    }
}
