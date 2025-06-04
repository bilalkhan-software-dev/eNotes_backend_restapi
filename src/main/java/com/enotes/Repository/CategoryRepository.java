package com.enotes.Repository;

import com.enotes.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {


    List<Category> findByIsDeletedFalse();

    List<Category> findByIsActiveTrueAndIsDeletedFalse();

    Optional<Category> findByIdAndIsDeletedFalse(Integer id);

    boolean existsByName(String name);
}
