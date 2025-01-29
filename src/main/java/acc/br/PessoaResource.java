package acc.br;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import java.util.List;

import org.jboss.logging.Logger;

import acc.br.model.Pessoa;

@Path("/pessoas")
public class PessoaResource {
	
	private static final Logger LOG = Logger.getLogger(PessoaResource.class);

    @GET
    public List<Pessoa> listarTodas() {
        return Pessoa.listAll();
    }

    @GET
    @Path("/{nome}")
    public Response buscarPorNome(@PathParam("nome") String nome) {
        Pessoa pessoas = Pessoa.findByName(nome);
        if (pessoas == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Pessoa não encontrada com o nome: " + nome)
                           .build();
        }
        return Response.status(Response.Status.OK)
                       .entity(pessoas)
                       .build();
    }
    
    @GET
    @Path("/idade")
    public Response buscarPessoasAcimaDeIdade(@QueryParam("idade") int idade) {
    	LOG.info("Mostrando as Lista de frutas");
        if (idade <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Idade deve ser um valor positivo")
                           .build();
        }
        
        List<Pessoa> pessoas = Pessoa.findOlderThan(idade);
        if (pessoas.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Nenhuma pessoa encontrada com idade superior a " + idade)
                           .build();
        }
        return Response.status(Response.Status.OK)
                       .entity(pessoas)
                       .build();
    }

    @POST
    @Transactional
    public Response criarPessoa(Pessoa pessoa) {
        pessoa.persist();
        return Response.status(Response.Status.CREATED).entity(pessoa).build();
    }
}

