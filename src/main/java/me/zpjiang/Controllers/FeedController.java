package me.zpjiang.Controllers;

import me.zpjiang.Models.Feed;
import me.zpjiang.Models.Publication;
import me.zpjiang.Models.Subscription;
import me.zpjiang.Repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhipeng
 * Date: 9/28/17 8:46 PM
 */
@Controller
public class FeedController {

    @Autowired
    private SubscriptionRepository subRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private PublicationRepository publicationRepository;

    private Logger logger;

    public FeedController() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @GetMapping(path = "/feeds/{userID}")
    public ResponseEntity<?> getAllFeedsByUserID(@PathVariable String userID) {
        if (userID == null || !userRepository.exists(userID)) {
            String msg = String.format("Invalid user ID: %s", userID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        List<Subscription> subs = subRepository.findByUserID(userID);
        if (subs == null || subs.size() == 0)  {
            logger.warn("User has no subscription.");
            return new ResponseEntity<>(new ArrayList<Feed>(), HttpStatus.OK);
        }

        List<String> feedIDs = new ArrayList<String>();
        for (Subscription sub : subs) {
            feedIDs.add(sub.getFeedID());
        }
        Iterable<Feed> feeds = feedRepository.findAll(feedIDs);
        logger.info(String.format("User %s gets feeds successfully.", userID));
        return new ResponseEntity<>(feeds, HttpStatus.OK);
    }

    @RequestMapping(path = "/feeds/feed/{feedID}/article/{articleID}", method = RequestMethod.POST)
    public ResponseEntity<?> publishArticle(
            @PathVariable String feedID,
            @PathVariable String articleID) {
        if (feedID == null || !feedRepository.exists(feedID)) {
            String msg = String.format("Invalid feed ID: %s", feedID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        if (articleID == null || !articleRepository.exists(articleID)) {
            String msg = String.format("Invalid article ID: %s", articleID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        List<Publication> pubs = publicationRepository.findAllByArticleIDAndFeedID(articleID, feedID);
        if (pubs != null && pubs.size() > 0) {
            String msg = String.format("Article %s already published in feed %s", articleID, feedID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        String pubID = String.format("%s_%s", feedID, articleID);
        if (publicationRepository.exists(pubID)) {
            String msg = String.format("Article %s has already beed added to feed %s.", articleID, feedID);
            logger.error(msg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        Publication pub = new Publication(pubID, feedID, articleID);
        publicationRepository.save(pub);
        logger.info(String.format("Add article %s to feed %s successfully.", articleID, feedID));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
