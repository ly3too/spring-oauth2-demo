package cn.ly3too.oauthdemo.oauthserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity()
public class WebSecurityConf extends WebSecurityConfigurerAdapter {
    private static Logger log = LoggerFactory.getLogger(WebSecurityConfigurerAdapter.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * create two user: admin and user, the password is the same as username
     * @return
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager (
            User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .authorities("admin")
                .build(),
            User.withUsername("user")
                .password(passwordEncoder.encode("user"))
                .authorities("user")
                .build()
        ) {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("== query user: " + username);
                UserDetails ret = super.loadUserByUsername(username);
                log.info("== found user: " + ret);
                return ret;
            }
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
