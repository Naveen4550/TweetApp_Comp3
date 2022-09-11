package com.tweetapp.repositories;

import java.util.List;

//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tweetapp.entities.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String>{
	
	UserModel findByUsername(String username);
	
//	@Query("{'username':{'$regex':'?0','$options':'i'}}")
	List<UserModel> searchByUsername(String username);
}
