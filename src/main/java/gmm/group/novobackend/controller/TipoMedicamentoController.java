package gmm.group.novobackend.controller;

import gmm.group.novobackend.entities.TipoMedicamento;
import gmm.group.novobackend.util.Conexao;
import gmm.group.novobackend.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TipoMedicamentoController {
    @Autowired
    private TipoMedicamento tipoMedicamentoModel;

    public boolean onGravar(Map<String, Object> json) {
        if (validar(json)) {
            //criando a conexao
            SingletonDB singletonDB = SingletonDB.getInstance();
            Conexao conexao = singletonDB.getConexao();

            //setando os valores do json no objeto
            tipoMedicamentoModel.setNome(json.get("nome").toString());
            tipoMedicamentoModel.setFormaFarmaceutica(json.get("formaFarmaceutica").toString());
            tipoMedicamentoModel.setDescricao(json.get("descricao").toString());

            if (tipoMedicamentoModel.incluir(conexao))
                return true;
            return false;
        }
        return false;
    }

    public boolean onDelete(int id) {
        //criando a conexao
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();

        TipoMedicamento tipoMedicamento = tipoMedicamentoModel.consultarID(id, conexao);
        if (tipoMedicamento != null) {
            return tipoMedicamento.excluir(conexao);
        }
        return false;
    }

    public Map<String, Object> onBuscarId(int id) {
        //criando a conexao
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();

        //buscar pelo id
        TipoMedicamento tipoMedicamento = tipoMedicamentoModel.consultarID(id, conexao);
        if (tipoMedicamento != null) {
            //achou um medicamento
            Map<String, Object> json = new HashMap<>();
            json.put("cod", tipoMedicamento.getCod());
            json.put("nome", tipoMedicamento.getNome());
            json.put("formaFarmaceutica", tipoMedicamento.getFormaFarmaceutica());
            json.put("descricao", tipoMedicamento.getDescricao());
            return json;
        }
        return null;
    }

    public List<Map<String, Object>> onBuscar(String filtro) {
        //Criar a conexao
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();
        List<TipoMedicamento> lista = tipoMedicamentoModel.consultar(filtro, conexao);

        if (lista!=null) {
            //achou tipos de medicamentos
            List<Map<String, Object>> listaJson = new ArrayList<>();
            for (int i = 0; i < lista.size(); i++) {
                Map<String, Object> json = new HashMap<>();
                json.put("cod", lista.get(i).getCod());
                json.put("nome", lista.get(i).getNome());
                json.put("formaFarmaceutica", lista.get(i).getFormaFarmaceutica());
                json.put("descricao", lista.get(i).getDescricao());
                listaJson.add(json);
            }
            return listaJson;
        }
        return null;
    }

    public boolean onAlterar(Map<String, Object> json) {
        //Criar a conexao
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();

        //colocar valores na instância da entidade
        tipoMedicamentoModel.setCod(Integer.parseInt(json.get("cod").toString()));
        tipoMedicamentoModel.setNome(json.get("nome").toString());
        tipoMedicamentoModel.setFormaFarmaceutica(json.get("formaFarmaceutica").toString());
        tipoMedicamentoModel.setDescricao(json.get("descricao").toString());

        if (validarAlterar(json)) {
            return tipoMedicamentoModel.alterar(conexao);
        } else
            return false;
    }

    public boolean validar(Map<String, Object> json) {
        return !json.isEmpty() &&
                !json.get("nome").toString().isEmpty() &&
                !json.get("formaFarmaceutica").toString().isEmpty() &&
                !json.get("descricao").toString().isEmpty();
    }

    public boolean validarAlterar(Map<String, Object> json) {
        return validar(json) && Integer.parseInt(json.get("cod").toString()) > 0;
    }
}
