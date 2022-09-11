package com.tweetapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.Comment;
import com.tweetapp.dto.TweetResponse;
import com.tweetapp.entities.Tweet;

import com.tweetapp.exception.InvalidUsernameException;
import com.tweetapp.exception.TweetDoesNotExistException;

import com.tweetapp.repositories.TweetRepository;

import io.micrometer.core.instrument.util.StringUtils;

@Service
public class TweetService {


	@Autowired
	private TweetRepository tweetRepository;

//	// Method to return all tweets
	public List<TweetResponse> getAllTweets() {
		
		// use username as login id
				
					List<Tweet> tweets = tweetRepository.findAll();
					System.out.println(tweets);
					 
					// List<TweetResponse> tweetResponse= new ArrayList<>();
					List<TweetResponse> tweetResponse = tweets.stream().map(tweet -> {
						List<Comment> comments = new ArrayList<>();
						Integer likesCount = tweet.getLikes();
						Boolean likeStatus = true;
						
						//Integer commentsCount = tweet.getComments().length();
						comments.add(new Comment(tweet.getUsername(),tweet.getComments()));
						System.out.println("Came here");
						return new TweetResponse(tweet.getTweetId(), tweet.getUsername(), tweet.getTweetText(), tweet.getFirstName(),
								tweet.getLastName(), tweet.getTweetDate(), likesCount, null, null,
								comments);
						
					}).collect(Collectors.toList());
					
					return tweetResponse;
					
	}
//	@KafkaListener(topics = "getAllTweets", groupId = "group-id")
//public List<TweetResponse> getAllTweets() {
//		
//		// use username as login id
//				
//					List<Tweet> tweets = tweetRepository.findAll();
//					System.out.println(tweets);
//					 
//					// List<TweetResponse> tweetResponse= new ArrayList<>();
//					List<TweetResponse> tweetResponse = tweets.stream().map(tweet -> {
//						List<Comment> comments = new ArrayList<>();
//						Integer likesCount = tweet.getLikes();
//						Boolean likeStatus = true;
//						
//						//Integer commentsCount = tweet.getComments().length();
//						comments.add(new Comment(tweet.getUsername(),tweet.getComments()));
//						System.out.println("Came here");
//						return new TweetResponse(tweet.getTweetId(), tweet.getUsername(), tweet.getTweetText(), tweet.getFirstName(),
//								tweet.getLastName(), tweet.getTweetDate(), likesCount, null, null,
//								comments);
//						
//					}).collect(Collectors.toList());
//					
//					return tweetResponse;
//					
//	}

	// Method to return all tweets of a user
	public List<TweetResponse> getUserTweets(String username, String loggedInUser) throws InvalidUsernameException {
		// use username as login id
		if (!StringUtils.isBlank(username)) {
			List<Tweet> tweets = tweetRepository.findByUsername(username);
			System.out.println(tweets);
			 
			// List<TweetResponse> tweetResponse= new ArrayList<>();
			List<TweetResponse> tweetResponse = tweets.stream().map(tweet -> {
				List<Comment> comments = new ArrayList<>();
				Integer likesCount = tweet.getLikes();
				Boolean likeStatus = true;
				//Integer commentsCount = tweet.getComments().length();
				comments.add(new Comment(username,tweet.getComments()));
				System.out.println("Came here");
				return new TweetResponse(tweet.getTweetId(), username, tweet.getTweetText(), tweet.getFirstName(),
						tweet.getLastName(), tweet.getTweetDate(), likesCount, null, null,
						comments);
				
			}).collect(Collectors.toList());
			
			return tweetResponse;
		} else {
			throw new InvalidUsernameException("Username/loginId provided is invalid");
		}

	}
//	@KafkaListener(topics = "getUserTweets", groupId = "group-id")
//	public List<TweetResponse> getUserTweets(String username, String loggedInUser) throws InvalidUsernameException {
//		// use username as login id
//		if (!StringUtils.isBlank(username)) {
//			List<Tweet> tweets = tweetRepository.findByUsername(username);
//			System.out.println(tweets);
//			 
//			// List<TweetResponse> tweetResponse= new ArrayList<>();
//			List<TweetResponse> tweetResponse = tweets.stream().map(tweet -> {
//				List<Comment> comments = new ArrayList<>();
//				Integer likesCount = tweet.getLikes();
//				Boolean likeStatus = true;
//				//Integer commentsCount = tweet.getComments().length();
//				comments.add(new Comment(username,tweet.getComments()));
//				System.out.println("Came here");
//				return new TweetResponse(tweet.getTweetId(), username, tweet.getTweetText(), tweet.getFirstName(),
//						tweet.getLastName(), tweet.getTweetDate(), likesCount, null, null,
//						comments);
//				
//			}).collect(Collectors.toList());
//			
//			return tweetResponse;
//		} else {
//			throw new InvalidUsernameException("Username/loginId provided is invalid");
//		}
//
//	}

