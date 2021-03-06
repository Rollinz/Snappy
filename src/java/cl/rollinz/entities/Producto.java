/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rollinz.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rolan
 */
@Entity
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p")
    , @NamedQuery(name = "Producto.findByIdproducto", query = "SELECT p FROM Producto p WHERE p.idproducto = :idproducto")
    , @NamedQuery(name = "Producto.findByNombreproducto", query = "SELECT p FROM Producto p WHERE p.nombreproducto = :nombreproducto")
    , @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDPRODUCTO")
    private Integer idproducto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBREPRODUCTO")
    private String nombreproducto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRECIO")
    private int precio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idprodcutoFk")
    private List<Pedido> pedidoList;
    @JoinColumn(name = "IDCATEGORIA_FK", referencedColumnName = "IDCATEGORIA")
    @ManyToOne(optional = false)
    private Categoria idcategoriaFk;
    @JoinColumn(name = "IDADMINISTRADOR_FK", referencedColumnName = "IDADMINISTRADOR")
    @ManyToOne(optional = false)
    private Administrador idadministradorFk;

    public Producto() {
    }

    public Producto(Integer idproducto) {
        this.idproducto = idproducto;
    }

    public Producto(String nombreproducto, int precio) {
        this.nombreproducto = nombreproducto;
        this.precio = precio;
    }

    public Producto(Integer idproducto, String nombreproducto, int precio) {
        this.idproducto = idproducto;
        this.nombreproducto = nombreproducto;
        this.precio = precio;
    }

    public Integer getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Integer idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @XmlTransient
    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

    public Categoria getIdcategoriaFk() {
        return idcategoriaFk;
    }

    public void setIdcategoriaFk(Categoria idcategoriaFk) {
        this.idcategoriaFk = idcategoriaFk;
    }

    public Administrador getIdadministradorFk() {
        return idadministradorFk;
    }

    public void setIdadministradorFk(Administrador idadministradorFk) {
        this.idadministradorFk = idadministradorFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idproducto != null ? idproducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.idproducto == null && other.idproducto != null) || (this.idproducto != null && !this.idproducto.equals(other.idproducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.rollinz.entities.Producto[ idproducto=" + idproducto + " ]";
    }
    
}
