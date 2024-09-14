package com.projectmanagement.ProjectManagement.service;

import com.projectmanagement.ProjectManagement.entity.Pole;
import com.projectmanagement.ProjectManagement.repository.PoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PoleService {

    @Autowired
    private PoleRepository poleRepository;

    public List<Pole> getAllPoles() {
        return poleRepository.findAll();
    }

    public Pole savePole(Pole pole) {
        return poleRepository.save(pole);
    }


    public Pole findById(Long id) {
        return poleRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        poleRepository.deleteById(id);
    }

    public void updatePole(Pole pole) {
        // Find the existing Pole entity
        Pole existingPole = poleRepository.findById(pole.getId()).orElseThrow(() -> new EntityNotFoundException("Pole not found"));

        // Update fields of the existing Pole
        existingPole.setPoleName(pole.getPoleName());
        existingPole.setElemPole(pole.getElemPole());
        existingPole.setElemPoleImage(pole.getElemPoleImage());

        // Save the updated Pole entity
        poleRepository.save(existingPole);
    }
}
