package com.agenciadeviagem.first_spring_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@RequestMapping("/api/destinos")
public class DestinoViagemApi {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<DestinoViagem> destinos = new ArrayList();

    public static void main(String[] args) {
        SpringApplication.run(DestinoViagemApi.class, args);
    }

    // POST - Cadastrar destino
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping
    public ResponseEntity<DestinoViagem> cadastrarDestino(@RequestBody DestinoViagem destino) {
        destinos.add(destino);
        return new ResponseEntity(destino, HttpStatus.CREATED);
    }

    // GET - Listar todos os destinos
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping
    public ResponseEntity<List<DestinoViagem>> listarDestinos() {
        return new ResponseEntity(destinos, HttpStatus.OK);
    }

    // GET - Pesquisar destinos por nome ou localização
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/pesquisar")
    public ResponseEntity<List<DestinoViagem>> pesquisarDestinos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "localizacao", required = false) String localizacao) {

        List<DestinoViagem> destinosFiltrados = destinos.stream()
                .filter(destino -> (nome == null || destino.getNome().toLowerCase().contains(nome.toLowerCase())) &&
                        (localizacao == null || destino.getLocalizacao().toLowerCase().contains(localizacao.toLowerCase())))
                .collect(Collectors.toList());

        return new ResponseEntity(destinosFiltrados, HttpStatus.OK);
    }

    // GET - Visualizar informações detalhadas sobre um destino específico
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/{id}")
    public ResponseEntity<DestinoViagem> visualizarDestino(@PathVariable Long id) {
        Optional<DestinoViagem> destino = destinos.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();

        return destino.map(value -> new ResponseEntity(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    // PUT - Atualizar um destino existente (substituição completa)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PutMapping("/{id}")
    public ResponseEntity<DestinoViagem> atualizarDestino(@PathVariable Long id, @RequestBody DestinoViagem destinoAtualizado) {
        Optional<DestinoViagem> destinoExistente = destinos.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();

        if (destinoExistente.isPresent()) {
            int index = destinos.indexOf(destinoExistente.get());
            destinos.set(index, destinoAtualizado);
            return new ResponseEntity(destinoAtualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    // PATCH - Atualizar parcialmente um destino
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PatchMapping("/{id}")
    public ResponseEntity<DestinoViagem> atualizarParcialmenteDestino(@PathVariable Long id, @RequestBody DestinoViagem destinoAtualizacaoParcial) {
         Optional<DestinoViagem> destinoExistente = destinos.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();

        if (destinoExistente.isPresent()) {
            DestinoViagem destino = destinoExistente.get();
            if (destinoAtualizacaoParcial.getNome() != null) {
                destino.setNome(destinoAtualizacaoParcial.getNome());
            }
            if (destinoAtualizacaoParcial.getLocalizacao() != null) {
                destino.setLocalizacao(destinoAtualizacaoParcial.getLocalizacao());
            }
            // ... outros campos
            return new ResponseEntity(destino, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Excluir destino
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDestino(@PathVariable Long id) {
        boolean removido = destinos.removeIf(d -> d.getId().equals(id));
        return removido ? new ResponseEntity(HttpStatus.NO_CONTENT) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    // POST - Reservar pacote de viagem
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping("/{id}/reservar")
    public ResponseEntity<String> reservarPacote(@PathVariable Long id) {
        // Lógica para reservar um pacote para o destino com o ID fornecido
        // ... (integração com sistemas de reserva, etc.)
        return new ResponseEntity("Pacote reservado para o destino " + id, HttpStatus.OK);
    }
}

// Classe DestinoViagem
class DestinoViagem {
    private Long id;
    private String nome;
    private String localizacao;

    // Getters e Setters para todos os atributos

    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getLocalizacao() {return localizacao;}
    public void setLocalizacao(String localizacao) {this.localizacao = localizacao;}

}
