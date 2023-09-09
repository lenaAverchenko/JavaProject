package telran.functionality.com.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import telran.functionality.com.service.UserDetailService;

/**
 * Class - Configuration for authorisation of user and access permission
 *
 * @author Olena Averchenko
 * */

@Configuration
@EnableConfigurationProperties
//@EnableWebSecurity
public class SecurityConfigBasicAuth extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailService userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers( "/test/**", "/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
//                .antMatchers("/test/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .antMatchers("/api/accounts/client/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/api/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
