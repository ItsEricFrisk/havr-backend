package com.itsericfrisk.havr.repository;

import com.itsericfrisk.havr.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find all categories, global or owned by the user
     *
     * @param userId Long
     * @return List of categories
     */
    List<Category> findByGlobalTrueOrUserId(Long userId);

    /**
     * Find all categories by category parent id
     *
     * @param parentId Long
     * @return List of categories
     */
    List<Category> findByParentId(Long parentId);
}
