package org.halfchord.config;

import interceptor.EmailSMSInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public EmailSMSInterceptor getEmailSMSInterceptor() {
        return new EmailSMSInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getEmailSMSInterceptor())
                .addPathPatterns("/passport/getSMSCode");
    }
}
