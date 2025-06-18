package gmm.group.novobackend.controller;

import gmm.group.novobackend.entities.Adocao;
import gmm.group.novobackend.entities.Animal;
import gmm.group.novobackend.util.Conexao;
import gmm.group.novobackend.util.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnimalController {

    @Autowired
    private Animal animalModel;

    public boolean onGravar(Map<String, Object> json) {
        if (validar(json)) {
            //criar aqui a conexão para o banco de dados
            SingletonDB singletonDB = SingletonDB.getInstance();
            Conexao conexao = singletonDB.getConexao();

            //transferir oq chegou de JSON para a instência de Animal
            animalModel.setNome(json.get("nome").toString());
            animalModel.setSexo(json.get("sexo").toString());
            animalModel.setRaca(json.get("raca").toString());
            animalModel.setDataNascimento(json.get("dataNascimento").toString());
            animalModel.setPeso(Double.parseDouble(json.get("peso").toString()));
            animalModel.setCastrado(json.get("castrado").toString());
            animalModel.setAdotado(json.get("adotado").toString());
            animalModel.setImagemBase64(json.get("imagemBase64").toString());
            animalModel.setCor(json.get("cor").toString());
            animalModel.setEspecie(json.get("especie").toString());
            System.out.println(json.get("imagemBase64").toString());
            System.out.println(animalModel.getDataNascimento());
            if (animalModel.incluir(conexao)) {
                // commit; finalizar transação e desconectar
                return true;
            }

            //se chegou até aqui é porque algo deu errado
            // rollback; finalizar t. desconecta;
            return false;
        }
        return false;
    }

    public boolean onDelete(int id) {
        // Criando a conexão
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();

        // Consultando o animal pelo ID
        Animal animal = animalModel.consultarID(id, conexao);
        // Se o animal for encontrado, exclui; caso contrário, retorna false
        if (animal != null)
        {
            Adocao adocao = new Adocao();
            Adocao a;
            List<Adocao> adocaoList = adocao.consultar("", conexao);
            int i = 0;
            while (i < adocaoList.size())
            {
                a = adocaoList.get(i);
                if (a.getAnimal().getCodAnimal() == animal.getCodAnimal())
                {
                    a.excluir(conexao);
                }
                i++;
            }
            return animal.excluir(conexao);
        }

        return false;
    }


    public Map<String, Object> onBuscarId(int id) {
        // Criando a conexão
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();

        // Consultando o animal pelo ID
        Animal animal = animalModel.consultarID(id, conexao);

        // Retornando os dados do animal, se encontrado
        if (animal != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("codAnimal", animal.getCodAnimal());
            json.put("nome", animal.getNome());
            json.put("sexo", animal.getSexo());
            json.put("raca", animal.getRaca());
            json.put("dataNascimento", animal.getDataNascimento());
            json.put("peso", animal.getPeso());
            json.put("castrado", animal.getCastrado());
            json.put("adotado", animal.getAdotado());
            json.put("imagemBase64", animal.getImagemBase64());
            json.put("cor", animal.getCor());
            json.put("especie", animal.getEspecie());
            return json;
        }

        // Retorna null se o animal não for encontrado
        return null;
    }
    public List<Map<String, Object>> onBuscarFiltro(String filtro)
    {
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();

        //criando a lista que conterá os JSON's
        List<Animal> lista = animalModel.consultarFiltro(filtro, conexao);

        //verificação de a minha lista JSON está vazia ou não
        if (lista!=null) {
            //crio uma lista json contendo os animais que retornaram no meu consultar
            List<Map<String, Object>> listaJson = new ArrayList<>();
            for (int i=0; i<lista.size(); i++) {
                Map<String, Object> json = new HashMap<>();
                json.put("codAnimal", lista.get(i).getCodAnimal());
                json.put("nome", lista.get(i).getNome());
                json.put("sexo", lista.get(i).getSexo());
                json.put("raca", lista.get(i).getRaca());
                json.put("dataNascimento", lista.get(i).getDataNascimento());
                json.put("peso", lista.get(i).getPeso());
                json.put("castrado", lista.get(i).getCastrado());
                json.put("adotado", lista.get(i).getAdotado());
                json.put("imagemBase64", lista.get(i).getImagemBase64());
                json.put("cor", lista.get(i).getCor());
                json.put("especie", lista.get(i).getEspecie());
                listaJson.add(json);
            }
            return listaJson;
        }
        else
            return null;
    }
    public List<String> onBuscarCor()
    {
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();
        List<String> listaCor = animalModel.consultarCor(conexao);
        if (listaCor != null)
        {
            return listaCor;
        }
        else
            return null;
    }
    public List<String> onBuscarRaca()
    {
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();
        List<String> listaRaca = animalModel.consultarRaca(conexao);
        if (listaRaca != null)
        {
            return listaRaca;
        }
        else
            return null;
    }
    public List<Map<String, Object>> onBuscar(String filtro) {
        //criando a conexão
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();

        //criando a lista que conterá os JSON's
        List<Animal> lista = animalModel.consultar(filtro, conexao);

        //verificação de a minha lista JSON está vazia ou não
        if (lista!=null) {
            //crio uma lista json contendo os animais que retornaram no meu consultar
            List<Map<String, Object>> listaJson = new ArrayList<>();
            for (int i=0; i<lista.size(); i++) {
                Map<String, Object> json = new HashMap<>();
                json.put("codAnimal", lista.get(i).getCodAnimal());
                json.put("nome", lista.get(i).getNome());
                json.put("sexo", lista.get(i).getSexo());
                json.put("raca", lista.get(i).getRaca());
                json.put("dataNascimento", lista.get(i).getDataNascimento());
                json.put("peso", lista.get(i).getPeso());
                json.put("castrado", lista.get(i).getCastrado());
                json.put("adotado", lista.get(i).getAdotado());
                json.put("imagemBase64", lista.get(i).getImagemBase64());
                json.put("cor", lista.get(i).getCor());
                json.put("especie", lista.get(i).getEspecie());
                listaJson.add(json);
            }
            return listaJson;
        }
        else
            return null;
    }

    public boolean onAlterar(Map<String, Object> json) {
        //criando a conexão
        SingletonDB singletonDB = SingletonDB.getInstance();
        Conexao conexao = singletonDB.getConexao();
        if (validarAlterar(json))
        {
            Animal animal = animalModel.consultarID((int) json.get("codAnimal"), conexao);

            //Transferir o JSON recebido para a instância de modelo
            animalModel.setCodAnimal((Integer)json.get("codAnimal"));
            animalModel.setNome((String)json.get("nome"));
            animalModel.setSexo((String)json.get("sexo"));
            animalModel.setRaca((String)json.get("raca"));
            animalModel.setDataNascimento((String) json.get("dataNascimento"));
            animalModel.setPeso((Double)json.get("peso"));
            animalModel.setCastrado((String)json.get("castrado"));
            animalModel.setAdotado((String)json.get("adotado"));
            animalModel.setCor((String)json.get("cor"));
            animalModel.setEspecie((String)json.get("especie"));
            String imagemBase64 = json.get("imagemBase64").toString();
            if (imagemBase64 == null || imagemBase64.isEmpty())
            {
                imagemBase64 = animal.getImagemBase64();
            }
            animalModel.setImagemBase64(imagemBase64);

            return animalModel.alterar(conexao);
        }
        else
            return false;

    }

    public boolean validar(Map<String, Object> json) {
        //retorna verdade se todas as informações forem válidas
        return json != null &&
                json.containsKey("nome") &&
                json.containsKey("sexo") &&
                json.containsKey("raca") &&
                json.containsKey("dataNascimento") &&
                json.containsKey("peso") &&
                json.containsKey("castrado") &&
                json.containsKey("adotado") &&
                json.containsKey("cor") &&
                json.containsKey("especie");
    }

    public boolean validarAlterar(Map<String, Object> json) {
        //retorna verdade se todas as informações forem válidas
        if (validar(json) && json.containsKey("codAnimal"))
        {

            SingletonDB singletonDB = SingletonDB.getInstance();
            Conexao conexao = singletonDB.getConexao();

            List<Adocao> adocaoList = new Adocao().consultar("", conexao);
            if (adocaoList.size() > 0) // se houver adoções
            {
                int i = 0;
                while(i < adocaoList.size() && adocaoList.get(i).getAnimal().getCodAnimal() != (int) json.get("codAnimal"))
                    i++;
                if (i < adocaoList.size()) // se eu encontrar uma adoção com esse animal
                {
                    if (!adocaoList.get(i).getAnimal().getAdotado().equals(json.get("adotado")))
                        return false;

                }
            }
            return true;

        }
        return false;
    }



}
