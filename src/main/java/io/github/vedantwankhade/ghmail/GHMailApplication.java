package io.github.vedantwankhade.ghmail;

import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;

@SpringBootApplication
@RestController
public class GHMailApplication {

	@Autowired
	private FolderRepository folderRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(GHMailApplication.class, args);
	}
	
	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
	
	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("ishan407", "Inbox", "blue"));
		folderRepository.save(new Folder("ishan407", "Sent", "green"));
		folderRepository.save(new Folder("ishan407", "Important", "yellow"));
	}
}
