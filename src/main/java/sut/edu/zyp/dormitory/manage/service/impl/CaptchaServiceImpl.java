package sut.edu.zyp.dormitory.manage.service.impl;

import org.springframework.stereotype.Service;
import sut.edu.zyp.dormitory.manage.service.CaptchaService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码服务实现类
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    /**
     * 验证码范围，[0~9][a~z][A~Z]，其中为了避免输入错误，去掉 l,o,O
     */
    private final char[] code = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 随机字体
     */
    final private String[] fontNames = new String[]{
            "黑体", "宋体", "Courier", "Arial",
            "Verdana", "Times", "Tahoma", "Georgia"};
    /**
     * 随机字体样式
     */
    final private int[] fontStyles = new int[]{
            Font.BOLD, Font.ITALIC | Font.BOLD
    };

    /**
     * 验证码图片字体大小
     */
    private static int fontsize = 21;

    /**
     * 验证码图片高度
     */
    private int height = fontsize + 12;

    @Override
    public String genCaptha(int length) {
        int len = code.length;
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(len);
            stringBuffer.append(code[index]);
        }
        return stringBuffer.toString();
    }

    @Override
    public BufferedImage genCapthaImageByVerifyCode(String verifyCode, int interferenceLine) {
        //验证码图片宽度
        int width = (fontsize + 1) * verifyCode.length() + 10;
        //创建验证码图片
        BufferedImage rotateVcodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotateVcodeImage.createGraphics();
        //填充背景色
        g2d.setColor(new Color(246, 240, 250));
        g2d.fillRect(0, 0, width, height);
        //画干扰线
        interferenceLine(g2d, interferenceLine, width, height);
        //在图片上画验证码
        for (int i = 0; i < verifyCode.length(); i++) {
            //获取每个验证码字符的图片信息
            BufferedImage rotateImage = getRotateImage(verifyCode.charAt(i));
            g2d.drawImage(rotateImage, null, (int) (this.height * 0.7) * i, 0);
        }
        g2d.dispose();
        return rotateVcodeImage;
    }

    /**
     * 为验证码图片画一些干扰线
     *
     * @param g                图片
     * @param interferenceLine 干扰线
     * @param width            宽度
     * @param height           高度
     */
    private void interferenceLine(Graphics g, int interferenceLine, int width, int height) {
        Random ran = new Random();
        for (int i = 0; i < interferenceLine; i++) {
            int x1 = ran.nextInt(width);
            int y1 = ran.nextInt(height);
            int x2 = ran.nextInt(width);
            int y2 = ran.nextInt(height);
            //设置随机颜色
            g.setColor(getRandomColor());
            //画干扰线
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * RGB均在220以下，这样看起来比较清晰
     * 默认RGB都是取自[0~255]
     *
     * @return 返回一个随机颜色
     */
    private Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(220), ran.nextInt(220), ran.nextInt(220));
    }

    /**
     * 获取一张旋转的图片
     *
     * @param c 要画的字符
     * @return 带有旋转的图片
     */
    private BufferedImage getRotateImage(char c) {
        BufferedImage rotateImage = new BufferedImage(height, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotateImage.createGraphics();
        //设置透明度为0
        g2d.setColor(new Color(255, 255, 255, 0));
        g2d.fillRect(0, 0, height, height);
        //获取随机量
        Random ran = new Random();
        //根据随机量，设置随机的字体和字体样式
        g2d.setFont(new Font(fontNames[ran.nextInt(fontNames.length)], fontStyles[ran.nextInt(fontStyles.length)], fontsize));
        g2d.setColor(getRandomColor());
        //获取随机旋转角度
        double theta = getTheta();
        //旋转图片
        g2d.rotate(theta, height / 2, height / 2);
        //绘制字符
        g2d.drawString(Character.toString(c), (height - fontsize) / 2, fontsize + 5);
        g2d.dispose();
        return rotateImage;
    }

    /**
     * 随机的旋转角度
     *
     * @return 角度
     */
    private double getTheta() {
        return ((int) (Math.random() * 1000) % 2 == 0 ? -1 : 1) * Math.random();
    }
}
