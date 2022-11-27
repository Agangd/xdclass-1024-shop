package net.xdclass.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Api(tags = "通知模块")
@RestController
@RequestMapping("/api/user/v1")
@Slf4j
public class NotifyController {

    @Autowired
    private Producer captchaProducer;


    @Autowired
    private RedisTemplate redisTemplate;

    private static final long CAPTCHA_CODE_EXPIRED = 60 * 1000 * 10;
    /**
     * 获取图形验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation("获取图形验证码")
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String captcha = captchaProducer.createText();
        log.info("图形验证码：{}",captcha);

        //存储
        redisTemplate.opsForValue().set(getCaptchaKey(request),captcha,CAPTCHA_CODE_EXPIRED,TimeUnit.MILLISECONDS);
//        redisTemplate.opsForValue().set("k3","v1");
        log.info("存入redis的键={}",getCaptchaKey(request));

        BufferedImage image = captchaProducer.createImage(captcha);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(image,"jpg",outputStream);
        } catch (IOException e) {
            log.info("获取图形验证码异常:{}",e);
        }finally {
            outputStream.flush();
            outputStream.close();
        }
    }


    /**
     * 获取缓存的key
     * @param request
     * @return
     */
    private String getCaptchaKey(HttpServletRequest request){
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");

        String key = "user-service:captcha:" + CommonUtil.MD5(ip + userAgent);

        log.info("ip={}",ip);
        log.info("UserAgent={}",userAgent);
        log.info("key={}",key);
        return key;
    }
}
