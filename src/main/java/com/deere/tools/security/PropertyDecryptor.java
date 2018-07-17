package com.deere.tools.security;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import com.deere.tools.iaf.Rc4Key;

/**
 * Property Source Post processing in order to decrypt RC4 properties. See
 * https://stackoverflow.com/questions/31989883/process-spring-boot-externalized-property-values
 *
 */
@Order(HIGHEST_PRECEDENCE)
public class PropertyDecryptor implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final Logger LOG = LoggerFactory.getLogger(PropertyDecryptor.class);
	private static final String ENCRYPTION_KEY = "REGIONII";

	@Override
	public void initialize(final ConfigurableApplicationContext applicationContext) {
		final ConfigurableEnvironment environment = applicationContext.getEnvironment();

		final Map<String, Object> overrides = new LinkedHashMap<>();
		for (final PropertySource<?> source : environment.getPropertySources()) {
			final String name = source.getName();

			LOG.info("Decrypting {}", name);
			decrypt(source, overrides);
		}

		environment.getPropertySources().addFirst(new MapPropertySource("decrypted", overrides));
	}

	private void decrypt(final PropertySource<?> source, final Map<String, Object> overrides) {
		if (source instanceof EnumerablePropertySource) {
			final EnumerablePropertySource<?> enumerable = (EnumerablePropertySource<?>) source;
			for (final String key : enumerable.getPropertyNames()) {
				final Object rawValue = source.getProperty(key);
				if (rawValue instanceof String && Rc4Key.IsRecognizedEncryption(rawValue.toString())) {
					overrides.put(key, decrypt(rawValue.toString()));
				}
			}
		} else if (source instanceof CompositePropertySource) {
			for (final PropertySource<?> nested : ((CompositePropertySource) source).getPropertySources()) {
				decrypt(nested, overrides);
			}
		}
	}

	private String decrypt(final String value) {
		return new Rc4Key(ENCRYPTION_KEY).rc4(value.replaceAll("\\.", ","));
	}
}
