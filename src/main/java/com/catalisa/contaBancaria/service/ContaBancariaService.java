package com.catalisa.contaBancaria.service;

import com.catalisa.contaBancaria.exception.ContaNotFoundException;
import com.catalisa.contaBancaria.model.ContaBancariaModel;
import com.catalisa.contaBancaria.repository.ContaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaBancariaService {
    @Autowired //implementa a interface sem instanciar(não precisa fazer implements...)
    ContaBancariaRepository contaBancariaRepository;

    //lista da tabela para retornar todos os dados
    public List<ContaBancariaModel> buscarTodas() {
        return contaBancariaRepository.findAll();
    }

    //busca cliente por Id
    public Optional<ContaBancariaModel> buscarPorId(Long id) {
        return contaBancariaRepository.findById(id);
    }

    //cadastra nova conta
    public ContaBancariaModel cadastrar(ContaBancariaModel contaBancariaModel) {
        contaBancariaModel.getId();
        contaBancariaModel.getNomeUsuario();
        contaBancariaModel.getNumeroConta();
        contaBancariaModel.getAgencia();
        contaBancariaModel.getUltimoValorFornecido();
        contaBancariaModel.getValorAtual();

        return contaBancariaRepository.save(contaBancariaModel);
    }

    public ContaBancariaModel atualizarConta(Long id, ContaBancariaModel atualizacao) {
        Optional<ContaBancariaModel> conta = contaBancariaRepository.findById(id);
        if (conta.isPresent()) {
            ContaBancariaModel contaDoCliente = conta.get();

            if (atualizacao.getNumeroConta() != null) {
                contaDoCliente.setNumeroConta(atualizacao.getNumeroConta());
            }
            if (atualizacao.getAgencia() != null) {
                contaDoCliente.setAgencia(atualizacao.getAgencia());
            }
            if (atualizacao.getNomeUsuario() != null) {
                contaDoCliente.setNomeUsuario(atualizacao.getNomeUsuario());
            }

            double valorFornecido = atualizacao.getUltimoValorFornecido();
            String tipoServico = atualizacao.getTipoServico();

            if ("saque".equalsIgnoreCase(tipoServico)) {
                double novoValorAtual = contaDoCliente.getValorAtual() - valorFornecido;

                if (novoValorAtual >= 0) {
                    contaDoCliente.setValorAtual(novoValorAtual);
                    contaDoCliente.setUltimoValorFornecido(valorFornecido);
                    contaDoCliente.setTipoServico("saque");
                } else {
                    throw new IllegalArgumentException("Saldo insuficiente para realizar o saque.");
                }
            } else if ("deposito".equalsIgnoreCase(tipoServico)) {
                double novoValorAtual = contaDoCliente.getValorAtual() + valorFornecido;
                contaDoCliente.setValorAtual(novoValorAtual);
                contaDoCliente.setUltimoValorFornecido(valorFornecido);
                contaDoCliente.setTipoServico("deposito");
            } else {
                throw new IllegalArgumentException("Tipo de serviço inválido. Use 'saque' ou 'deposito'.");
            }

            return contaBancariaRepository.save(contaDoCliente);
        } else {
            throw new ContaNotFoundException("Conta não encontrada com o ID: " + id);
        }
    }

    public ContaBancariaModel realizarDeposito(Long id, double valorDeposito) {
        Optional<ContaBancariaModel> conta = contaBancariaRepository.findById(id);
        if (conta.isPresent()) {
            ContaBancariaModel contaDoCliente = conta.get();
            double novoValorAtual = contaDoCliente.getValorAtual() + valorDeposito;
            contaDoCliente.setValorAtual(novoValorAtual);
            return contaBancariaRepository.save(contaDoCliente);
        } else {
            throw new ContaNotFoundException("Conta não encontrada com o ID: " + id);
        }
    }

    public ContaBancariaModel realizarSaque(Long id, double valorSaque) {
        Optional<ContaBancariaModel> conta = contaBancariaRepository.findById(id);
        if (conta.isPresent()) {
            ContaBancariaModel contaDoCliente = conta.get();
            double novoValorAtual = contaDoCliente.getValorAtual() - valorSaque;

            if (novoValorAtual >= 0) {
                contaDoCliente.setValorAtual(novoValorAtual);
                return contaBancariaRepository.save(contaDoCliente);
            } else {
                throw new IllegalArgumentException("Saldo insuficiente para realizar o saque.");
            }
        } else {
            throw new ContaNotFoundException("Conta não encontrada com o ID: " + id);
        }
    }

    public void deletarConta(Long id) {
        if (contaBancariaRepository.existsById(id)) {
            contaBancariaRepository.deleteById(id);
        } else {
            throw new ContaNotFoundException("Conta não encontrada com o ID: " + id);
        }
    }
}