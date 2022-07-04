package io.github.vedantwankhade.ghmail.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.vedantwankhade.ghmail.model.Email;
import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.repository.EmailListItemRepository;
import io.github.vedantwankhade.ghmail.repository.EmailRepository;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;
import io.github.vedantwankhade.ghmail.service.FolderService;

@Controller
public class EmailViewController {
	
	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private EmailRepository emailRepository;
	
	@Autowired
	private EmailListItemRepository emailListItemRepository;
	
	@Autowired
	private FolderService folderService;
	
	@GetMapping(value = "/emails/{id}")
	public String emailView(@PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {

		if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
			return "index";

		String userId = principal.getAttribute("login");

		model.addAttribute("userId", userId);

		List<Folder> userFolders = folderRepository.findAllByUserId(userId);
		model.addAttribute("userFolders", userFolders);

		List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);

		Optional<Email> optionalEmail = emailRepository.findById(id);
		
		if (optionalEmail.isEmpty())
			return "inbox-page";
		
		Email email = optionalEmail.get();
		model.addAttribute("email", email);
		String toIds = String.join(", ", email.getTo());
		
		model.addAttribute("toIds", toIds);
		return "email-page";
	}
}
