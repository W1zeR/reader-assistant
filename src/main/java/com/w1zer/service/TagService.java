package com.w1zer.service;

import com.w1zer.entity.Tag;
import com.w1zer.exception.NotFoundException;
import com.w1zer.payload.UpdateTagRequest;
import com.w1zer.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {
	public static final String TAG_WITH_ID_NOT_FOUND = "Tag with id %s not found";
	
	private final TagRepository tagRepository;
	
	public TagService(TagRepository tagRepository){
		this.tagRepository = tagRepository;
	}
	
	private Tag findById(Long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new NotFoundException(TAG_WITH_ID_NOT_FOUND.formatted(id))
        );
    }
	
	public void delete(Long id) {
        tagRepository.deleteById(id);
    }
	
	public Tag update(UpdateTagRequest updateTagRequest, Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new NotFoundException(TAG_WITH_ID_NOT_FOUND)
        );
        if (updateTagRequest.name() != null) {
            tag.setName(updateTagRequest.name());
        }
        return tagRepository.save(tag);
    }
}
