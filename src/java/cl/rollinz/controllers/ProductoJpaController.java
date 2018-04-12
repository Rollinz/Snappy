/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rollinz.controllers;

import cl.rollinz.controllers.exceptions.IllegalOrphanException;
import cl.rollinz.controllers.exceptions.NonexistentEntityException;
import cl.rollinz.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cl.rollinz.entities.Categoria;
import cl.rollinz.entities.Administrador;
import cl.rollinz.entities.Pedido;
import cl.rollinz.entities.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author rolan
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws RollbackFailureException, Exception {
        if (producto.getPedidoList() == null) {
            producto.setPedidoList(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categoria idcategoriaFk = producto.getIdcategoriaFk();
            if (idcategoriaFk != null) {
                idcategoriaFk = em.getReference(idcategoriaFk.getClass(), idcategoriaFk.getIdcategoria());
                producto.setIdcategoriaFk(idcategoriaFk);
            }
            Administrador idadministradorFk = producto.getIdadministradorFk();
            if (idadministradorFk != null) {
                idadministradorFk = em.getReference(idadministradorFk.getClass(), idadministradorFk.getIdadministrador());
                producto.setIdadministradorFk(idadministradorFk);
            }
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : producto.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getIdpedido());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            producto.setPedidoList(attachedPedidoList);
            em.persist(producto);
            if (idcategoriaFk != null) {
                idcategoriaFk.getProductoList().add(producto);
                idcategoriaFk = em.merge(idcategoriaFk);
            }
            if (idadministradorFk != null) {
                idadministradorFk.getProductoList().add(producto);
                idadministradorFk = em.merge(idadministradorFk);
            }
            for (Pedido pedidoListPedido : producto.getPedidoList()) {
                Producto oldIdprodcutoFkOfPedidoListPedido = pedidoListPedido.getIdprodcutoFk();
                pedidoListPedido.setIdprodcutoFk(producto);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldIdprodcutoFkOfPedidoListPedido != null) {
                    oldIdprodcutoFkOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldIdprodcutoFkOfPedidoListPedido = em.merge(oldIdprodcutoFkOfPedidoListPedido);
                }
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

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto persistentProducto = em.find(Producto.class, producto.getIdproducto());
            Categoria idcategoriaFkOld = persistentProducto.getIdcategoriaFk();
            Categoria idcategoriaFkNew = producto.getIdcategoriaFk();
            Administrador idadministradorFkOld = persistentProducto.getIdadministradorFk();
            Administrador idadministradorFkNew = producto.getIdadministradorFk();
            List<Pedido> pedidoListOld = persistentProducto.getPedidoList();
            List<Pedido> pedidoListNew = producto.getPedidoList();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoListOldPedido + " since its idprodcutoFk field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idcategoriaFkNew != null) {
                idcategoriaFkNew = em.getReference(idcategoriaFkNew.getClass(), idcategoriaFkNew.getIdcategoria());
                producto.setIdcategoriaFk(idcategoriaFkNew);
            }
            if (idadministradorFkNew != null) {
                idadministradorFkNew = em.getReference(idadministradorFkNew.getClass(), idadministradorFkNew.getIdadministrador());
                producto.setIdadministradorFk(idadministradorFkNew);
            }
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getIdpedido());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            producto.setPedidoList(pedidoListNew);
            producto = em.merge(producto);
            if (idcategoriaFkOld != null && !idcategoriaFkOld.equals(idcategoriaFkNew)) {
                idcategoriaFkOld.getProductoList().remove(producto);
                idcategoriaFkOld = em.merge(idcategoriaFkOld);
            }
            if (idcategoriaFkNew != null && !idcategoriaFkNew.equals(idcategoriaFkOld)) {
                idcategoriaFkNew.getProductoList().add(producto);
                idcategoriaFkNew = em.merge(idcategoriaFkNew);
            }
            if (idadministradorFkOld != null && !idadministradorFkOld.equals(idadministradorFkNew)) {
                idadministradorFkOld.getProductoList().remove(producto);
                idadministradorFkOld = em.merge(idadministradorFkOld);
            }
            if (idadministradorFkNew != null && !idadministradorFkNew.equals(idadministradorFkOld)) {
                idadministradorFkNew.getProductoList().add(producto);
                idadministradorFkNew = em.merge(idadministradorFkNew);
            }
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Producto oldIdprodcutoFkOfPedidoListNewPedido = pedidoListNewPedido.getIdprodcutoFk();
                    pedidoListNewPedido.setIdprodcutoFk(producto);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldIdprodcutoFkOfPedidoListNewPedido != null && !oldIdprodcutoFkOfPedidoListNewPedido.equals(producto)) {
                        oldIdprodcutoFkOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldIdprodcutoFkOfPedidoListNewPedido = em.merge(oldIdprodcutoFkOfPedidoListNewPedido);
                    }
                }
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
                Integer id = producto.getIdproducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdproducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pedido> pedidoListOrphanCheck = producto.getPedidoList();
            for (Pedido pedidoListOrphanCheckPedido : pedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Pedido " + pedidoListOrphanCheckPedido + " in its pedidoList field has a non-nullable idprodcutoFk field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idcategoriaFk = producto.getIdcategoriaFk();
            if (idcategoriaFk != null) {
                idcategoriaFk.getProductoList().remove(producto);
                idcategoriaFk = em.merge(idcategoriaFk);
            }
            Administrador idadministradorFk = producto.getIdadministradorFk();
            if (idadministradorFk != null) {
                idadministradorFk.getProductoList().remove(producto);
                idadministradorFk = em.merge(idadministradorFk);
            }
            em.remove(producto);
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

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
