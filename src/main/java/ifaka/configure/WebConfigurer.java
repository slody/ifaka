package ifaka.configure;

import ifaka.interceptor.AdminDoInterceptor;
import ifaka.interceptor.AdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xiaofeng
 * @date 2020/3/21 20:45
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new AdminInterceptor());
        registration.addPathPatterns("/:**","/:/**").excludePathPatterns("/:/login","/:/login.do");
        InterceptorRegistration registration1 = registry.addInterceptor(new AdminDoInterceptor());
        registration1.addPathPatterns("/do**","/do/**");
    }
}
