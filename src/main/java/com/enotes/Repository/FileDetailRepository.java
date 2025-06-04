package com.enotes.Repository;

import com.enotes.Entity.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailRepository extends JpaRepository<FileDetails,Integer> {


}
