package io.github.vedantwankhade.ghmail;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.github.vedantwankhade.ghmail.model.Email;
import io.github.vedantwankhade.ghmail.model.EmailListItem;
import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.model.utilmodel.EmailListItemCompoundKey;
import io.github.vedantwankhade.ghmail.repository.EmailListItemRepository;
import io.github.vedantwankhade.ghmail.repository.EmailRepository;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;
import io.github.vedantwankhade.ghmail.repository.UnreadEmailStatsRepository;
import io.github.vedantwankhade.ghmail.service.EmailService;

@SpringBootApplication
@RestController
public class GHMailApplication {

	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private UnreadEmailStatsRepository unreadEmailStatsRepository;
	
	@Autowired
	private EmailService emailService;
	
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
		folderRepository.save(new Folder("ishan407", "Work", "blue"));
		folderRepository.save(new Folder("ishan407", "Home", "green"));
		folderRepository.save(new Folder("ishan407", "Other", "yellow"));
	
		
		for (int i = 0; i < 10; i++) {
			emailService.sendEmail("ishan407", Arrays.asList("ishan407", "abc"), "Hello " + i, "Body " + i);
		}
		emailService.sendEmail("me", Arrays.asList("notMe"), "Hello ", "Body ");
	}
}
