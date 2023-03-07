package cn.alvasw.starter;

import cn.alvasw.plugin.PagePlugin;
import cn.alvasw.web.aspect.PageAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ALsW
 * @version 1.0.0
 * @date 2023-03-07
 */
@Configuration
public class PageHelperConfiguration {

	@Bean
	public PagePlugin pagePlugin() {
		return new PagePlugin();
	}

	@Bean
	public PageAspect pageAspect() {
		return new PageAspect();
	}

}
