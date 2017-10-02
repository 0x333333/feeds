package me.zpjiang.Controllers;

import me.zpjiang.Models.Subscription;
import me.zpjiang.Repositories.FeedRepository;
import me.zpjiang.Repositories.SubscriptionRepository;
import me.zpjiang.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Author: zhipeng
 * Date: 9/28/17 9:59 PM
 */
@Controller
public class SubscriptionController {
    @Autowired
    private SubscriptionRepository subRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedRepository feedRepository;

    private Logger logger;

    public SubscriptionController() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping(path = "/sub/user/{userID}/feed/{feedID}", method = RequestMethod.POST)
    public ResponseEntity<?> subscribe(@PathVariable String userID, @PathVariable String feedID) {

        // validate user ID and feed ID
        if (userID == null || !userRepository.exists(userID)) {
            String msg = String.format("User %s does not exists.", userID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        if (feedID == null || !feedRepository.exists(feedID)) {
            String msg = String.format("Feed %s does not exists.", feedID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        // check subscription duplication
        String subID = String.format("%s_%s", userID, feedID);
        if (subRepository.exists(subID)) {
            String msg = String.format("User %s has already subscribed feed %s.", userID, feedID);
            logger.error(msg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        Subscription sub = new Subscription(subID, userID, feedID);
        try {
            subRepository.save(sub);
            logger.info(String.format("User %s subscribe feed %s successfully.", userID, feedID));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/sub/user/{userID}/feed/{feedID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> unsubscribe(@PathVariable String userID, @PathVariable String feedID) {

        // validate user ID and feed ID
        if (userID == null || !userRepository.exists(userID)) {
            String msg = String.format("User %s does not exists.", userID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        if (feedID == null || !feedRepository.exists(feedID)) {
            String msg = String.format("Feed %s does not exists.", feedID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        // check subscription existance
        String subID = String.format("%s_%s", userID, feedID);
        if (!subRepository.exists(subID)) {
            String msg = String.format("User %s has not subscribed feed %s.", userID, feedID);
            logger.error(msg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        try {
            subRepository.delete(subID);
            logger.info(String.format("User %s unsubscribe feed %s successfully.", userID, feedID));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