	// Method to post a new tweet
	public Tweet postNewTweet(String username, Tweet newTweet) {

		newTweet.setTweetId(UUID.randomUUID().toString());
		return tweetRepository.save(newTweet);
	}

	// method to get tweet data by id
	public TweetResponse getTweet(String tweetId, String username) throws TweetDoesNotExistException {
		Optional<Tweet> tweetFounded = tweetRepository.findById(tweetId);
		
		 System.out.println(tweetFounded);
		List<Comment> comments = new ArrayList<>();
		if (tweetFounded.isPresent()) {
			Tweet tweet = tweetFounded.get();
			Integer likesCount = tweet.getLikes();
			//Boolean likeStatus = true;
			//Integer commentsCount = tweet.getComments().length();
			comments.add(new Comment(username,tweet.getComments()));
			
			return new TweetResponse(tweet.getTweetId(), tweet.getUsername(), tweet.getTweetText(),
					tweet.getFirstName(), tweet.getLastName(), tweet.getTweetDate(), likesCount,null, 
					null, comments);
		} else {
			throw new TweetDoesNotExistException("This tweet does not exist anymore.");
		}

	}

	// Method to update an existing tweet
	public Tweet updateTweet(String userId, String tweetId, String updatedTweetText) throws TweetDoesNotExistException {

		Optional<Tweet> originalTweetOptional = tweetRepository.findById(tweetId);
		System.out.println("===========>" + tweetId);
		System.out.println("===========>" + updatedTweetText);
		if (originalTweetOptional.isPresent()) {
			Tweet tweet = originalTweetOptional.get();
			tweet.setTweetText(updatedTweetText);

			return tweetRepository.save(tweet);
		} else {
			throw new TweetDoesNotExistException("This tweet does not exist anymore.");
		}

	}
//	@KafkaListener(topics = "updateTweet", groupId = "group-id")
//	public Tweet updateTweet(String tweetId, String updatedTweetText) throws TweetDoesNotExistException {
//
//		Optional<Tweet> originalTweetOptional = tweetRepository.findById(tweetId);
//		System.out.println("===========>" + tweetId);
//		System.out.println("===========>" + updatedTweetText);
//		if (originalTweetOptional.isPresent()) {
//			Tweet tweet = originalTweetOptional.get();
//			tweet.setTweetText(updatedTweetText);
//
//			return tweetRepository.save(tweet);
//		} else {
//			throw new TweetDoesNotExistException("This tweet does not exist anymore.");
//		}
//
//	}
	// Method to delete a tweet
	public boolean deleteTweet(String tweetId) throws TweetDoesNotExistException {
		if (tweetRepository.existsById(tweetId) && !StringUtils.isBlank(tweetId)) {
			tweetRepository.deleteById(tweetId);
			return true;
		} else {

			throw new TweetDoesNotExistException("This tweet does not exist anymore.");
		}
	}
	
//	@KafkaListener(topics = "deleteTweet", groupId = "group-id")
//	   public boolean deleteTweet(String id) {
//	      System.out.println("Received ID in group - group-id: " + id);
//	      tweetRepository.deleteById(id);
//		return true;
//	   }

	

	// Method to like a tweet
	public Tweet likeTweet(String username, String tweetId) throws TweetDoesNotExistException {
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweet tweet = tweetOptional.get();
			int c=tweet.getLikes();
			tweet.setLikes(c+1);
			return tweetRepository.save(tweet);
		} else {
			throw new TweetDoesNotExistException("This tweet does not exist anymore.");
		}
	}

	// Method to unlike a tweet
//	public Tweet dislikeTweet(String username, String tweetId) throws TweetDoesNotExistException {
//		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
//		if (tweetOptional.isPresent()) {
//			Tweet tweet = tweetOptional.get();
//			int c=tweet.getLikes();
//			tweet.setLikes(c-1);
//			return tweetRepository.save(tweet);
//		} else {
//			throw new TweetDoesNotExistException("This tweet does not exist anymore.");
//		}
//	}

	// Method to comment on a tweet
	public Tweet replyTweet(String username, String tweetId, String tweetReply) throws TweetDoesNotExistException {
		Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
		if (tweetOptional.isPresent()) {
			Tweet tweet = tweetOptional.get();
			System.out.println(tweet.getTweetId());
			String s=tweet.getComments();
			   
			if(s==null)
			{
				s=tweetReply;
			}
			else
			{
				s=s+","+" "+System.lineSeparator()+tweetReply;
			}
			tweet.setComments(s);
			//tweet.setComments(tweet.getUsername()+"  " +s);
			return tweetRepository.save(tweet);
			// return tweetReply;
		} else {
			throw new TweetDoesNotExistException("This tweet does not exist anymore.");
		}
	}


}
