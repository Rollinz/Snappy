/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rollinz.controllers;

import cl.rollinz.controllers.exceptions.NonexistentEntityException;
import cl.rollinz.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cl.rollinz.entities.Producto;
import cl.rollinz.entities.Mesa;
import cl.rollinz.entities.Pedido;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author rolan
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto idprodcutoFk = pedido.getIdprodcutoFk();
            if (idprodcutoFk != null) {
                idprodcutoFk = em.getReference(idprodcutoFk.getClass(), idprodcutoFk.getIdproducto());
                pedido.setIdprodcutoFk(idprodcutoFk);
            }
            Mesa idmesaFk = pedido.getIdmesaFk();
            if (idmesaFk != null) {
                idmesaFk = em.getReference(idmesaFk.getClass(), idmesaFk.getIdmesa());
                pedido.setIdmesaFk(idmesaFk);
            }
            em.persist(pedido);
            if (idprodcutoFk != null) {
                idprodcutoFk.getPedidoList().add(pedido);
                idprodcutoFk = em.merge(idprodcutoFk);
            }
            if (idmesaFk != null) {
                idmesaFk.getPedidoList().add(pedido);
                idmesaFk = em.merge(idmesaFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getIdpedido());
            Producto idprodcutoFkOld = persistentPedido.getIdprodcutoFk();
            Producto idprodcutoFkNew = pedido.getIdprodcutoFk();
            Mesa idmesaFkOld = persistentPedido.getIdmesaFk();
            Mesa idmesaFkNew = pedido.getIdmesaFk();
            if (idprodcutoFkNew != null) {
                idprodcutoFkNew = em.getReference(idprodcutoFkNew.getClass(), idprodcutoFkNew.getIdproducto());
                pedido.setIdprodcutoFk(idprodcutoFkNew);
            }
            if (idmesaFkNew != null) {
                idmesaFkNew = em.getReference(idmesaFkNew.getClass(), idmesaFkNew.getIdmesa());
                pedido.setIdmesaFk(idmesaFkNew);
            }
            pedido = em.merge(pedido);
            if (idprodcutoFkOld != null && !idprodcutoFkOld.equals(idprodcutoFkNew)) {
                idprodcutoFkOld.getPedidoList().remove(pedido);
                idprodcutoFkOld = em.merge(idprodcutoFkOld);
            }
            if (idprodcutoFkNew != null && !idprodcutoFkNew.equals(idprodcutoFkOld)) {
                idprodcutoFkNew.getPedidoList().add(pedido);
                idprodcutoFkNew = em.merge(idprodcutoFkNew);
            }
            if (idmesaFkOld != null && !idmesaFkOld.equals(idmesaFkNew)) {
                idmesaFkOld.getPedidoList().remove(pedido);
                idmesaFkOld = em.merge(idmesaFkOld);
            }
            if (idmesaFkNew != null && !idmesaFkNew.equals(idmesaFkOld)) {
                idmesaFkNew.getPedidoList().add(pedido);
                idmesaFkNew = em.merge(idmesaFkNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedido.getIdpedido();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getIdpedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            Producto idprodcutoFk = pedido.getIdprodcutoFk();
            if (idprodcutoFk != null) {
                idprodcutoFk.getPedidoList().remove(pedido);
                idprodcutoFk = em.merge(idprodcutoFk);
            }
            Mesa idmesaFk = pedido.getIdmesaFk();
            if (idmesaFk != null) {
                idmesaFk.getPedidoList().remove(pedido);
                idmesaFk = em.merge(idmesaFk);
            }
            em.remove(pedido);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pedido findPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
