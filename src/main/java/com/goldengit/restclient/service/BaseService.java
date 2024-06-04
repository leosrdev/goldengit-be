package com.goldengit.restclient.service;

import com.goldengit.web.model.GitProject;
import com.goldengit.web.service.GitProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public abstract class BaseService {

    @Autowired
    private GitProjectService gitProjectService;

    @Cacheable(value = "git-repositories", key = "'projects:' + #uuid")
    protected GitProject getProjectByUUID(String uuid) throws BadRequestException {
        return gitProjectService.findById(uuid)
                .orElseThrow(() -> {
                    log.error("No project found with uuid: " + uuid);
                    return new BadRequestException("No project found with uuid: " + uuid);
                });
    }
}
