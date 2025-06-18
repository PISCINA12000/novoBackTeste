package gmm.group.novobackend.view;

import gmm.group.novobackend.controller.DoacaoController;
import gmm.group.novobackend.util.Erro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/doacao")
public class DoacaoRestView
{
    @PostMapping("gravar")
    public ResponseEntity<Object> gravarDoacao(
            @RequestParam int cod_usuario,
            @RequestParam String status,
            @RequestParam String data,
            @RequestParam int valor){

        //criar o mapeamento do meu json ADOCAO
        Map<String, Object> json = new HashMap<>();
        json.put("usuario", cod_usuario);
        json.put("status", status);
        json.put("data", data);
        json.put("valor", valor);

        DoacaoController doacaoController = new DoacaoController();
        if (doacaoController.onGravar(json))
            return ResponseEntity.ok(json);
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao gravar doação!!"));
    }
    @GetMapping("buscar/{filtro}") // vazio, retorna todos
    public ResponseEntity<Object> getDoacao(@PathVariable(value = "filtro") String filtro) {
        List<Map<String, Object>> listaJson;

        //mando para a controller
        DoacaoController doacaoController = new DoacaoController();
        listaJson = doacaoController.onBuscar(filtro);
        if(listaJson != null)
            return ResponseEntity.ok().body(listaJson);
        else
            return ResponseEntity.badRequest().body(new Erro("Doação não encontrada ou nenhuma doação cadastrada!!"));
    }

    @GetMapping("buscar-id/{id}")
    public ResponseEntity<Object> getDoacao(@PathVariable(value = "id") int codDoacao) {
        Map<String, Object> json;

        //mando para a controller
        DoacaoController doacaoController = new DoacaoController();
        json = doacaoController.onBuscarId(codDoacao);
        if(json != null) {
            return ResponseEntity.ok(json);
        }
        else
            return ResponseEntity.badRequest().body(new Erro("Doação não encontrada!!"));
    }

    @DeleteMapping("excluir/{id}") //
    public ResponseEntity<Object> excluirDoacao(@PathVariable (value = "id") int codDoacao) {
        DoacaoController doacaoController = new DoacaoController();
        if(doacaoController.onDelete(codDoacao)) {
            return ResponseEntity.ok(new Erro("Doação excluida com sucesso!"));
        }
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao excluir doação!!"));
    }

    @PutMapping("atualizar")
    public ResponseEntity<Object> atualizarDoacao(
            @RequestParam int codDoacao,
            @RequestParam int cod_usuario,
            @RequestParam String status,
            @RequestParam String data,
            @RequestParam int valor)
    {
        Map<String, Object> json = new HashMap<>();
        json.put("codDoacao", codDoacao);
        json.put("usuario", cod_usuario);
        json.put("status", status);
        json.put("data", data);
        json.put("valor", valor);
        DoacaoController doacaoController = new DoacaoController();
        if (doacaoController.onAlterar(json)) {
            return ResponseEntity.ok(json);
        } else
            return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar doação!!"));
    }

    @GetMapping("buscarPorUsuario/{id}")
    public ResponseEntity<Object> getDoacoesPorUsuario(@PathVariable(value = "id") int codUsuario) {
        DoacaoController doacaoController = new DoacaoController();
        List<Map<String, Object>> listaJson = doacaoController.onBuscarPorUsuario(codUsuario);

        if (listaJson != null && !listaJson.isEmpty()) {
            return ResponseEntity.ok().body(listaJson);
        } else {
            return ResponseEntity.badRequest().body(new Erro("Nenhuma doação encontrada para este usuário."));
        }
    }

    @GetMapping("relatorio-por-data")
    public ResponseEntity<Object> relatorioPorData(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {

        DoacaoController controller = new DoacaoController();
        List<Map<String, Object>> lista = controller.buscarPorData(dataInicio, dataFim);

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.ok(List.of());
        }
    }
}