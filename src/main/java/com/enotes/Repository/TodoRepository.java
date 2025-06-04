package com.enotes.Repository;

import com.enotes.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Integer> {


    List<Todo> findByCreatedBy(Integer userId);
}
