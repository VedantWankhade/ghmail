package io.github.vedantwankhade.ghmail.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.model.UnreadEmailStats;
import io.github.vedantwankhade.ghmail.repository.UnreadEmailStatsRepository;

@Service
public class FolderService {

	@Autowired
	private UnreadEmailStatsRepository unreadEmailStatsRepository;

	public List<Folder> fetchDefaultFolders(String userId) {
		return Arrays.asList(new Folder(userId, "Inbox", "blue"), new Folder(userId, "Sent Items", "green"),
				new Folder(userId, "Important", "red"));
	}

	public Map<String, Integer> mapCountToLabels(String userId) {
		List<UnreadEmailStats> unreadStats = unreadEmailStatsRepository.findAllByUserId(userId);
		return unreadStats.stream()
				.collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
	}
}
