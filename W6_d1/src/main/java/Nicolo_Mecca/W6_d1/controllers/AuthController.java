package Nicolo_Mecca.W6_d1.controllers;

import Nicolo_Mecca.W6_d1.entities.Dipendente;
import Nicolo_Mecca.W6_d1.excepetions.BadRequestException;
import Nicolo_Mecca.W6_d1.payloads.DipendentiLoginDTO;
import Nicolo_Mecca.W6_d1.payloads.DipendentiLoginResponseDTO;
import Nicolo_Mecca.W6_d1.payloads.NewDipendenteDTO;
import Nicolo_Mecca.W6_d1.services.AuthService;
import Nicolo_Mecca.W6_d1.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public DipendentiLoginResponseDTO login(@RequestBody DipendentiLoginDTO body) {
        return new DipendentiLoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente save(@RequestBody @Validated NewDipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.dipendenteService.save(body);
    }
}
