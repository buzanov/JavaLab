package ru.itis.servlets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.servlets.models.FileInfo;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {
    Optional<FileInfo> findByStorageFileName(String storageName);

}
