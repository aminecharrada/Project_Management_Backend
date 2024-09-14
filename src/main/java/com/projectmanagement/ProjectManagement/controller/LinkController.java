// LinkController.java
package com.projectmanagement.ProjectManagement.controller;
import com.projectmanagement.ProjectManagement.entity.Link;
import com.projectmanagement.ProjectManagement.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping
    public ResponseEntity<List<Link>> getAllLinks() {
        List<Link> links = linkService.getAllLinks();
        return ResponseEntity.ok(links);
    }

    @PostMapping
    public ResponseEntity<Link> createLink(@RequestBody Link link) {
        Link createdLink = linkService.saveLink(link);
        return ResponseEntity.ok(createdLink);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Link> updateLink(@PathVariable Long id, @RequestBody Link link) {
        Link updatedLink = linkService.updateLink(id, link);
        return ResponseEntity.ok(updatedLink);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        try {
            linkService.deleteLink(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
