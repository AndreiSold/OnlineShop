package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Role;
import ro.msg.learning.shop.exceptions.CustomerIdNotFoundException;
import ro.msg.learning.shop.repositories.CustomerRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
@ComponentScan("ro.msg.learning.shop.services")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomerRepository customerRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(s -> {

            final Customer customer = customerRepository.findByUsername(s);

            if (customer == null) {
                log.error("Given customer username does not exist!");
                throw new CustomerIdNotFoundException("This username does not exist!");
            }

            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return customer.getRoles().parallelStream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                }

                @Override
                public String getPassword() {
                    return customer.getPassword();
                }

                @Override
                public String getUsername() {
                    return s;
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/customer").permitAll()
            .antMatchers("/customer/profile").authenticated()
            .antMatchers("/customer/profile/*").authenticated()
            .antMatchers("/customer/register").permitAll()
            .antMatchers("/stocks/export-stocks-from-location/*").hasRole("ADMINISTRATOR")
            .antMatchers(HttpMethod.GET).permitAll()
            .antMatchers(HttpMethod.POST).authenticated()
            .antMatchers(HttpMethod.DELETE, "/customer/*").hasRole("ADMINISTRATOR")
            .anyRequest().denyAll()
            .and()
            .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
