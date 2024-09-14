package com.projectmanagement.ProjectManagement.service;

// LinkService.java
import com.projectmanagement.ProjectManagement.entity.Link;
import com.projectmanagement.ProjectManagement.repository.LinkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    public Link saveLink(Link link) {
        if (link.getVersion() == null) {
            link.setVersion(0L);
        }
        return linkRepository.save(link);
    }

    public Link updateLink(Long id, Link link) {
        link.setId(id);
        return linkRepository.save(link);
    }

    @Transactional
    public void deleteLink(Long id) {
        if (linkRepository.existsById(id)) {
            linkRepository.deleteById(id);
        } else {
            throw new RuntimeException("Link not found with id " + id);
        }
    }

}
