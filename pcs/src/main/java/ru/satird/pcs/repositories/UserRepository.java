package ru.satird.pcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.satird.pcs.domains.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String username);
    Boolean existsByName(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
