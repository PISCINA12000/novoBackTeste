package gmm.group.novobackend.view;

import gmm.group.novobackend.controller.LancamentoController;
import gmm.group.novobackend.util.Erro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/lancamento")
public class LancamentoRestView {
    @Autowired
    private LancamentoController lancController;


    @GetMapping("buscar_animal/{animalId}")
    public ResponseEntity<Object> getLancamentoAnimal(@PathVariable(value = "animalId") int animalId) {
        List<Map<String, Object>> lista;

        //mando para a controller
        lista = lancController.onBuscarAnimal(animalId);
        if(lista!=null /*&& !lista.isEmpty()*/)  //aqui pode retornar lista vazia
            return ResponseEntity.ok().body(lista);
        return ResponseEntity.badRequest().body(new Erro("Não foi possível recuperar os lançamentos!!"));
    }

    @GetMapping("buscar/{filtro}")
    public ResponseEntity<Object> getLancamento(@PathVariable(value = "filtro") String filtro) {
        List<Map<String, Object>> lista;

        //mando para a controller
        lista = lancController.onBuscar(filtro);
        if(lista!=null && !lista.isEmpty())
            return ResponseEntity.ok().body(lista);
        return ResponseEntity.badRequest().body(new Erro("Não foi possível recuperar os lançamentos!!"));
    }

    @GetMapping("buscar")
    public ResponseEntity<Object> getLancamento(@RequestParam String dataIni, @RequestParam String dataFim) {
        List<Map<String, Object>> lista;

        //mando para a controller
        lista = lancController.onBuscarData(dataIni, dataFim);
        if(lista!=null && !lista.isEmpty())
            return ResponseEntity.ok().body(lista);
        return ResponseEntity.badRequest().body(new Erro("Não existe lançamentos dentro desse período!!"));
    }

    @GetMapping("buscar-filtro")
    public ResponseEntity<Object> getLancamentoFiltros(
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) String dataIni,
            @RequestParam(required = false) String dataFim)
    {
        List<Map<String,Object>> lancamentos;
        List<Map<String, Object>> lancFiltrado = new ArrayList<>();

        //mando para a controller
        lancamentos = lancController.onBuscarData(dataIni, dataFim);
        if(lancamentos != null && !lancamentos.isEmpty()){
            if(filtro != null && !filtro.isEmpty()){
                //filtrar os lancamentos dentro dessa data
                for(int i=0; i<lancamentos.size(); i++){
                    if(lancamentos.get(i).get("descricao") != null){
                        if(lancamentos.get(i).get("descricao").toString().contains(filtro.toLowerCase()))
                            lancFiltrado.add(lancamentos.get(i));
                    }
                }
                if(!lancFiltrado.isEmpty())
                    return ResponseEntity.ok().body(lancFiltrado);
                else
                    return ResponseEntity.badRequest().body(new Erro("Não possui lancamentos com esse filtros!!"));
            }
            else
                return ResponseEntity.ok().body(lancamentos);
        }
        return ResponseEntity.badRequest().body(new Erro("Não possui lancamentos com esse filtros!!"));

    }

    @GetMapping("buscar-id/{id}")
    public ResponseEntity<Object> getLancamento(@PathVariable(value = "id") int id) {
        Map<String, Object> json;

        //mando para a controller
        json = lancController.onBuscarID(id);
        if(json!=null)
            return ResponseEntity.ok().body(json);
        return ResponseEntity.badRequest().body(new Erro("Não encontrado lançamento com esse ID!!"));
    }

    @GetMapping("arquivo/{id}")
    public ResponseEntity<byte[]> getArquivoPDF(@PathVariable(value = "id") int id) {
        byte[] pdfBytes = lancController.onBuscarPDF(id); // view chama controller

        if (pdfBytes != null && pdfBytes.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.inline()
                    .filename("lancamento_" + id + ".pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("anos")
    public ResponseEntity<Object> getAnos() {
        List<Map<String,Object>> listaAnos;

        //mandar para a controller
        listaAnos = lancController.onGetAnos();
        if(listaAnos!=null && !listaAnos.isEmpty()){
            return ResponseEntity.ok().body(listaAnos);
        }
        return ResponseEntity.badRequest().body(new Erro("Não foi possível recuperar os anos!!"));
    }

    @PostMapping("gravar")
    public ResponseEntity<Object> postLancamento(
            @RequestParam String data,
            @RequestParam int codTpLanc,
            @RequestParam int codAnimal,
            @RequestParam int debito,
            @RequestParam int credito,
            @RequestParam String descricao,
            @RequestParam double valor,
            @RequestParam (value="pdf", required = false) MultipartFile pdf)
    {
        try {
            Map<String, Object> json = new HashMap<>();
            json.put("data", data);
            json.put("codTpLanc", codTpLanc);
            json.put("codAnimal", codAnimal);
            json.put("debito", debito);
            json.put("credito", credito);
            json.put("descricao", descricao);
            json.put("valor", valor);

            //agora tratar o pdf para um arquivo binário para conseguir passar para a minha controller
            //posso atribuir nulo para a chave pois o pdf é algo opcional
            if(pdf!=null && !pdf.isEmpty()) {
                byte[] arquivo = pdf.getBytes();
                json.put("arquivo", arquivo);
            }
            else
                json.put("arquivo", null);

            //mando para a controller
            if (lancController.onGravar(json))
                return ResponseEntity.ok().body(json);
            return ResponseEntity.badRequest().body(new Erro("Não foi possível GRAVAR o Lançamento!!"));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(
                    new Erro("Não foi possível GRAVAR o Lançamento e entrou na exceção!!"+ e.getMessage()));
        }
    }

    @PutMapping("atualizar")
    public ResponseEntity<Object> putLancamento(
            @RequestParam int id,
            @RequestParam String data,
            @RequestParam int codTpLanc,
            @RequestParam int codAnimal,
            @RequestParam int debito,
            @RequestParam int credito,
            @RequestParam String descricao,
            @RequestParam double valor,
            @RequestParam (value="pdf", required = false) MultipartFile pdf)
    {
        try {
            Map<String, Object> json = new HashMap<>();
            json.put("id", id);
            json.put("data", data);
            json.put("codTpLanc", codTpLanc);
            json.put("codAnimal", codAnimal);
            json.put("debito", debito);
            json.put("credito", credito);
            json.put("descricao", descricao);
            json.put("valor", valor);

            //agora tratar o pdf para um arquivo binário para conseguir passar para a minha controller
            //posso atribuir nulo para a chave pois o pdf é algo opcional
            if(pdf!=null && !pdf.isEmpty()) {
                byte[] arquivo = pdf.getBytes();
                json.put("arquivo", arquivo);
            }
            else
                json.put("arquivo", null);

            //mando para a controller
            if (lancController.onAtualizar(json))
                return ResponseEntity.ok().body(json);
            return ResponseEntity.badRequest().body(new Erro("Não foi possível ATUALIZAR o Lançamento!!"));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(
                    new Erro("Não foi possível ATUALIZAR o Lançamento e entrou na exceção!! "+ e.getMessage()));
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<Object> deleteLancamento(@PathVariable(value="id") int id) {
        //mandando para a controller
        if(lancController.onDelete(id))
            return ResponseEntity.ok().body(new Erro("Lançamento excluído com sucesso!"));
        return ResponseEntity.badRequest().body(new Erro("Não foi possível excluir o Lançamento!!"));
    }
}
