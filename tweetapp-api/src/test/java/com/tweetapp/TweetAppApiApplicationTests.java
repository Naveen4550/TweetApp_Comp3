package com.tweetapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tweetapp.dto.Reply;



@SpringBootTest(properties = "spring.main.lazy-initialization=true",
classes = {Reply.class})
class TweetAppApiApplicationTests {

	@Mock
	private Reply reply;
	
	
	
	@Test
	public void getComment()
	{
		reply.setComment("test");
		
		assertNull(reply.getComment());
	}


}
