package com.projectmanagement.ProjectManagement.controller;

import com.projectmanagement.ProjectManagement.DTO.PersonneDTO;
import com.projectmanagement.ProjectManagement.entity.Personne;
import com.projectmanagement.ProjectManagement.service.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/personnes")
public class PersonneController {
    private static final String UPLOAD_DIR = "C:/Users/amine/Downloads/ProjectManagement/ProjectManagement/src/main/resources/uploads/";
    private static final Logger LOGGER = Logger.getLogger(PersonneController.class.getName());

    @Autowired
    private PersonneService personneService;

    @GetMapping
    public List<Personne> getAllPersonnes() {
        LOGGER.info("Fetching all personnes");
        return personneService.getAllPersonnes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personne> getPersonneById(@PathVariable Long id) {
        LOGGER.info("Fetching personne by id: " + id);
        Personne personne = personneService.getPersonneById(id);
        if (personne != null) {
            return ResponseEntity.ok(personne);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Personne createPersonne(@ModelAttribute PersonneDTO personneDTO) throws IOException {
        LOGGER.info("Creating Personne: " + personneDTO.getName());
        Personne personne = new Personne();
        personne.setName(personneDTO.getName());
        personne.setRole(personneDTO.getRole()); // Set role
        personne.setPoleName(personneDTO.getPoleName());
        // Save the image file
        if (personneDTO.getImage() != null && !personneDTO.getImage().isEmpty()) {
            LOGGER.info("Received image: " + personneDTO.getImage().getOriginalFilename());
            String fileName = saveImage(personneDTO.getImage());
            personne.setImage(fileName);
            LOGGER.info("Image saved as: " + fileName);
        } else {
            LOGGER.warning("No image received.");
        }

        Personne savedPersonne = personneService.savePersonne(personne);
        LOGGER.info("Personne saved with id: " + savedPersonne.getId());
        return savedPersonne;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Personne> updatePersonne(@PathVariable Long id, @ModelAttribute PersonneDTO personneDTO) throws IOException {
        LOGGER.info("Updating Personne: " + id);
        Personne personne = personneService.getPersonneById(id);
        if (personne != null) {
            personne.setName(personneDTO.getName());
            personne.setRole(personneDTO.getRole()); // Set role

            // Save the image file
            if (personneDTO.getImage() != null && !personneDTO.getImage().isEmpty()) {
                LOGGER.info("Received image: " + personneDTO.getImage().getOriginalFilename());
                String fileName = saveImage(personneDTO.getImage());
                personne.setImage(fileName);
                LOGGER.info("Image saved as: " + fileName);
            } else {
                LOGGER.warning("No image received.");
            }

            final Personne updatedPersonne = personneService.savePersonne(personne);
            LOGGER.info("Personne updated with id: " + updatedPersonne.getId());
            return ResponseEntity.ok(updatedPersonne);
        } else {
            LOGGER.warning("Personne not found with id: " + id);
            return ResponseEntity.notFound().build();
        }
    }


    private String saveImage(MultipartFile image) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + image.getOriginalFilename());
        Files.write(path, image.getBytes());
        return image.getOriginalFilename();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonne(@PathVariable Long id) {
        LOGGER.info("Deleting Personne: " + id);
        Personne personne = personneService.getPersonneById(id);
        if (personne != null) {
            personneService.deletePersonne(id);
            LOGGER.info("Personne deleted with id: " + id);
            return ResponseEntity.noContent().build();
        } else {
            LOGGER.warning("Personne not found with id: " + id);
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/delete-multiple")
    public ResponseEntity<Void> deleteMultiplePersonnes(@RequestBody List<Long> ids) {
        LOGGER.info("Deleting multiple personnes: " + ids);
        personneService.deleteMultiplePersonnes(ids);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
