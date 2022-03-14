package smartmg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import smartmg.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
