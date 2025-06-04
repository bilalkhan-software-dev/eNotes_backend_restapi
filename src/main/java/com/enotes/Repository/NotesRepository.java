package com.enotes.Repository;

import com.enotes.Entity.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

}
