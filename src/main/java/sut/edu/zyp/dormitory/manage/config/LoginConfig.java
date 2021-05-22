package sut.edu.zyp.dormitory.manage.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.persistence.Entity;
import java.util.Set;

/**
 * 登录配置
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
                final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
                provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
                final Set<BeanDefinition> beans = provider.findCandidateComponents("sut.edu.zyp.dormitory.manage.entity");
                for (final BeanDefinition bean : beans) {
                    try {
                        config.exposeIdsFor(Class.forName(bean.getBeanClassName()));
                    } catch (final ClassNotFoundException e) {
                        throw new IllegalStateException("Failed to expose `id` field due to", e);
                    }
                }
            }
        };
    }

    @Bean
    public MappedInterceptor dataRestMappedInterceptor() {
        return new MappedInterceptor(new String[]{"/**"}, new String[]{
                "/login",
                "/captcha",
                "/login.html",
                "/400.html",
                "/500.html",
                "/**/*.js",
                "/**/*.css",
                "/**/*.woff",
                "/**/*.woff2",
                "/**/*.ttf",
                "/**/*.eot",
                "/**/*.otf",
                "/**/*.svg",
                "/**/*.less",
                "/**/*.scss",
                "/**/*.jpg",
                "/**/*.ico",
                "/**/*.jpeg",
                "/**/*.png",
                "/**/*.bmp"}, new LoginInterceptor());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册LoginInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
        //所有路径都被拦截
        registration.addPathPatterns("/**");
        //添加不拦截路径
        registration.excludePathPatterns(
                "/login",
                "/captcha",
                "/login.html",
                "/400.html",
                "/500.html",
                "/**/*.js",
                "/**/*.css",
                "/**/*.woff",
                "/**/*.woff2",
                "/**/*.ttf",
                "/**/*.eot",
                "/**/*.otf",
                "/**/*.svg",
                "/**/*.less",
                "/**/*.scss",
                "/**/*.jpg",
                "/**/*.ico",
                "/**/*.jpeg",
                "/**/*.png",
                "/**/*.bmp"
        );
    }
}
