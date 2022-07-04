package io.github.vedantwankhade.ghmail.controller;

import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.github.vedantwankhade.ghmail.model.EmailListItem;
import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.repository.EmailListItemRepository;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;
import io.github.vedantwankhade.ghmail.service.FolderService;

@Controller
public class InboxController {
	
	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private EmailListItemRepository emailListItemRepository;
	
	@Autowired
	private FolderService folderService;

	@GetMapping(value = "/")
	public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
		
		if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
			return "index";
		
		String userId = principal.getAttribute("login");
		
		model.addAttribute("userId", userId);
		
		List<Folder> userFolders = folderRepository.findAllByUserId(userId);
		model.addAttribute("userFolders", userFolders);
		
		List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);
		
		String folderLabel = "Inbox";
		List<EmailListItem> emails = emailListItemRepository.findAllByKey_UserIdAndKey_Label(userId, folderLabel);
		
		PrettyTime pt = new PrettyTime();
		emails.stream().forEach(email -> {
			Date time = new Date(Uuids.unixTimestamp(email.getKey().getTimeUUID()));
			email.setHumanTime(pt.format(time));
		});
		
		model.addAttribute("emailList", emails);
		
		return "inbox-page";
	}
}
