package ro.msg.learning.shop.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.entities.Role;
import ro.msg.learning.shop.exceptions.CustomerIdNotFoundException;
import ro.msg.learning.shop.mappers.CustomerDtoMapper;
import ro.msg.learning.shop.repositories.CustomerRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@EnableWebSecurity
@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomerRepository customerRepository;
    private final CustomerDtoMapper customerDtoMapper;

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
            .antMatchers(HttpMethod.GET).permitAll()
            .antMatchers(HttpMethod.POST).authenticated()
            .antMatchers(HttpMethod.DELETE, "/customer/delete").hasRole("ADMINISTRATOR")
            .anyRequest().permitAll()
            .and()
            .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public String currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }

        return null;
    }

//    @Bean
//    public Customer currentUser2() {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null) {
//            return null;
//        }
//
////        if (!(authentication instanceof AnonymousAuthenticationToken)) {
////            return authentication.getName();
////        }
//
//        return customerRepository.findByUsername(authentication.getName());
//    }

}
