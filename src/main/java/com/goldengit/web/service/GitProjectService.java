package com.goldengit.web.service;

import com.goldengit.web.model.GitProject;
import com.goldengit.web.repository.GitProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GitProjectService {

    private final GitProjectRepository repository;

    @Cacheable("git-repositories")
    public GitProject findOrCreate(String fullName) {
        Optional<GitProject> optionalGitProject = repository.findByFullName(fullName);
        return optionalGitProject.orElseGet(() -> repository.save(
                GitProject.builder()
                        .uuid(UUID.randomUUID().toString())
                        .fullName(fullName)
                        .build()
        ));
    }

    @Cacheable("git-repositories")
    public GitProject findById(String uuid) {
        return repository.findById(uuid).orElseThrow();
    }
}
