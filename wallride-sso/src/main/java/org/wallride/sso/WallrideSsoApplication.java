package org.wallride.sso;

import com.kakawait.spring.security.cas.client.CasAuthorizationInterceptor;
import com.kakawait.spring.security.cas.client.ticket.ProxyTicketProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ForwardedHeaderFilter;

@SpringBootApplication
public class WallrideSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WallrideSsoApplication.class, args);
	}



	@Bean
	FilterRegistrationBean forwardedHeaderFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setFilter(new ForwardedHeaderFilter());
		filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegBean;
	}

	@Bean
	RestTemplate casRestTemplate(ServiceProperties serviceProperties, ProxyTicketProvider proxyTicketProvider) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new CasAuthorizationInterceptor(serviceProperties, proxyTicketProvider));
		return restTemplate;
	}
}
