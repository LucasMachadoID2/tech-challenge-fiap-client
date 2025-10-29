package com.tech_challenge_fiap.bdd;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.E;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientSteps {

    @Autowired
    private MockMvc mockMvc;

    private MvcResult lastResult;

    @Dado("que existem clientes cadastrados no sistema")
    public void queExistemClientesCadastrados() {
        // Mock definido em CucumberSpringConfig
    }

    @Dado("que não existem clientes cadastrados no sistema")
    public void queNaoExistemClientesCadastrados() {
        // Mock redefinido em CucumberSpringConfig
    }

    @Quando("eu enviar uma requisição GET para {string}")
    public void euEnviarUmaRequisicaoGetPara(String path) throws Exception {
        lastResult = mockMvc.perform(get(path))
                .andReturn();
    }

    @Então("o status da resposta deve ser {int}")
    public void oStatusDaRespostaDeveSer(Integer statusCode) throws Exception {
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(statusCode);
    }

    @E("o tipo de conteúdo da resposta deve ser {string}")
    public void oTipoDeConteudoDaRespostaDeveSer(String contentType) {
        assertThat(lastResult.getResponse().getContentType())
                .startsWith(MediaType.valueOf(contentType).toString());
    }

    @E("a resposta deve conter uma lista de clientes")
    public void aRespostaDeveConterUmaListaDeClientes() throws Exception {
        String json = lastResult.getResponse().getContentAsString();
        assertThat(json).contains("id");
    }

    @E("cada cliente deve conter os campos {string}, {string}, {string} e {string}")
    public void cadaClienteDeveConterOsCampos(String id, String nome, String email, String cpf) throws Exception {
        String json = lastResult.getResponse().getContentAsString();
        assertThat(json).contains(id, nome, email, cpf);
    }

    @E("a resposta deve conter uma lista vazia")
    public void aRespostaDeveConterUmaListaVazia() throws Exception {
        String json = lastResult.getResponse().getContentAsString();
        assertThat(json).isEqualTo("[]");
    }
}
