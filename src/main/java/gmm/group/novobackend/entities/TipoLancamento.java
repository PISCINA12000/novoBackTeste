package gmm.group.novobackend.entities;

import gmm.group.novobackend.dao.TipoLancamentoDAO;
import gmm.group.novobackend.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TipoLancamento {
    @Autowired
    private TipoLancamentoDAO tipoLancamentoDAO;

    private int cod;
    private String descricao;

    // Construtores
    public TipoLancamento(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
        if(tipoLancamentoDAO == null)
            tipoLancamentoDAO = new TipoLancamentoDAO();
    }

    public TipoLancamento() {
        this(0, "");
    }

    // Gets e Sets
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // CRUD ------------------------------------------------------------------
    public boolean incluir(Conexao conexao) {
        return tipoLancamentoDAO.gravar(this, conexao); // grava no banco
    }

    public boolean excluir(Conexao conexao) {
        return tipoLancamentoDAO.apagar(this,conexao);
    }

    public TipoLancamento consultarID(int id, Conexao conexao) {
        return tipoLancamentoDAO.get(id, conexao);
    }

    public List<TipoLancamento> consultar(String filtro, Conexao conexao) {
        return tipoLancamentoDAO.get(filtro, conexao);
    }

    public boolean alterar(Conexao conexao) {
        return tipoLancamentoDAO.alterar(this, conexao);
    }
}
