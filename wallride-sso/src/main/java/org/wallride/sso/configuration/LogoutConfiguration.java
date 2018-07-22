package org.wallride.sso.configuration;

import com.kakawait.spring.boot.security.cas.CasSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Profile("!custom-logout")
@Configuration
public class LogoutConfiguration extends CasSecurityConfigurerAdapter {

    private final LogoutSuccessHandler casLogoutSuccessHandler;

    public LogoutConfiguration(LogoutSuccessHandler casLogoutSuccessHandler) {
        this.casLogoutSuccessHandler = casLogoutSuccessHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Allow GET method to /logout even if CSRF is enabled
        http.logout()
                .logoutSuccessHandler(casLogoutSuccessHandler)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }
}