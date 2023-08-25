package com.catalisa.contaBancaria.controller;



import com.catalisa.contaBancaria.exception.ContaNotFoundException;
import com.catalisa.contaBancaria.model.ContaBancariaModel;
import com.catalisa.contaBancaria.repository.ContaBancariaRepository;
import com.catalisa.contaBancaria.service.ContaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

//é a primeira camada, se comunica com o usuario, recebe as requisições HTTP e direciona para as operações adequadas
@RestController
public class ContaBancariaController {

    @Autowired
    ContaBancariaService contaBancariaService;

    @Autowired
    ContaBancariaRepository contaBancariaRepository;

    //requisicoes get
    @GetMapping(path = "/contas") //mapeia o metodo como get e como acessar esse metodo

    public List<ContaBancariaModel> buscarTodasContas() {
        return contaBancariaService.listarContas();
    }

    @GetMapping(path = "/contas/{id}")
    public Optional<ContaBancariaModel> buscarContaPorID(@PathVariable Long id) {
        return contaBancariaService.buscarContaPorId(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/contas")
    public ContaBancariaModel cadastrarNovoCliente(@RequestBody ContaBancariaModel contaBancariaModel) {
        return contaBancariaService.cadastrar(contaBancariaModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaBancariaModel> atualizarConta(@PathVariable Long id, @RequestBody ContaBancariaModel contaAtualizada) {
        ContaBancariaModel atualizarConta = contaBancariaService.atualizarConta(id, contaAtualizada);
        return ResponseEntity.ok(atualizarConta);
    }

    @DeleteMapping(path = "/contas/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id) {
        try {
            contaBancariaService.deletarConta(id);
            return ResponseEntity.noContent().build();
        } catch (ContaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(ContaNotFoundException.class)
    public ResponseEntity<String> handleContaNotFoundException(ContaNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
