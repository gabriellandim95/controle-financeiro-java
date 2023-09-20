package br.com.controlefinanceiro.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/version")
public class VersionController {
    @Operation(summary = "Retorna a versão da API.", method = "GET")
    @GetMapping
    public String getVersion(){
        return "desenvolvimento";
    }
}
