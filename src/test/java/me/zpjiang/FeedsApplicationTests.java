package me.zpjiang;

import me.zpjiang.Models.Article;
import me.zpjiang.Models.Feed;
import me.zpjiang.Models.User;
import me.zpjiang.Repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FeedsApplicationTests {
	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private FeedRepository feedRepository;

	@Autowired
	private PublicationRepository publicationRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testSubscribeFeed() throws Exception {
	    User user = createTestUser();
	    Feed feed = createTestFeed();

		// first subscribe, should be OK
		String path = String.format("/sub/user/%s/feed/%s", user.getId(), feed.getId());
		mockMvc.perform(post(path))
                .andDo(print())
                .andExpect(status().isOk());

		// duplicate subscribe, should be bad request
		mockMvc.perform(post(path))
                .andDo(print())
                .andExpect(status().isBadRequest());

		cleanTestUser(user);
		cleanTestFeed(feed);
	}

	@Test
	public void testUnSubscribeFeed() throws Exception {
	    User user = createTestUser();
	    Feed feed = createTestFeed();

		// first subscribe, should be OK
		String subPath = String.format("/sub/user/%s/feed/%s", user.getId(), feed.getId());
		mockMvc.perform(post(subPath))
                .andDo(print())
                .andExpect(status().isOk());

		// unsubscribe, should be ok
	    String unSubPath = String.format("/sub/user/%s/feed/%s", user.getId(), feed.getId());
	    mockMvc.perform(delete(unSubPath))
                .andDo(print())
                .andExpect(status().isOk());

	    // unsubscribe again, should be bad request
	    mockMvc.perform(delete(unSubPath))
                .andDo(print())
                .andExpect(status().isBadRequest());

	    // unsubscribe an invalid record, shoule be bad request
	    String invalidUnSubPath = String.format("/sub/user/%s/feed/%s", "someUserID", "someFeedID");
	    mockMvc.perform(delete(invalidUnSubPath))
                .andDo(print())
                .andExpect(status().isBadRequest());

		cleanTestUser(user);
		cleanTestFeed(feed);
	}

	@Test
	public void testGetFeed() throws Exception {
	    User user = createTestUser();
	    Feed feed = createTestFeed();

		// first subscribe, should be OK
		String subPath = String.format("/sub/user/%s/feed/%s", user.getId(), feed.getId());
		mockMvc.perform(post(subPath))
                .andDo(print())
                .andExpect(status().isOk());

		// list the subscription, should be OK
		String getFeedsPath = String.format("/feeds/%s", user.getId());
		mockMvc.perform(get(getFeedsPath))
                .andDo(print())
                .andExpect(status().isOk());

		cleanTestUser(user);
		cleanTestFeed(feed);
	}

	private User createTestUser() {
	    String userID = UUID.randomUUID().toString();
	    User user = new User(userID, "Test User");
	    userRepository.save(user);
	    return user;
    }

    private Feed createTestFeed() {
	    String feedID = UUID.randomUUID().toString();
	    Feed feed = new Feed(feedID, "Test Feed");
	    feedRepository.save(feed);
	    return feed;
    }

    private Article createTestArticle() {
	    String articleID = UUID.randomUUID().toString();
	    Article article = new Article(articleID, "Test Article", "");
	    articleRepository.save(article);
	    return article;
    }

    private void cleanTestUser(User user) {
	    userRepository.delete(user);
    }

    private void cleanTestFeed(Feed feed) {
	    feedRepository.delete(feed);
    }

    private void cleanTestArticle(Article article) {
	    articleRepository.delete(article);
    }
}
