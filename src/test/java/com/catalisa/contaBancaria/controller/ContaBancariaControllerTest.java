package com.catalisa.contaBancaria.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.catalisa.contaBancaria.model.ContaBancariaModel;
import com.catalisa.contaBancaria.service.ContaBancariaService;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//indica que é uma classe de teste de integracao para que o contexto completo do spring seja carregado
//DUVIDA: é melhor usar @SpringBootTest junto com @RunWith(SpringRunner.class)?
@SpringBootTest
//simula solicitações HTTP sem a necessidade de um servidor real
@AutoConfigureMockMvc
//@WebMvcTest(ContaBancariaController.class) ---------> não roda quando uso essa config
public class ContaBancariaControllerTest {

    @Autowired
    MockMvc mockMvc;

    //converte objeto java em json
    @Autowired
    private ObjectMapper objectMapper;

    // cria um mock (simulação) de um bean em um ambiente de teste no Spring (a classe service)
    @MockBean
    private ContaBancariaService contaBancariaService;

    @Test
    @DisplayName("Cadastrar conta")
    public void testarCadastrarContaBancaria() throws Exception {
        ContaBancariaModel contaBancariaModel = new ContaBancariaModel();
        contaBancariaModel.setId(1L);
        contaBancariaModel.setNumeroConta("12345678");
        contaBancariaModel.setAgencia("1212");
        contaBancariaModel.setNomeUsuario("Cliente1");
        contaBancariaModel.setValorAtual(10);
        contaBancariaModel.setUltimoValorFornecido(1000);
        contaBancariaModel.setTipoServico("Deposito");

        //mockito.when: configura uma ação quando o método cadastrar do objeto contaBancariaService for chamado
        //Mockito.any: qualquer argumento do tipo ContaBancariaModel será aceito quando o método cadastrar for chamado
        //thenReturn: quando o método cadastrar for chamado com qualquer instância de ContaBancariaModel, o mock retornará a instância contaBancariaModel
        when(contaBancariaService.cadastrar(Mockito.any(ContaBancariaModel.class)))
                .thenReturn(contaBancariaModel);

        //.perform(...): Isso inicia a simulação de uma solicitação HTTP
        //$: está se referindo ao próprio objeto JSON em consideração, indica o início do documento JSON retornado pela solicitação
        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaBancariaModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1)) //
                .andExpect(jsonPath("$.numeroConta").value("12345678"))
                .andExpect(jsonPath("$.agencia").value("1212"))
                .andExpect(jsonPath("$.nomeUsuario").value("Cliente1"))
                .andExpect(jsonPath("$.valorAtual").value(10))
                .andExpect(jsonPath("$.ultimoValorFornecido").value(1000))
                .andExpect(jsonPath("$.tipoServico").value("Deposito"));
    }

    @Test
    @DisplayName("Listar contas")
    public void testarBuscarTodasContas() throws Exception {
        ContaBancariaModel contaBancariaModel = new ContaBancariaModel();
        contaBancariaModel.setNumeroConta("12345678");
        contaBancariaModel.setAgencia("1212");
        contaBancariaModel.setNomeUsuario("Cliente1");
        contaBancariaModel.setValorAtual(1000);
        contaBancariaModel.setUltimoValorFornecido(500);
        contaBancariaModel.setTipoServico("Deposito");

        ContaBancariaModel contaModel2 = new ContaBancariaModel();
        contaModel2.setNumeroConta("87654321");
        contaModel2.setAgencia("3434");
        contaModel2.setNomeUsuario("Cliente2");
        contaModel2.setValorAtual(1500);
        contaModel2.setUltimoValorFornecido(700);
        contaModel2.setTipoServico("Saque");

        List<ContaBancariaModel> contas = new ArrayList<>();
        contas.add(contaBancariaModel);
        contas.add(contaModel2);

        when(contaBancariaService.listarContas()).thenReturn(contas);

        mockMvc.perform(get("/contas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroConta").value("12345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].agencia").value("1212"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeUsuario").value("Cliente1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].valorAtual").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ultimoValorFornecido").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tipoServico").value("Deposito"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].numeroConta").value("87654321"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].agencia").value("3434"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomeUsuario").value("Cliente2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].valorAtual").value(1500))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ultimoValorFornecido").value(700))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].tipoServico").value("Saque"));

        verify(contaBancariaService, times(1)).listarContas();
    }

    @Test
    @DisplayName("Buscar conta por id cadastrado")
    public void testarBuscarContaPorID() throws Exception {
        ContaBancariaModel contaBancariaModel = new ContaBancariaModel();
        contaBancariaModel.setNumeroConta("12345678");
        contaBancariaModel.setAgencia("1212");
        contaBancariaModel.setNomeUsuario("Cliente1");
        contaBancariaModel.setValorAtual(1000);
        contaBancariaModel.setUltimoValorFornecido(500);
        contaBancariaModel.setTipoServico("Deposito");

        when(contaBancariaService.buscarContaPorId(1L)).thenReturn(Optional.of(contaBancariaModel));

        mockMvc.perform(get("/contas/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroConta").value("12345678"))
                .andExpect(jsonPath("$.agencia").value("1212"))
                .andExpect(jsonPath("$.nomeUsuario").value("Cliente1"))
                .andExpect(jsonPath("$.valorAtual").value(1000))
                .andExpect(jsonPath("$.ultimoValorFornecido").value(500))
                .andExpect(jsonPath("$.tipoServico").value("Deposito"));
    }

