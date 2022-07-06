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

@SpringBootApplication
@RestController
public class GHMailApplication {

	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private EmailListItemRepository emailListRepository;
	
	@Autowired
	private EmailRepository emailRepository;
	
	@Autowired
	private UnreadEmailStatsRepository unreadEmailStatsRepository;
	
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
		
		unreadEmailStatsRepository.incrementUnreadCount("ishan407", "Inbox");
		unreadEmailStatsRepository.incrementUnreadCount("ishan407", "Inbox");
		unreadEmailStatsRepository.incrementUnreadCount("ishan407", "Inbox");
		
		for (int i = 0; i < 10; i++) {
			EmailListItemCompoundKey key = new EmailListItemCompoundKey();
			key.setUserId("ishan407");
			key.setLabel("Inbox");
			key.setTimeUUID(Uuids.timeBased());
			
			EmailListItem emailItem = new EmailListItem();
			emailItem.setKey(key);
			emailItem.setTo(Arrays.asList("ishan407"));
			emailItem.setSubject("Subject " + i);
			emailItem.setUnread(true);
			emailListRepository.save(emailItem);
			
			Email email = new Email();
			email.setTimeUUID(key.getTimeUUID());
			email.setFrom("ishan407");
			email.setSubject(emailItem.getSubject());
			email.setBody("Body " + i);
			email.setTo(emailItem.getTo());
			emailRepository.save(email);
		}
	}
}
