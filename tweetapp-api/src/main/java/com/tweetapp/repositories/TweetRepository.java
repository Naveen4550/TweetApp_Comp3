package com.tweetapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, String>{
	
	List<Tweet> findByUsername(String username);
}
