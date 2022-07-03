package io.github.vedantwankhade.ghmail;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "datastax.astra")
public class DataStaxAstraProperties {
	private File secureConnectBundle; 
}
