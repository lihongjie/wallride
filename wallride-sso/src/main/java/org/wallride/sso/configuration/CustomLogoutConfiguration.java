package org.wallride.sso.configuration;

import com.kakawait.spring.boot.security.cas.CasSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Profile("custom-logout")
@Configuration
public class CustomLogoutConfiguration extends CasSecurityConfigurerAdapter {

    private final LogoutSuccessHandler casLogoutSuccessHandler;

    public CustomLogoutConfiguration(LogoutSuccessHandler casLogoutSuccessHandler) {
        this.casLogoutSuccessHandler = casLogoutSuccessHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.logout()
                .permitAll()
                // Add null logoutSuccessHandler to disable CasLogoutSuccessHandler
                .logoutSuccessHandler(null)
                .logoutSuccessUrl("/logout.html")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        LogoutFilter filter = new LogoutFilter(casLogoutSuccessHandler, new SecurityContextLogoutHandler());
        filter.setFilterProcessesUrl("/cas/logout");
        http.addFilterBefore(filter, LogoutFilter.class);
    }
}