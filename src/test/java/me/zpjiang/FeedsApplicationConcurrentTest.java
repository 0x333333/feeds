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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: zhipeng
 * Date: 10/1/17 3:44 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FeedsApplicationConcurrentTest {
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
    public void testConcurrentUseCases() {
        // create 100 users, 100 feeds, 100 articles
        List<User> users = createTestUsers(100);
        List<Feed> feeds = createTestFeeds(100);
        List<Article> articles = createTestArticles(100);

        ExecutorService es = Executors.newCachedThreadPool();

        // create 20 threads
        // firstly, subscribe 10 random users to 10 random feeds
        // secondly, add 10 random articles to 10 random feeds
        for (int i = 0; i < 10; i++) {
            es.execute(new Subscriber(users, feeds, 10, i, mockMvc));
            es.execute(new Publisher(feeds, articles, 10, i, mockMvc));
        }

        es.shutdown();

        for (int i = 0; i < users.size(); i++) {
            getArticles(users.get(i));
        }

        cleanTestUsers(users);
        cleanTestArticle(articles);
        cleanTestFeeds(feeds);
    }

    private void getArticles(User user) {
        String listArticlePath = String.format("/articles/%s", user.getId());
        try {
            MvcResult result = mockMvc.perform(get(listArticlePath)).andReturn();
            if (result.getResponse().getStatus() == 200) {
                System.out.println(String.format("[Done] user %s", user.getId()));
            } else {
                System.out.println(String.format("[Failed] user %s", user.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<User> createTestUsers(int amount) {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < amount; i++) {
            users.add(new User(UUID.randomUUID().toString(), "Test User"));
        }
        userRepository.save(users);
        return users;
    }

    private List<Feed> createTestFeeds(int amount) {
        List<Feed> feeds = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            feeds.add(new Feed(UUID.randomUUID().toString(), "Test Feed"));
        }
        feedRepository.save(feeds);
        return feeds;
    }

    private List<Article> createTestArticles(int amount) {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            articles.add(new Article(UUID.randomUUID().toString(), "Test Article", ""));
        }
        articleRepository.save(articles);
        return articles;
    }

    private void cleanTestUsers(List<User> users) {
        userRepository.delete(users);
    }

    private void cleanTestFeeds(List<Feed> feeds) {
        feedRepository.delete(feeds);
    }

    private void cleanTestArticle(List<Article> articles) {
        articleRepository.delete(articles);
    }
}

