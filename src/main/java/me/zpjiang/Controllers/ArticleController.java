package me.zpjiang.Controllers;

import me.zpjiang.Models.Article;
import me.zpjiang.Models.Feed;
import me.zpjiang.Models.Publication;
import me.zpjiang.Models.Subscription;
import me.zpjiang.Repositories.ArticleRepository;
import me.zpjiang.Repositories.PublicationRepository;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhipeng
 * Date: 9/28/17 8:47 PM
 */
@Controller
public class ArticleController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private Logger logger;

    public ArticleController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping(path = "/articles/{userID}", method = RequestMethod.GET)
    public ResponseEntity<?> getArticles(@PathVariable String userID) {
        // validate user ID
        if (userID == null || !userRepository.exists(userID)) {
            String msg = String.format("User %s does not exists.", userID);
            logger.error(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        List<Subscription> subs = subscriptionRepository.findByUserID(userID);
        if (subs == null || subs.size() == 0) {
            logger.warn("User has no subscription.");
            return new ResponseEntity<>(new ArrayList<Feed>(), HttpStatus.OK);
        }

        List<Article> articles = new ArrayList<>();
        for (Subscription sub : subs) {
            List<Publication> pubs = publicationRepository.findAllByFeedID(sub.getFeedID());
            if (pubs != null && pubs.size() > 0) {
                for (Publication pub : pubs) {
                    String articleID = pub.getArticleID();
                    if (articleRepository.exists(articleID)) {
                        articles.add(articleRepository.findOne(articleID));
                    } else {
                        String msg = String.format(
                                "ArticleID %s found in publication, but not in article table.",
                                articleID);
                        logger.warn(msg);
                    }
                }
            }
        }

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
}
