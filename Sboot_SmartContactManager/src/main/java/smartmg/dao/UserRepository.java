package smartmg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import smartmg.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);
	
//	@Query("select u from User u where u.email = :email")				// We can also find User by email through HQL query on custom-method  
//	public User getUserByUserEmail(@Param("email") String email);
	
	
}
