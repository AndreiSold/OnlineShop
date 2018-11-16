package ro.msg.learning.shop.configurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final UserApprovalHandler userApprovalHandler;
    @Qualifier("authenticationManagerBean")
    private final AuthenticationManager authenticationManager;

    @Value("${client_id}")
    private String clientId;

    @Value("${client_secret}")
    private String clientSecret;

    @Value("${grant_type}")
    private String grantType;

    @Value("${authorization_code}")
    private String authorizationCode;

    @Value("${refresh_token}")
    private String refreshToken;

    @Value("${implicit}")
    private String implicit;

    @Value("${scope_read}")
    private String scopeRead;

    @Value("${scope_write}")
    private String scopeWrite;

    @Value("${scope_trust}")
    private String scopeTrust;

    @Value("${access_token_validity_seconds}")
    private int accessTokenValiditySeconds;

    @Value("${refresh_token_validity_seconds}")
    private int refreshTokenValiditySeconds;

    @Value("${product_manager}")
    private String productManager;

    @Value("${customer}")
    private String customer;

    @Value("${administrator}")
    private String administrator;

    private static final String FALSE = "false";

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        List<String> authoritiesList = new ArrayList<>();

        if (!FALSE.equals(productManager)) authoritiesList.add(productManager);
        if (!FALSE.equals(customer)) authoritiesList.add(customer);
        if (!FALSE.equals(administrator)) authoritiesList.add(administrator);

        String[] authoritiesArray = authoritiesList.toArray(new String[0]);

        clients.inMemory()
            .withClient(clientId)
            .secret(clientSecret)
            .authorizedGrantTypes(grantType, authorizationCode, refreshToken, implicit)
            .authorities(authoritiesArray)
            .scopes(scopeRead, scopeWrite, scopeWrite)
            .accessTokenValiditySeconds(accessTokenValiditySeconds)
            .refreshTokenValiditySeconds(refreshTokenValiditySeconds);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
            .authenticationManager(authenticationManager);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.passwordEncoder(new BCryptPasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}