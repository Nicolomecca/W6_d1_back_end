package Nicolo_Mecca.W6_d1.services;

import Nicolo_Mecca.W6_d1.entities.Dipendente;
import Nicolo_Mecca.W6_d1.excepetions.UnauthorizedException;
import Nicolo_Mecca.W6_d1.payloads.DipendentiLoginDTO;
import Nicolo_Mecca.W6_d1.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private JWT jwt;

    public String checkCredentialsAndGenerateToken(DipendentiLoginDTO body) {
        Dipendente found = this.dipendenteService.findByEmail(body.email());
        if (found.getPassword().equals(body.password())) {
            String accessToken = jwt.createToken(found);
            return accessToken;
        } else {
            throw new UnauthorizedException("Credenziali errate");
        }
    }

}
