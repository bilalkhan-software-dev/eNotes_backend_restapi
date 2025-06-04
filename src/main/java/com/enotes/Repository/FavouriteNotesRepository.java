package com.enotes.Repository;

import com.enotes.Entity.FavouriteNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes,Integer> {


    List<FavouriteNotes> findByUserId(Integer userId);
}
