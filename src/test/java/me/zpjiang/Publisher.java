package me.zpjiang;

import me.zpjiang.Models.Article;
import me.zpjiang.Models.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Author: zhipeng
 * Date: 10/1/17 4:20 PM
 */
public class Publisher implements Runnable {
    private List<Feed> feeds;
    private List<Article> articles;
    private Random random;
    private int count;
    private int threadIndex;

    private MockMvc mockMvc;

    public Publisher(List<Feed> feeds, List<Article> articles, int count, int i, MockMvc mockMvc) {
        this.feeds = feeds;
        this.articles = articles;
        this.count = count;
        this.threadIndex = i;
        this.mockMvc = mockMvc;
        random = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            Feed feed = feeds.get(random.nextInt(feeds.size()));
            Article article = articles.get(random.nextInt(articles.size()));
            try {
                publish(article, feed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(String.format("[Publisher] Thread %d finished.", threadIndex));
    }

    private void publish(Article article, Feed feed) throws Exception {
        String pubPath = String.format("/feeds/feed/%s/article/%s", feed.getId(), article.getId());
        MvcResult result = mockMvc.perform(post(pubPath)).andReturn();
        if (result.getResponse().getStatus() == 200) {
//            System.out.println(String.format("[Done] Publish article %s to feed %s.", article.getId(), feed.getId()));
        } else {
//            System.out.println(String.format("[Failed] Failed to publish article %s to feed %s", article.getId(), feed.getId()));
        }
    }

}
