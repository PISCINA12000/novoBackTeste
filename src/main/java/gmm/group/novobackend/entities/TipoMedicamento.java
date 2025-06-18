package gmm.group.novobackend.entities;

import gmm.group.novobackend.dao.TipoMedicamentoDAO;
import gmm.group.novobackend.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TipoMedicamento {

    @Autowired
    private TipoMedicamentoDAO tipoMedicamentoDAO;

    private int cod;
    private String nome;
    private String formaFarmaceutica;
    private String descricao;

    // Construtores
    public TipoMedicamento(int cod, String nome, String formaFarmaceutica, String descricao) {
        this.cod = cod;
        this.nome = nome;
        this.formaFarmaceutica = formaFarmaceutica;
        this.descricao = descricao;
        if (tipoMedicamentoDAO == null)
            tipoMedicamentoDAO = new TipoMedicamentoDAO();
    }

    public TipoMedicamento() {
        this(0, "", "", "");
    }

    // Gets e Sets
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    //CRUD
    public List<TipoMedicamento> consultar(String filtro, Conexao conexao) {
        return tipoMedicamentoDAO.get(filtro, conexao);
    }

    public TipoMedicamento consultarID(int id, Conexao conexao) {
        return tipoMedicamentoDAO.get(id, conexao);
    }

    public boolean incluir(Conexao conexao) {
        return tipoMedicamentoDAO.gravar(this, conexao); // grava no banco
    }

    public boolean excluir(Conexao conexao) {
        return tipoMedicamentoDAO.apagar(this, conexao);
    }

    public boolean alterar(Conexao conexao) {
        return tipoMedicamentoDAO.alterar(this, conexao);
    }
}
