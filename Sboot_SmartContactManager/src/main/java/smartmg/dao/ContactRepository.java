package smartmg.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import smartmg.entity.Contact;
import smartmg.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

//	@Query("from Contact as c where c.user.id = :userId")
//	public List<Contact> findContactsByUser(@Param("userId") int userId);	
	
/*=======================# Implementing Pagination as well==========================*/
	
	/*	Pageable has two information :
	 		1) Current Page - page
	 		2) Contact Per Page - 5
	*/ 
	
	@Query("from Contact as c where c.user.id = :userId ORDER BY cId DESC")
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

	// searching
	public List<Contact> findByNameContainingAndUser(String keyword, User user);
	
}
