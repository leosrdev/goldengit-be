package com.goldengit.web.repository;

import com.goldengit.web.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {

    public Optional<Project> findByFullName(String fullName);
}
