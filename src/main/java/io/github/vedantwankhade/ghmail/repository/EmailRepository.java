package io.github.vedantwankhade.ghmail.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import io.github.vedantwankhade.ghmail.model.Email;

@Repository
public interface EmailRepository extends CassandraRepository<Email, UUID> {
}
