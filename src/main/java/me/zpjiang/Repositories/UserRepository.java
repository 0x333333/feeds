package me.zpjiang.Repositories;

import me.zpjiang.Models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Author: zhipeng
 * Date: 9/28/17 9:34 PM
 */
public interface UserRepository extends CrudRepository<User, String> {
}
