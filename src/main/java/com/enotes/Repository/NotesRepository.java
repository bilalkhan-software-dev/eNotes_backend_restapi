package com.enotes.Repository;

import com.enotes.Entity.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository // optional
public interface NotesRepository extends JpaRepository<Notes,Integer> {


    Optional<Notes> findByIdAndIsDeletedFalse(Integer id);

    Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pageable);

    Optional<Notes> findByCreatedByAndIsDeletedFalse(Integer id);

    List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);

    List<Notes> findByIsDeletedAndDeletedOnBefore(Boolean isDeleted, LocalDateTime deletedOn);



    @Query("SELECT n FROM Notes n " +
            "WHERE (LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR n.description LIKE CONCAT('%', :keyword, '%') " + // no LOWER() here
            "   OR LOWER(n.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND n.isDeleted = false " +
            "AND n.createdBy = :userId")
    Page<Notes> searchNotes(@Param("keyword") String keyword,
                            @Param("userId") Integer userId,
                            Pageable pageable);


    Optional<Notes> findByIdAndCreatedByAndIsDeletedFalse(Integer id, Integer createdBy);
}
