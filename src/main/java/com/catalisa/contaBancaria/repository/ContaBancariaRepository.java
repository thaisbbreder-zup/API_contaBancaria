package com.catalisa.contaBancaria.repository;

import com.catalisa.contaBancaria.model.ContaBancariaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//repository só tem interface
@Repository //indica que nossa interface é um repositorio
//  extends JPA pq ele faz a comunicação com o banco e essa interface é a que faz a comunicacao do banco

public interface ContaBancariaRepository extends JpaRepository<ContaBancariaModel, Long> {


}
