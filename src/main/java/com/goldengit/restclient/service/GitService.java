package com.goldengit.restclient.service;

import com.goldengit.restclient.repository.GitRepository;
import com.goldengit.restclient.schema.Repositories;
import com.goldengit.web.dto.RepoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitService {

    private final GitRepository gitRepository;

    public List<RepoResponse> findRepoByQuery(String query) {
        Repositories repositories = gitRepository.findRepoByQuery(query);

        return repositories.getItems().stream().map(repository ->
                RepoResponse.builder()
                        .id(repository.id)
                        .name(repository.name)
                        .fullName(repository.full_name)
                        .description(repository.description)
                        .stars(repository.stargazers_count)
                        .forks(repository.forks_count)
                        .watchers(repository.watchers_count)
                        .openIssues(repository.open_issues_count)
                        .build()
        ).collect(Collectors.toList());

    }
}
