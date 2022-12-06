package net.xdclass.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.model.LoginUser;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JWTUtil;
import net.xdclass.util.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("请求的路劲为[{}]-[{}]",request.getMethod(),request.getRequestURI());

        String accessToken = request.getHeader("token");
        if (accessToken == null){
            accessToken = request.getParameter("token");
        }

        if (StringUtils.isNotBlank(accessToken)){
            //不为空
            Claims claims = JWTUtil.checkJWT(accessToken);
            if (claims == null){
                //未登录
                CommonUtil.sendJsonMessage(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN));
                return false;
            }

            long userId = Long.valueOf(claims.get("id").toString());
            String headImg = (String) claims.get("head_img").toString();
            String name = (String) claims.get("name").toString();
            String mail = (String) claims.get("mail").toString();

//            LoginUser loginUser = new LoginUser();
//            loginUser.setName(name);
//            loginUser.setId(userId);
//            loginUser.setHeadImg(headImg);
//            loginUser.setMail(mail);

            LoginUser loginUser = LoginUser
                    .builder()
                    .headImg(headImg)
                    .name(name)
                    .id(userId)
                    .mail(mail).build();

//          通过attribute传递信息
//          request.setAttribute("loginUser",loginUser);

            //通过threadLocal传递用户信息 TODO
            threadLocal.set(loginUser);

            return true;
        }

        CommonUtil.sendJsonMessage(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}