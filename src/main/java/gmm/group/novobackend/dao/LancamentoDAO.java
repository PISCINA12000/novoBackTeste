package gmm.group.novobackend.dao;

import gmm.group.novobackend.entities.Lancamento;
import gmm.group.novobackend.util.Conexao;
import gmm.group.novobackend.util.IDAL;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LancamentoDAO implements IDAL<Lancamento> {

    @Override
    public boolean gravar(Lancamento entidade, Conexao conexao) {
        String sql = """
                INSERT INTO lancamento (
                    lan_codTpLanc, lan_codAnimal, lan_data, lan_debito, lan_credito, 
                    lan_descricao, lan_valor, lan_pdf
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = conexao.getPreparedStatement(sql)) {
            stmt.setInt(1, entidade.getCodTpLanc());

            //se o código recebido for 0, então eu seto nullo na minha base de dados
            if (entidade.getCodAnimal() == 0)
                stmt.setNull(2, Types.INTEGER);
            else
                stmt.setInt(2, entidade.getCodAnimal());

            // Converte a String de data para o formato de Date do SQL
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date parsedDate = dateFormat.parse(entidade.getData());
            stmt.setDate(3, new Date(parsedDate.getTime()));
            //stmt.setDate(3, Date.valueOf(entidade.getData()));

            stmt.setInt(4, entidade.getDebito());
            stmt.setInt(5, entidade.getCredito());
            stmt.setString(6, entidade.getDescricao());
            stmt.setDouble(7, entidade.getValor());

            //para caso eu não envie pdf algum
            if (entidade.getPDF() == null)
                stmt.setNull(8, Types.BINARY);
            else
                stmt.setBytes(8, entidade.getPDF());

            return stmt.executeUpdate() > 0;
        } catch (SQLException | ParseException e) {
            e.printStackTrace(); // ou logue o erro
            return false;
        }
    }

    @Override
    public boolean alterar(Lancamento entidade, Conexao conexao) {
        String sql = """
                    UPDATE lancamento
                    SET lan_codTpLanc = ?, lan_codAnimal = ?, lan_data = ?, lan_debito = ?, lan_credito = ?, 
                        lan_descricao = ?, lan_valor = ?, lan_pdf = ?
                    WHERE lan_id = ?
                """;

        try (PreparedStatement ps = conexao.getPreparedStatement(sql)) {
            ps.setInt(1, entidade.getCodTpLanc());

            //se o código recebido for 0, então eu seto nullo na minha base de dados
            if (entidade.getCodAnimal() == 0)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, entidade.getCodAnimal());

            // Converte a String de data para o formato de Date do SQL
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date parsedDate = dateFormat.parse(entidade.getData());
            ps.setDate(3, new Date(parsedDate.getTime()));
            //ps.setDate(3, Date.valueOf(entidade.getData()));

            ps.setInt(4, entidade.getDebito());
            ps.setInt(5, entidade.getCredito());
            ps.setString(6, entidade.getDescricao());
            ps.setDouble(7, entidade.getValor());

            //para caso eu não envie pdf algum
            if (entidade.getPDF() == null)
                ps.setNull(8, Types.BINARY);
            else
                ps.setBytes(8, entidade.getPDF());

            ps.setInt(9, entidade.getCod());

            return ps.executeUpdate() > 0;
        } catch (SQLException | ParseException e) {
            e.printStackTrace(); // ou logue como preferir
            return false;
        }
    }

    @Override
    public boolean apagar(Lancamento entidade, Conexao conexao) {
        String sql = "DELETE FROM lancamento WHERE lan_id = " + entidade.getCod();
        return conexao.manipular(sql);
    }

    @Override
    public Lancamento get(int id, Conexao conexao) {
        Lancamento lancamento = null;
        String sql = "SELECT * FROM lancamento WHERE lan_id = " + id;
        ResultSet resultSet = conexao.consultar(sql);
        try {
            if (resultSet.next()) {
                lancamento = new Lancamento(
                        id,
                        resultSet.getDate("lan_data").toString(),
                        resultSet.getInt("lan_codTpLanc"),
                        resultSet.getInt("lan_codAnimal"),
                        resultSet.getInt("lan_debito"),
                        resultSet.getInt("lan_credito"),
                        resultSet.getString("lan_descricao"),
                        resultSet.getDouble("lan_valor"),
                        resultSet.getBytes("lan_pdf")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lancamento;
    }

    @Override
    public List<Lancamento> get(String filtro, Conexao conexao) {
        List<Lancamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM lancamento";
        if (!filtro.isEmpty() && !filtro.equals(" ")) {
            sql += " WHERE lan_descricao ILIKE '%" + filtro + "%'";
        }
        sql += " ORDER BY lan_descricao";
        System.out.println("SQL gerado: " + sql);
        ResultSet resultSet = conexao.consultar(sql);
        try {
            while (resultSet.next()) {
                lista.add(new Lancamento(
                        resultSet.getInt("lan_id"),
                        resultSet.getDate("lan_data").toString(),
                        resultSet.getInt("lan_codTpLanc"),
                        resultSet.getInt("lan_codAnimal"),
                        resultSet.getInt("lan_debito"),
                        resultSet.getInt("lan_credito"),
                        resultSet.getString("lan_descricao"),
                        resultSet.getDouble("lan_valor"),
                        resultSet.getBytes("lan_pdf")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Lancamento> getAnimal(int animalId, Conexao conexao) {
        List<Lancamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM lancamento WHERE lan_codAnimal =" + animalId;

        sql += " ORDER BY lan_data";

        //System.out.println("SQL gerado: " + sql);
        ResultSet resultSet = conexao.consultar(sql);
        try {
            while (resultSet.next()) {
                lista.add(new Lancamento(
                        resultSet.getInt("lan_id"),
                        resultSet.getDate("lan_data").toString(),
                        resultSet.getInt("lan_codTpLanc"),
                        resultSet.getInt("lan_codAnimal"),
                        resultSet.getInt("lan_debito"),
                        resultSet.getInt("lan_credito"),
                        resultSet.getString("lan_descricao"),
                        resultSet.getDouble("lan_valor"),
                        resultSet.getBytes("lan_pdf")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Map<String, Object>> getAnos(Conexao conexao) {
        List<Map<String, Object>> anos = new ArrayList<>();
        String sql = "SELECT DISTINCT extract(year from lan_data) as ano FROM lancamento ORDER BY ano";

        ResultSet resultSet = conexao.consultar(sql);
        try {
            while (resultSet.next()) {
                Map<String, Object> ano = new HashMap<>();
                ano.put("ano", resultSet.getInt("ano"));
                anos.add(ano);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return anos;
    }

    public Map<String, Object> somatorioTipoPag(String debCred, int codTpPag, int ano, Conexao conexao) {
        Map<String, Object> soma = new HashMap<>();
        if (debCred.equalsIgnoreCase("credito")) {
            //faço o somatório do crédito==codTpPag
            String sql = """
                    SELECT SUM(lancamento.lan_valor) AS soma_valores
                    FROM lancamento inner join plano_contas_gerencial
                        ON lancamento.lan_credito = plano_contas_gerencial.pcg_id
                    WHERE plano_contas_gerencial.pcr_id = #1
                    AND EXTRACT(YEAR FROM lan_data) = #2
                    """;
            sql = sql.replace("#1", "" + codTpPag)
                    .replace("#2", "" + ano);
            ResultSet resultSet = conexao.consultar(sql);
            try {
                resultSet.next();
                soma.put("soma", resultSet.getDouble("soma_valores"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //faço o somatório do débito==codTpPag
            String sql = """
                    SELECT SUM(lancamento.lan_valor) AS soma_valores
                    FROM lancamento inner join plano_contas_gerencial
                        ON lancamento.lan_debito = plano_contas_gerencial.pcg_id
                    WHERE plano_contas_gerencial.pcr_id = #1
                    AND EXTRACT(YEAR FROM lan_data) = #2
                    """;
            sql = sql.replace("#1", "" + codTpPag)
                    .replace("#2", "" + ano);
            ResultSet resultSet = conexao.consultar(sql);
            try {
                resultSet.next();
                soma.put("soma", resultSet.getDouble("soma_valores"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return soma;
    }

    public Map<String, Object> somatorioTipoPag(String debCred, int codTpPag, int ano, Integer mes, Conexao conexao) {
        Map<String, Object> soma = new HashMap<>();
        if (debCred.equalsIgnoreCase("credito")) {
            //faço o somatório do crédito==codTpPag
            String sql = """
                    SELECT SUM(lancamento.lan_valor) AS soma_valores
                    FROM lancamento inner join plano_contas_gerencial
                        ON lancamento.lan_credito = plano_contas_gerencial.pcg_id
                    WHERE plano_contas_gerencial.pcr_id = #1
                    AND EXTRACT(YEAR FROM lan_data) = #2 AND EXTRACT(MONTH FROM lan_data) = #3
                    """;
            sql = sql.replace("#1", "" + codTpPag)
                    .replace("#2", "" + ano)
                    .replace("#3", "" + mes);
            ResultSet resultSet = conexao.consultar(sql);
            try {
                resultSet.next();
                soma.put("soma", resultSet.getDouble("soma_valores"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //faço o somatório do débito==codTpPag
            String sql = """
                    SELECT SUM(lancamento.lan_valor) AS soma_valores
                    FROM lancamento inner join plano_contas_gerencial
                        ON lancamento.lan_debito = plano_contas_gerencial.pcg_id
                    WHERE plano_contas_gerencial.pcr_id = #1
                    AND EXTRACT(YEAR FROM lan_data) = #2 AND EXTRACT(MONTH FROM lan_data) = #3
                    """;
            sql = sql.replace("#1", "" + codTpPag)
                    .replace("#2", "" + ano)
                    .replace("#3", "" + mes);
            ResultSet resultSet = conexao.consultar(sql);
            try {
                resultSet.next();
                soma.put("soma", resultSet.getDouble("soma_valores"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return soma;
    }

    public List<Lancamento> getByData(String dataInicial, String dataFinal, Conexao conexao) {
        List<Lancamento> lista = new ArrayList<>();
        String dataFinalFormatada, dataInicialFormatada, sql;

        if (dataInicial == null && dataFinal == null) {
            return get("", conexao);
        }
        else if (dataInicial == null) { // Ou seja, quero tudo até a data final
            // Converter para AAAA-MM-DD
            String[] partesDataFinal = dataFinal.split("/");
            dataFinalFormatada = partesDataFinal[2] + "-" + partesDataFinal[1] + "-" + partesDataFinal[0];

            sql = """
                        SELECT * FROM lancamento
                        WHERE lan_data <= '#1'
                    """;
            sql = sql.replace("#1", dataFinalFormatada);
        }
        else if (dataFinal == null) { // Quero tudo pra frente da data inicial
            // Converter para AAAA-MM-DD
            String[] partesDataInicial = dataInicial.split("/");
            dataInicialFormatada = partesDataInicial[2] + "-" + partesDataInicial[1] + "-" + partesDataInicial[0];

            sql = """
                        SELECT * FROM lancamento
                        WHERE lan_data >= '#1'
                    """;
            sql = sql.replace("#1", dataInicialFormatada);
        }
        else { // Quero tudo entre o intervalo
            // Converter as duas datas para AAAA-MM-DD
            String[] partesDataFinal = dataFinal.split("/");
            dataFinalFormatada = partesDataFinal[2] + "-" + partesDataFinal[1] + "-" + partesDataFinal[0];

            String[] partesDataInicial = dataInicial.split("/");
            dataInicialFormatada = partesDataInicial[2] + "-" + partesDataInicial[1] + "-" + partesDataInicial[0];

            sql = """
                        SELECT * FROM lancamento
                        WHERE lan_data BETWEEN '#1' AND '#2'
                    """;
            sql = sql.replace("#1", dataInicialFormatada)
                    .replace("#2", dataFinalFormatada);
        }

        System.out.println("SQL gerado: " + sql);
        ResultSet resultSet = conexao.consultar(sql);
        try {
            while (resultSet.next()) {
                lista.add(new Lancamento(
                        resultSet.getInt("lan_id"),
                        resultSet.getDate("lan_data").toString(),
                        resultSet.getInt("lan_codTpLanc"),
                        resultSet.getInt("lan_codAnimal"),
                        resultSet.getInt("lan_debito"),
                        resultSet.getInt("lan_credito"),
                        resultSet.getString("lan_descricao"),
                        resultSet.getDouble("lan_valor"),
                        resultSet.getBytes("lan_pdf")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
