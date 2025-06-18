package gmm.group.novobackend.view;

import gmm.group.novobackend.controller.AnimalController;
import gmm.group.novobackend.util.Erro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/animal")
public class AnimalRestView {
    // DECLARAÇÕES
    @Autowired
    private AnimalController animalController;

    // BUSCAR
    @GetMapping("buscar/{filtro}") // vazio, retorna todos
    public ResponseEntity<Object> getAnimais(@PathVariable(value = "filtro") String filtro) {
        List<Map<String, Object>> listaJson;

        //mando para a controller
        listaJson = animalController.onBuscar(filtro);
        if(listaJson != null)
            return ResponseEntity.ok().body(listaJson);
        else
            return ResponseEntity.badRequest().body(new Erro("Animal não encontrado ou nenhum animal cadastrado!!"));
    }
    @GetMapping("buscar-filtro/{filtro}")
    public ResponseEntity<Object> getAnimaisFiltro(@PathVariable(value = "filtro") String filtro)
    {
        List<Map<String, Object>> listaJson;
        listaJson = animalController.onBuscarFiltro(filtro);
        if (listaJson != null)
            return ResponseEntity.ok().body(listaJson);
        else
            return ResponseEntity.badRequest().body(new Erro("Animal não encontrado ou nenhum animal cadastrado!!"));
    }
    @GetMapping("buscar-id/{id}")
    public ResponseEntity<Object> getAnimais(@PathVariable(value = "id") int id) {
        Map<String, Object> json;

        //mando para a controller
        json = animalController.onBuscarId(id);
        if(json != null) {
            return ResponseEntity.ok(json);
        }
        else
            return ResponseEntity.badRequest().body(new Erro("Animal não encontrado!!"));
    }
    @GetMapping("buscar-cor")
    public ResponseEntity<Object> getAnimaisCor()
    {
        List<String> listaCor;
        listaCor = animalController.onBuscarCor();
        if (listaCor != null)
            return ResponseEntity.ok().body(listaCor);
        else
            return ResponseEntity.badRequest().body(new Erro("Nenhum animal cadastrado!!"));
    }
    @GetMapping("buscar-raca")
    public ResponseEntity<Object> getAnimaisRaca()
    {
        List<String> listaRaca;
        listaRaca = animalController.onBuscarRaca();
        if (listaRaca != null)
            return ResponseEntity.ok().body(listaRaca);
        else
            return ResponseEntity.badRequest().body(new Erro("Nenhum animal cadastrado!!"));
    }
    // GRAVAR
    @PostMapping("gravar")
    public ResponseEntity<Object> gravarAnimal(
            @RequestParam String nome,
            @RequestParam String sexo,
            @RequestParam String raca,
            @RequestParam String dataNascimento,
            @RequestParam double peso,
            @RequestParam String castrado,
            @RequestParam String adotado,
            @RequestParam String cor,
            @RequestParam String especie,
            @RequestParam MultipartFile imagemBase64) throws IOException
    {
        //criar o mapeamento do meu json ANIMAL

        Map<String, Object> json = new HashMap<>();
        json.put("nome", nome);
        json.put("sexo", sexo);
        json.put("raca", raca);
        json.put("dataNascimento", dataNascimento);
        json.put("peso", peso);
        json.put("castrado", castrado);
        json.put("adotado", adotado);
        json.put("cor", cor);
        json.put("especie", especie);
        String imagemBase64Encoded = Base64.getEncoder().encodeToString(imagemBase64.getBytes());
        System.out.println(json);
        json.put("imagemBase64", imagemBase64Encoded);
        //System.out.println("JSON recebido: " + json);
        if (animalController.onGravar(json)) //json -> enviar
            return ResponseEntity.ok(json);
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao gravar animal!!"));
    }

    // DELETE
    @DeleteMapping("excluir/{id}") //
    public ResponseEntity<Object> excluirAnimal(@PathVariable (value = "id") int codAnimal) {
       if(animalController.onDelete(codAnimal))
           return ResponseEntity.ok(new Erro("Animal excluido com sucesso!"));
       return ResponseEntity.badRequest().body(new Erro("Erro ao excluir animal!!"));
    }

    // ATUALIZAR
    @PutMapping("atualizar")
    public ResponseEntity<Object> atualizarAnimal(
            @RequestParam int codAnimal,
            @RequestParam String nome,
            @RequestParam String sexo,
            @RequestParam String raca,
            @RequestParam String dataNascimento,
            @RequestParam double peso,
            @RequestParam String castrado,
            @RequestParam String adotado,
            @RequestParam String cor,
            @RequestParam String especie,
            @RequestParam MultipartFile imagemBase64) throws IOException {
        //criar o mapeamento do meu json ANIMAL
        Map<String, Object> json = new HashMap<>();
        json.put("codAnimal", codAnimal);
        json.put("nome", nome);
        json.put("sexo", sexo);
        json.put("raca", raca);
        json.put("dataNascimento", dataNascimento);
        json.put("peso", peso);
        json.put("castrado", castrado);
        json.put("adotado", adotado);
        json.put("cor", cor);
        json.put("especie", especie);
        String imagemBase64Encoded = Base64.getEncoder().encodeToString(imagemBase64.getBytes());
        json.put("imagemBase64", imagemBase64Encoded);

        if (animalController.onAlterar(json)) {
            return ResponseEntity.ok(json);
        } else
            return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar animal!!"));
    }
}
