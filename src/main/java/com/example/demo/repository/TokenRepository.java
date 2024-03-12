package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Long id);
    @Query(value = """
      select case when count(t) > 0 then true else false end\s
      from Token t\s
      where t.token = :token and (t.expired = false or t.revoked = false)\s
      """)
    Boolean isTokenValid(String token);

    Optional<Token> findByToken(String token);
}