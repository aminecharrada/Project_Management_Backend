package com.projectmanagement.ProjectManagement.controller;

import com.projectmanagement.ProjectManagement.DTO.PoleDto;
import com.projectmanagement.ProjectManagement.entity.Pole;
import com.projectmanagement.ProjectManagement.entity.Personne;
import com.projectmanagement.ProjectManagement.repository.PoleRepository;
import com.projectmanagement.ProjectManagement.service.PersonneService;
import com.projectmanagement.ProjectManagement.service.PoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poles")
public class PoleController {

    @Autowired
    private PersonneService personneService;

    @Autowired
    private PoleService poleService;

    @Autowired
    private PoleRepository poleRepository;

    @GetMapping
    public ResponseEntity<List<Pole>> getAllPoles() {
        List<Pole> poles = poleService.getAllPoles();
        return ResponseEntity.ok(poles);
    }

    @PostMapping
    public ResponseEntity<?> createPole(@RequestBody PoleDto poleDto) {
        Personne person = personneService.getPersonneById(poleDto.getPersonId());
        if (person == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person not found");
        }

        Pole newPole = new Pole();
        newPole.setPoleName(poleDto.getPoleName());
        newPole.setElemPole(person.getName());
        newPole.setElemPoleImage(person.getImage());

        poleRepository.save(newPole);
        return ResponseEntity.ok(newPole);
    }

    @PostMapping("/upload")
    public ResponseEntity<Pole> uploadPoleImage(@RequestParam("poleName") String poleName,
                                                @RequestParam("elemPole") String elemPole,
                                                @RequestParam("elemPoleImage") String elemPoleImage) {
        Pole pole = new Pole();
        pole.setPoleName(poleName);
        pole.setElemPole(elemPole);
        pole.setElemPoleImage(elemPoleImage);

        Pole savedPole = poleService.savePole(pole);
        return ResponseEntity.ok(savedPole);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pole> getPoleById(@PathVariable Long id) {
        Pole pole = poleService.findById(id);
        return pole != null ? ResponseEntity.ok(pole) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePole(@PathVariable Long id, @RequestBody Pole pole) {
        pole.setId(id);
        poleService.updatePole(pole);
        return ResponseEntity.ok("Pole updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePole(@PathVariable Long id) {
        poleService.deleteById(id);
        return ResponseEntity.ok("Pole deleted successfully");
    }
}
