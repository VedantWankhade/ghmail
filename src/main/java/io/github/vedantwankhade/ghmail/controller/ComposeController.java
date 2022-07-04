package io.github.vedantwankhade.ghmail.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;
import io.github.vedantwankhade.ghmail.service.EmailService;
import io.github.vedantwankhade.ghmail.service.FolderService;

@Controller
public class ComposeController {

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private FolderService folderService;

	@Autowired
	private EmailService emailService;
	
	@GetMapping(value = "/compose")
	public String getComposePage(@RequestParam(required = false) String to,
			@AuthenticationPrincipal OAuth2User principal, Model model) {
		if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
			return "index";

		String userId = principal.getAttribute("login");

		model.addAttribute("userId", userId);

		List<Folder> userFolders = folderRepository.findAllByUserId(userId);
		model.addAttribute("userFolders", userFolders);

		List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);

		List<String> uniqueToIds = splitToIds(to);
		model.addAttribute("toIds", String.join(", ", uniqueToIds));
		
		return "compose-page";
	}

	private List<String> splitToIds(String to) {
		if (!StringUtils.hasText(to))
			return new ArrayList<String>();

		return Arrays.asList(to.split(",")).stream().map(id -> StringUtils.trimWhitespace(id))
				.filter(id -> StringUtils.hasText(id)).distinct().collect(Collectors.toList());
	}

	@PostMapping(value = "/sendEmail")
	public ModelAndView sendEmail(@RequestBody MultiValueMap<String, String> formDataMap,
			@AuthenticationPrincipal OAuth2User principal) {

		if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
			return new ModelAndView("redirect:/");
		
		String from = principal.getAttribute("login");
		List<String> toIds = splitToIds(formDataMap.getFirst("toIds"));
		String subject = formDataMap.getFirst("subject");
		String body = formDataMap.getFirst("body");

		emailService.sendEmail(from, toIds, subject, body);
		
		return new ModelAndView("redirect:/?folder=Sent%20Items");
	}

}
