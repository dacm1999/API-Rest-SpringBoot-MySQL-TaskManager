package com.dacm.taskManager.tags;

import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.repository.TagRepository;
import com.dacm.taskManager.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


}
