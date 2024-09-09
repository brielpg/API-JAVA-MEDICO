package br.com.api.controller;

import br.com.api.dto.DadosAtualizacaoMedico;
import br.com.api.dto.DadosCadastroMedico;
import br.com.api.dto.DadosListagemMedico;
import br.com.api.models.Medico;
import br.com.api.repositories.MedicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    // A anotacao Autowired serve para dizer ao Spring que ele conhece esta interface "MedicoRepository", e é para ele
    // mesmo fazer esta conexao
    @Autowired
    private MedicoRepository medicoRepository;


    // 1- Basicamente a anotacao @Transcational serve para dizer ao Spring que quando este método for chamado, haverá
    // uma transacao de informacoes e ele (Spring) precisará fazer algo no banco de dados (ex: salvar um médico).
    // 2- Recebemos como parametro DadosCadastroMedico, ele é um DTO que serve para dizer quais serao as informacoes
    // serao trazidas e serao necessarias para criar um médico, em sequencia temos que utilizar as anotacoes @Valid e
    // @RequestBody, Valid serve para dizer ao Spring que o DTO somente será aceito se ele passar nas validacoes que
    // fizemos lá na classe do DTO (DadosCadastroMedico), já o RequestBody serve para dizer ao Spring que as informacoes
    // que iremos utilizar está vindo do corpo da requisicao (JSON).
    // 3- Chamamos a interface que criamos "medicoRepository" e utilizamos o método .save() para salvar este médico
    // no banco de dados, aqui eu passei como parametro um construtor de médico pois, estamos recebendo como parâmetro
    // no método "cadastrarMedico" uma classe chamada "DadosCadastroMedico", e como queremos salvar a JPA "Medico"
    // precisamos criar um novo Medico com esses dados, já que ambos nao sao a mesma coisa, apesar de terem os mesmos
    // dados. Além disto a interface do repositório é referente a classe "Medico" nao a "DadosAtualizacaoMedico".
    @PostMapping
    @Transactional
    public void cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados){
        medicoRepository.save(new Medico(dados));
    }

    // 1- Neste método passamos como parâmetro a interface "Pageable" que é uma interface de paginacao do Spring, com ela
    // dizemos ao Spring que esse nosso método pode utilizar alguns parametros na URL para o usuario configurar oque
    // deseja ver. A anotacao @PageableDefault serve para definirmos alguma informacao do pageamento como Default.
    // Ainda no método precisamos utilizar o retorno Page<DadosListagemMedico> pois a partir do momento que decidimos
    // utilizar uma "Pageable" teremos que retornar uma Page com os dados do DTO DadosListagemMedico. Poderiamos no
    // lugar da Pageable retornar uma List<DadosListagemMedico> e nao passar o parametro "Pageable". O método
    // findAllByAtivoTrue() precisa do argumento da paginacao utilizado pelo método, e este método diz para o Spring
    // retornar TODOS os MEDICOS do banco de dados que estao ATIVOS (ativo = true) com as informacoes do DTO.
    // 2- O método .stream() serve para passar por cada dado de uma lista, assim como o for/foreach, já o método .map()
    // é um método exclusivo do .stream() e ele (map) serve para transformar um objeto em outro (ex: Transformar um
    // Medico(JPA) em um DadosListagemMedico(DTO)), esta transformacao geralmente é necessária pois você nao deseja
    // retornar por exemplo todas as informacoes de um objeto, se retornassemos um Medico aqui, ele traria todas as
    // informacoes do médico, e de acordo com as regras de negócio deste projeto em específico o método listarMedico()
    // deveria retornar somente alguns dados do "Medico", por isso temos que transformar a JPA "Medico" em um DTO
    // "DadosListagemMedico". Neste caso nao é necessário passar o método .stream() pois este método já está "dentro"
    // do .findAllByAtivoTrue(), é como se ele já existisse aqui, só nao precisamos escrever. o "::new" serve para
    // utilizarmos o construtor de "DadosListagemMedico".
    @GetMapping
    public Page<DadosListagemMedico> listarMedico(@PageableDefault(sort = {"nome"}, size = 10, page = 0) Pageable paginacao){
        return medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }

    // Funciona de maneira parecida com o método criado acima "cadastrarMedico()" mas neste caso no body da requisicao
    // o usuario irá passar o "id" do médico que deseja alterar as informacoes, e nós iremos salvar os dados deste médico
    // em uma variável "var medico", para saber de qual médico você está se referindo o Spring disponibiliza um método
    // chamado .getReferenceById(id aqui dentro), que dentro do parametro recebe o "id" do médico que deseja alterar.
    // Sabendo quem é o médico ao qual você se refere você só vai precisar de um método dentro da classe "Medico" que
    // serve para atualizar as informacoes passadas para ele atraves do DTO.
    @PutMapping
    @Transactional
    public void atualizarMedico(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = medicoRepository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }

    // Funciona de maneira quase idêntica ao método acima "atualizarMedico" a única coisa que muda é a maneira que nós
    // recebemos o parâmetro que aqui já nao é mais pelo body da requisicao e sim pela URL /medicos/"id". Na anotacao
    // @DeleteMapping() temos que passar como parametro um "/{id}" para dizer ao Spring que aquele método receberá
    // algo escrito na sua URL, já no método receberemos como parâmetro o "id" com seu respectivo tipo, neste caso, Long,
    // mas só com isso o Spring nao consegue entender que esses dois parametros sao a mesma coisa, pois poderiamos ter
    // mais de 1 parametro, e como o Spring faria para decifrar quem é quem?, para isso utilizamos a anotacao @PathVariable
    // que serve para fazer esta conexao para o Spring e dizer que o "id" passado no método é o mesmo valor passado na anotacao.
    @DeleteMapping("/{id}")
    @Transactional
    public void desativarMedico(@PathVariable Long id){
        var medico = medicoRepository.getReferenceById(id);
        medico.desativarMedico();
    }
}
