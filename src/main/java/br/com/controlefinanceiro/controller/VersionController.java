package br.com.controlefinanceiro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/version")
public class VersionController {
    @GetMapping
    public String getVersion(){
        return "desenvolvimento";
    }
}
