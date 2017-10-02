package me.zpjiang;

import me.zpjiang.Models.Feed;
import me.zpjiang.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Author: zhipeng
 * Date: 10/1/17 4:19 PM
 */
class Subscriber implements Runnable {
    private List<User> users;
    private List<Feed> feeds;
    private Random random;
    private int count;
    private int threadIndex;

    private MockMvc mockMvc;

    public Subscriber(List<User> users, List<Feed> feeds, int count, int i, MockMvc mockMvc) {
        this.users = users;
        this.feeds = feeds;
        this.count = count;
        this.threadIndex = i;
        this.mockMvc = mockMvc;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            User user = users.get(random.nextInt(users.size()));
            Feed feed = feeds.get(random.nextInt(feeds.size()));
            try {
                subscribe(user, feed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(String.format("[Subscriber] Thread %d finished.", threadIndex));
    }

    private void subscribe(User user, Feed feed) throws Exception {
        String path = String.format("/sub/user/%s/feed/%s", user.getId(), feed.getId());
        MvcResult result = mockMvc.perform(post(path)).andReturn();
        if (result.getResponse().getStatus() == 200) {
//            System.out.println(String.format("[Done] User %s subscribe to feed %s.", user.getId(), feed.getId()));
        } else {
//            System.out.println(String.format("[Failed] User %s failed to subscribe to feed %s.", user.getId(), feed.getId()));
        }
    }
}

