package sut.edu.zyp.dormitory.manage.service;

import java.awt.image.BufferedImage;

/**
 * 验证码服务
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
public interface CaptchaService {

    /**
     * 生成验证码
     *
     * @param number 验证码位数
     * @return 生成的随机验证码
     */
    String genCaptha(int number);

    /**
     * 根据验证码生成验证码图片
     *
     * @param verifyCode 验证码
     * @param interferenceLine 干扰线数量
     * @return 验证码图片
     */
    BufferedImage genCapthaImageByVerifyCode(String verifyCode,int interferenceLine);
}
