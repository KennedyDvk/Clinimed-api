package br.com.clinimed.controller;

import br.com.clinimed.domain.usuario.DadosAutenticacao;
import br.com.clinimed.domain.usuario.Usuario;
import br.com.clinimed.infra.security.DadosTokenJWT;
import br.com.clinimed.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(),dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = ResponseEntity.ok(tokenService.gerarToken((Usuario) authentication.getPrincipal()));

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));

    }

}
