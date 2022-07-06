package io.github.vedantwankhade.ghmail.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.vedantwankhade.ghmail.model.UnreadEmailStats;

@Repository
public interface UnreadEmailStatsRepository extends CassandraRepository<UnreadEmailStats, String> {
	
	List<UnreadEmailStats> findAllByUserId(String userId);
	
	@Query("update unread_email_stats set unreadcount = unreadcount + 1 where user_id = ?0 and label = ?1")
	public void incrementUnreadCount(String userId, String label);
	
	@Query("update unread_email_stats set unreadcount = unreadcount - 1 where user_id = ?0 and label = ?1")
	public void decrementUnreadCount(String userId, String label);
}
