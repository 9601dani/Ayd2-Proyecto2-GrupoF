package com.codenbugs.ms_company.services.module;

import com.codenbugs.ms_company.client.UserRestClient;
import com.codenbugs.ms_company.dtos.module.ModuleResponseDto;
import com.codenbugs.ms_company.dtos.user.UserResponse;
import com.codenbugs.ms_company.exceptions.UserNotFoundException;
import com.codenbugs.ms_company.repositories.module.PageRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
@Transactional(rollbackOn = Exception.class)
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    private final UserRestClient userRestClient;

    @Override
    public List<ModuleResponseDto> getPagesByRole(Integer id) throws UserNotFoundException {

        UserResponse user = userRestClient.findById(id);
        return pageRepository.findPagesByRoleId(user.role());
    }
}
