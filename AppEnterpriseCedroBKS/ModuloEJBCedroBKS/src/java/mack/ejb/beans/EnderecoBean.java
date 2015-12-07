package mack.ejb.beans;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import shared.entities.Endereco;

@Stateless
@LocalBean
public class EnderecoBean {

    @PersistenceContext(unitName = "DerbyPU")
    private EntityManager em;

    public void save(Endereco e) {
        em.persist(e);
    }
    
    public List<Endereco> list(String cep) {
        Query query = em.createQuery("FROM Endereco e where e.cep='" + cep + "'");
        List<Endereco> list = query.getResultList();
        return list;
    }
    
    public List<Endereco> verificarEndereco(String cep, String endereco) {
        Query query = em.createQuery("FROM Endereco e where e.cep='" + cep + "' and e.endereco='" + endereco +"'");
        List<Endereco> list = query.getResultList();
        return list;
    }
}
