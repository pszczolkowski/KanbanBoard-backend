package pl.pszczolkowski.kanban.config.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import pl.pszczolkowski.kanban.web.converter.LocalDateTimeDeserializer;
import pl.pszczolkowski.kanban.web.converter.LocalDateTimeSerializer;

@Configuration
@EnableWebMvc
public class WebMvcConfigurator extends WebMvcConfigurerAdapter {

	@Value("${server.contextPath}")
	private String contextPath;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

		builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
		builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());

		converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
		converters.add(new ByteArrayHttpMessageConverter());

	}

	@Override
	public void configureCors(CorsConfigurer configurer) {
		configurer.enableCors("/**").allowedMethods("GET", "POST", "PUT", "DELETE");
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(-1);
		return messageSource;
	}

}
