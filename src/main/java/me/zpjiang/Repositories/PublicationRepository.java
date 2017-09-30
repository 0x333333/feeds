package me.zpjiang.Repositories;

import me.zpjiang.Models.Publication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Author: zhipeng
 * Date: 9/28/17 9:34 PM
 */
public interface PublicationRepository extends CrudRepository<Publication, String> {
    List<Publication> findAllByArticleIDAndFeedID(String articleID, String feedID);
}
