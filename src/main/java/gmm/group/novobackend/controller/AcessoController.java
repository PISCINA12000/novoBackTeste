package gmm.group.novobackend.controller;

import gmm.group.novobackend.entities.Usuario;
import gmm.group.novobackend.security.JWTTokenProvider;
import gmm.group.novobackend.util.Conexao;
import gmm.group.novobackend.util.SingletonDB;

public class AcessoController
{
    public String autenticar(String email, String senha)
    {
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();
        Usuario usuario = new Usuario();
        Usuario novoUsuario;
        novoUsuario = usuario.consultarEmail(email, conexao);
        String token = null;
        if (novoUsuario != null)
        {
            if (novoUsuario.getSenha().equals(senha))
                token = JWTTokenProvider.getToken(novoUsuario.getNome(), novoUsuario.getPrivilegio(), novoUsuario.getCod());
        }

        return token;
    }

}
