package io.github.vedantwankhade.ghmail;

import java.nio.file.Path;
import java.util.Arrays;

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

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.github.vedantwankhade.ghmail.model.EmailListItem;
import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.model.utilmodel.EmailListItemCompoundKey;
import io.github.vedantwankhade.ghmail.repository.EmailListItemRepository;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;

@SpringBootApplication
@RestController
public class GHMailApplication {

	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private EmailListItemRepository emailListRepository;
	
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
		
		for (int i = 0; i < 10; i++) {
			EmailListItemCompoundKey key = new EmailListItemCompoundKey();
			key.setUserId("ishan407");
			key.setLabel("Inbox");
			key.setTimeUUID(Uuids.timeBased());
			
			EmailListItem email = new EmailListItem();
			email.setKey(key);
			email.setTo(Arrays.asList("ishan407"));
			email.setSubject("Subject " + i);
			email.setUnread(true);
			
			emailListRepository.save(email);
		}
	}
}
