package tech.getarrays.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.getarrays.bibliotheque.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{


User findFirstByEmail(String email);
}
