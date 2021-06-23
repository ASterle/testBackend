package com.asterle.testbackend.database.repository;

import com.asterle.testbackend.database.model.DatabaseFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<DatabaseFile, Long> {

}
