package me.zpjiang.Repositories;

import me.zpjiang.Models.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Author: zhipeng
 * Date: 9/28/17 9:54 PM
 */
public interface SubscriptionRepository extends CrudRepository<Subscription, String> {
    List<Subscription> findByUserID(String userID);
}

