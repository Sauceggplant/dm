package sut.edu.zyp.dormitory.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 应用启动，主程序入口
 * spring boot 框架自带tomcat，对于jar包可以直接启动
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@EnableJpaAuditing
@SpringBootApplication
public class DmApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmApplication.class, args);
    }

}
