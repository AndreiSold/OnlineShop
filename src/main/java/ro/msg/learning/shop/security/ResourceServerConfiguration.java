package ro.msg.learning.shop.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "my_rest_api";
    private static final String ADMINISTRATOR_ROLE = "ADMINISTRATOR";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/customer/register").anonymous()
            .antMatchers(HttpMethod.GET, "/profile").authenticated()
            .antMatchers(HttpMethod.DELETE, "/customer/*").hasRole(ADMINISTRATOR_ROLE)
            .antMatchers(HttpMethod.POST, "/create-order").authenticated()
            .antMatchers(HttpMethod.GET, "/report/**").hasRole(ADMINISTRATOR_ROLE)
            .antMatchers(HttpMethod.GET, "/stock/export-stocks-from-location/*").hasRole(ADMINISTRATOR_ROLE)
            .and()
            .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}