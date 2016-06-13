package org.iplatform.microservices.support.authserver.config;

import java.awt.Color;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/*
kaptcha.border  是否有边框  默认为true  我们可以自己设置yes，no  
kaptcha.border.color   边框颜色   默认为Color.BLACK  
kaptcha.border.thickness  边框粗细度  默认为1  
kaptcha.producer.impl   验证码生成器  默认为DefaultKaptcha  
kaptcha.textproducer.impl   验证码文本生成器  默认为DefaultTextCreator  
kaptcha.textproducer.char.string   验证码文本字符内容范围  默认为abcde2345678gfynmnpwx  
kaptcha.textproducer.char.length   验证码文本字符长度  默认为5  
kaptcha.textproducer.font.names    验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)  
kaptcha.textproducer.font.size   验证码文本字符大小  默认为40  
kaptcha.textproducer.font.color  验证码文本字符颜色  默认为Color.BLACK  
kaptcha.textproducer.char.space  验证码文本字符间距  默认为2  
kaptcha.noise.impl    验证码噪点生成对象  默认为DefaultNoise  
kaptcha.noise.color   验证码噪点颜色   默认为Color.BLACK  
kaptcha.obscurificator.impl   验证码样式引擎  默认为WaterRipple  
kaptcha.word.impl   验证码文本字符渲染   默认为DefaultWordRenderer  
kaptcha.background.impl   验证码背景生成器   默认为DefaultBackground  
kaptcha.background.clear.from   验证码背景颜色渐进   默认为Color.LIGHT_GRAY  
kaptcha.background.clear.to   验证码背景颜色渐进   默认为Color.WHITE  
kaptcha.image.width   验证码图片宽度  默认为200  
kaptcha.image.height  验证码图片高度  默认为50
 * */
@Configuration
public class KaptchaConfig {

	private DefaultKaptcha kaptcha;

	@Bean(name = "captchaProducer")
	public DefaultKaptcha getKaptchaProducer() {
		if (null == kaptcha) {
			kaptcha = new DefaultKaptcha();
			kaptcha.setConfig(getKaptchaConfig());
		}
		return kaptcha;
	}

	@Bean
	public Config getKaptchaConfig() {
		return new Config(kaptchaProperties());
	}

	private Properties kaptchaProperties() {
		Properties properties = new Properties();
		properties.put("kaptcha.border", "no");
		properties.put("kaptcha.image.width", 200);
		properties.put("kaptcha.image.height", 60);
		properties.put("kaptcha.textproducer.char.string","abcde2345678gfynmnpwx");
		properties.put("kaptcha.textproducer.char.length",5);
		properties.put("kaptcha.textproducer.font.color",Color.RED);		
		
		return properties;
	}
}