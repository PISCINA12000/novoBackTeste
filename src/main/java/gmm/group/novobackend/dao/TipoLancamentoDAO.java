package gmm.group.novobackend.dao;

import gmm.group.novobackend.entities.TipoLancamento;
import gmm.group.novobackend.util.Conexao;
import gmm.group.novobackend.util.IDAL;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class TipoLancamentoDAO implements IDAL<TipoLancamento> {

    @Override
    public boolean gravar(TipoLancamento entidade, Conexao conexao) {
        String sql = """
                INSERT INTO tipo_lancamento (tpl_descricao)
                VALUES ('#1')
                """;
        sql = sql.replace("#1", entidade.getDescricao());
        return conexao.manipular(sql);
    }

    @Override
    public boolean alterar(TipoLancamento entidade, Conexao conexao) {
        String sql = """
                UPDATE tipo_lancamento
                SET tpl_descricao = '#1'
                WHERE tpl_id = #2
                """;
        sql = sql.replace("#1", entidade.getDescricao())
                .replace("#2", "" + entidade.getCod());
        return conexao.manipular(sql);
    }

    @Override
    public boolean apagar(TipoLancamento entidade, Conexao conexao) {
        String sql = "DELETE FROM tipo_lancamento WHERE tpl_id = " + entidade.getCod();
        return conexao.manipular(sql);
    }

    @Override
    public TipoLancamento get(int id, Conexao conexao) {
        TipoLancamento tipoLancamento = null;
        String sql = "SELECT * FROM tipo_lancamento WHERE tpl_id = " + id;
        ResultSet resultSet = conexao.consultar(sql);
        try {
            if (resultSet.next()) {
                tipoLancamento = new TipoLancamento(
                        id,
                        resultSet.getString("tpl_descricao")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipoLancamento;
    }

    @Override
    public List<TipoLancamento> get(String filtro, Conexao conexao) {
        List<TipoLancamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_lancamento";
        if (!filtro.isEmpty() && !filtro.equals(" ")) {
            //filtro = "'" + filtro + "'";
            //sql += " WHERE tpl_descricao = " + filtro;
            sql += " WHERE tpl_descricao ILIKE '%" + filtro + "%'";
        }
        sql += " ORDER BY tpl_descricao";
        System.out.println("SQL gerado: " + sql);
        ResultSet resultSet = conexao.consultar(sql);
        try {
            while (resultSet.next()) {
                lista.add(new TipoLancamento(
                        resultSet.getInt("tpl_id"),
                        resultSet.getString("tpl_descricao")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
