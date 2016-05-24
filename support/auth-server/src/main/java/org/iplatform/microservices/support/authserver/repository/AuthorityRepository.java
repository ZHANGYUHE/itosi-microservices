package org.iplatform.microservices.support.authserver.repository;


import org.iplatform.microservices.support.authserver.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
