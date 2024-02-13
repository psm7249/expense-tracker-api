package com.springboot.restapi.appilaction.expensetrackerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.springboot.restapi.appilaction.expensetrackerapi.filter.AuthFilter;

@SpringBootApplication
public class ExpenseTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApiApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		filterRegistrationBean.setFilter(authFilter);
		filterRegistrationBean.addUrlPatterns("/api/categories/*");
        return filterRegistrationBean;
	}

}