/*    @Test
    public void testarAtualizacaoConta() throws Exception {
        ContaBancariaModel contaExistente = new ContaBancariaModel();
        contaExistente.setId(1L);
        contaExistente.setNumeroConta("123456");
        contaExistente.setAgencia("7890");
        contaExistente.setNomeUsuario("João");

        ContaBancariaModel atualizacao = new ContaBancariaModel();
        atualizacao.setId(1L); // Correção: Definir o ID da atualização
        atualizacao.setNumeroConta("654321");
        atualizacao.setAgencia("1011");
        atualizacao.setNomeUsuario("Maria");

        // Quando o método buscarContaPorId do serviço contaBancariaService for chamado com o ID da conta existente, ele deve retornar um Optional contendo a própria contaExistente, que é a conta com os detalhes iniciais
        when(contaBancariaService.buscarContaPorId(1L)).thenReturn(Optional.of(contaExistente)); // Correção: Passar diretamente o ID 1L
        // eq(contaExistente.getId()): verifica se o ID passado para o método atualizarConta é o mesmo da conta existente
        // any(ContaBancariaModel.class): qualquer objeto do tipo ContaBancariaModel pode ser passado como segundo argumento para o método atualizarConta, e o mock retornará o objeto atualizacao
        when(contaBancariaService.atualizarConta(eq(1L), any(ContaBancariaModel.class))).thenReturn(atualizacao); // Correção: Passar diretamente o ID 1L

        mockMvc.perform(MockMvcRequestBuilders.put("/contas/{id}", 1) // Correção: Passar diretamente o ID 1
                        .contentType(MediaType.APPLICATION_JSON)
                        // Define o conteúdo do corpo da solicitação com o objeto atualizacao convertido em uma representação JSON usando o ObjectMapper
                        .content(objectMapper.writeValueAsString(atualizacao)))
                .andExpect(status().isOk());
    }*/

    @Test
    @DisplayName("Deletar conta existente")
    public void testarDeletarConta() throws Exception {
        Long contaId = 2L;

        //não executa nenhuma ação real quando o método deletarConta for chamado
        doNothing().when(contaBancariaService).deletarConta(contaId);

        mockMvc.perform(delete("/contas/2")
                        .contentType(MediaType.APPLICATION_JSON))
                //isNoContent: verifica se o código de resposta é 204(No Content), pois quando se trata de endpoints de exclusão é mais adequado verificar ele já que não há nenhum conteúdo a ser retornado, apenas a confirmação de que a exclusão foi realizada com sucesso
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}


