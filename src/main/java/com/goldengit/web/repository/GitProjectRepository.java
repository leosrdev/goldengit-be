package com.goldengit.web.repository;

import com.goldengit.web.model.GitProject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GitProjectRepository extends CrudRepository<GitProject, String> {

    public Optional<GitProject> findByFullName(String fullName);
}
