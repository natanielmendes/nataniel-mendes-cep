package cedrobks.ejb.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import mack.ejb.beans.EnderecoBean;
import org.json.*;
import shared.entities.Endereco;

@Stateless
@LocalBean
@Path("/rest-api/")
public class CedroBksRestService {

    @EJB
    EnderecoBean enderecoBean;
    
    @POST
    @Path("/cadastro/")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Map cadastro(String enderecoJSON) {
        Map<String, String> map = new HashMap<String, String>();
        Endereco e = new Endereco();
        JSONObject obj = new JSONObject(enderecoJSON).getJSONObject("endereco");
        String cep = obj.getString("cep").substring(0, 5) + obj.getString("cep").substring(obj.getString("cep").length() - 3, obj.getString("cep").length());
        // Verificação de registro, levando em conta CEP e endereço
        if (enderecoBean.verificarEndereco(cep, obj.getString("endereco")).isEmpty()) {
            e.setCep(cep);
            e.setEndereco(obj.getString("endereco"));
            e.setBairro(obj.getString("bairro"));
            e.setCidade(obj.getString("cidade"));
            e.setEstado(obj.getString("estado"));
            enderecoBean.save(e);
            
            // Verificar se a inserção no banco de dados foi feita com sucesso
            if (enderecoBean.verificarEndereco(cep, obj.getString("endereco")).isEmpty()){
                map.put("status", "ERRO!");
                map.put("mensagem", "Ocorreu um erro no cadastro do endereço informado");
            } else {
                map.put("status", "SUCESSO!");
                map.put("mensagem", "O endereço foi cadastrado com sucesso.");
            }
            return map;
        } else {
            map.put("status", "ERRO!");
            map.put("mensagem", "O endereço informado já foi cadastrado.");
            return map;
        }
    }

    @GET
    @Path("/busca/{cepEscolhido}")
    @Produces({"application/json"})
    public Map listaEnderecos(@PathParam("cepEscolhido") final String cep) {
        Map<String, String> map = new HashMap<String, String>();

        if (cep.length() == 8) {
            // Verificar se existem somente números no CEP
            for (char digito : cep.toCharArray()) {
                if (!Character.isDigit(digito)) {
                    map.put("status", "ERRO!");
                    map.put("mensagem", "O CEP informado é inválido");
                    return map;
                }
            }
            
            // Fluxo padrão (retornar o CEP, caso exista no banco de dados)
            List<Endereco> enderecos = enderecoBean.list(cep);
            if (enderecos.isEmpty()) {
                map.put("status", "ERRO!");
                map.put("mensagem", "O CEP informado não foi encontrado");
            } else {
                for (Endereco e : enderecos) {
                    map.put("status", "SUCESSO!");
                    map.put("cep", e.getCep());
                    map.put("endereco", e.getEndereco());
                    map.put("bairro", e.getBairro());
                    map.put("cidade", e.getCidade());
                    map.put("estado", e.getEstado());
                }
            }
        } else {
            map.put("status", "ERRO!");
            map.put("mensagem", "O CEP informado é inválido");
        }
        return map;
    }
}
