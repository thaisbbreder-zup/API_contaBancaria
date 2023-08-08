package com.catalisa.contaBancaria.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import javax.persistence.*;

@Entity //indica que essa classe é uma entidade do banco
@Table(name = "TB_CONTAS") //indica que é uma tabela do banco
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//representará as informações de uma conta bancária
public class ContaBancariaModel {
    @Id //id = primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //gerador de id
    private Long id;
    @Column(length = 8, nullable = false)
    private String numeroConta;
    @Column(length = 4, nullable = false)
    private String agencia;
    @Column(length = 50, nullable = false)
    private String nomeUsuario;
    private double valorAtual;
    private double ultimoValorFornecido;
    @Column(nullable = false)
    private String tipoServico;

}
