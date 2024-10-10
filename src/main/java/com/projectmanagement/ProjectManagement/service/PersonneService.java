    package com.projectmanagement.ProjectManagement.service;

    import com.projectmanagement.ProjectManagement.entity.Personne;
    import com.projectmanagement.ProjectManagement.repository.PersonneRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.logging.Logger;

    @Service
    public class PersonneService {
        private static final Logger LOGGER = Logger.getLogger(PersonneService.class.getName());

        @Autowired
        private PersonneRepository personneRepository;

        public List<Personne> getAllPersonnes() {
            return personneRepository.findAll();
        }

        public Personne getPersonneById(Long id) {
            return personneRepository.findById(id).orElse(null);
        }

        public Personne savePersonne(Personne personne) {

            return personneRepository.save(personne);
        }

        public void deletePersonne(Long id) {

            personneRepository.deleteById(id);
        }
        public void deleteMultiplePersonnes(List<Long> ids) {
            personneRepository.deleteAllById(ids);
        }
    }
