package br.com.clinimed.infra.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptExample {
    public static void main(String[] args) {
        // Crie um codificador BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Senha que você deseja codificar
        String senha = "kennedy";

        // Codifique a senha
        String senhaCodificada = passwordEncoder.encode(senha);

        // Exiba a senha codificada
        System.out.println("Senha original: " + senha);
        System.out.println("Senha codificada: " + senhaCodificada);

        // Verifique se a senha sem codificar corresponde à senha codificada
        boolean senhaCorrespondente = passwordEncoder.matches(senha, senhaCodificada);
        System.out.println("Senhas correspondem: " + senhaCorrespondente);
    }
}
