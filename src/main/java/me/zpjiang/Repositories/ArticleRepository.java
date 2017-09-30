package me.zpjiang.Repositories;

import me.zpjiang.Models.Article;
import org.springframework.data.repository.CrudRepository;

/**
 * Author: zhipeng
 * Date: 9/28/17 9:34 PM
 */
public interface ArticleRepository extends CrudRepository<Article, String> {
}
