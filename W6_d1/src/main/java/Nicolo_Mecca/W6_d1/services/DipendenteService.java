package Nicolo_Mecca.W6_d1.services;

import Nicolo_Mecca.W6_d1.entities.Dipendente;
import Nicolo_Mecca.W6_d1.excepetions.BadRequestException;
import Nicolo_Mecca.W6_d1.excepetions.NotFoundException;
import Nicolo_Mecca.W6_d1.payloads.NewDipendenteDTO;
import Nicolo_Mecca.W6_d1.repositories.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;


    public Dipendente findById(Long id) {
        return dipendenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dipendente con id " + id + " non trovato"));
    }

    public Dipendente save(NewDipendenteDTO body) {
        // Verifico che l'email non sia già in uso
        dipendenteRepository.findByEmail(body.email()).ifPresent(
                dipendente -> {
                    throw new BadRequestException("Email " + body.email() + " già in uso!");
                }
        );
        Dipendente newDipendente = new Dipendente();
        newDipendente.setUsername(body.username());
        newDipendente.setNome(body.nome());
        newDipendente.setCognome(body.cognome());
        newDipendente.setEmail(body.email());
        newDipendente.setImgProfilo("https://ui-avatars.com/api/?name=" +
                body.nome() + "+" + body.cognome());
        newDipendente.setPassword(body.password());
        return dipendenteRepository.save(newDipendente);
    }

    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return dipendenteRepository.findAll(pageable);
    }

    public Dipendente findByIdAndUpdate(Long dipendenteId, NewDipendenteDTO body) {
        Dipendente found = this.findById(dipendenteId);

        // Verifico che la nuova email non sia già in uso
        if (!found.getEmail().equals(body.email())) {
            dipendenteRepository.findByEmail(body.email()).ifPresent(
                    dipendente -> {
                        throw new BadRequestException("Email " + body.email() + " già in uso!");
                    }
            );
        }

        found.setUsername(body.username());
        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setEmail(body.email());
        found.setPassword(body.password());

        return dipendenteRepository.save(found);
    }

    public void findByIdAndDelete(Long dipendenteId) {
        Dipendente found = this.findById(dipendenteId);
        dipendenteRepository.delete(found);
    }

    public Dipendente findByEmail(String email) {
        return this.dipendenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato"));
    }


}