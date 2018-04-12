/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rollinz.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rolan
 */
@Entity
@Table(name = "pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p")
    , @NamedQuery(name = "Pedido.findByIdpedido", query = "SELECT p FROM Pedido p WHERE p.idpedido = :idpedido")
    , @NamedQuery(name = "Pedido.findByTotalpedido", query = "SELECT p FROM Pedido p WHERE p.totalpedido = :totalpedido")})
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDPEDIDO")
    private Integer idpedido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTALPEDIDO")
    private int totalpedido;
    @JoinColumn(name = "IDPRODCUTO_FK", referencedColumnName = "IDPRODUCTO")
    @ManyToOne(optional = false)
    private Producto idprodcutoFk;
    @JoinColumn(name = "IDMESA_FK", referencedColumnName = "IDMESA")
    @ManyToOne(optional = false)
    private Mesa idmesaFk;

    public Pedido() {
    }

    public Pedido(Integer idpedido) {
        this.idpedido = idpedido;
    }

    public Pedido(Integer idpedido, int totalpedido) {
        this.idpedido = idpedido;
        this.totalpedido = totalpedido;
    }

    public Integer getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(Integer idpedido) {
        this.idpedido = idpedido;
    }

    public int getTotalpedido() {
        return totalpedido;
    }

    public void setTotalpedido(int totalpedido) {
        this.totalpedido = totalpedido;
    }

    public Producto getIdprodcutoFk() {
        return idprodcutoFk;
    }

    public void setIdprodcutoFk(Producto idprodcutoFk) {
        this.idprodcutoFk = idprodcutoFk;
    }

    public Mesa getIdmesaFk() {
        return idmesaFk;
    }

    public void setIdmesaFk(Mesa idmesaFk) {
        this.idmesaFk = idmesaFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpedido != null ? idpedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.idpedido == null && other.idpedido != null) || (this.idpedido != null && !this.idpedido.equals(other.idpedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.rollinz.entities.Pedido[ idpedido=" + idpedido + " ]";
    }
    
}
