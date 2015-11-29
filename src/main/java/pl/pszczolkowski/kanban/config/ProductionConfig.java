package pl.pszczolkowski.kanban.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import pl.pszczolkowski.kanban.shared.constant.Profiles;

@Configuration
@PropertySource("classpath:/defaultConfig.properties")
@Profile({Profiles.PRODUCTION, Profiles.DEVELOPMENT})
public class ProductionConfig {

}
