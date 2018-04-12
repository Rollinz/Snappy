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
@Table(name = "administrador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Administrador.findAll", query = "SELECT a FROM Administrador a")
    , @NamedQuery(name = "Administrador.findByIdadministrador", query = "SELECT a FROM Administrador a WHERE a.idadministrador = :idadministrador")
    , @NamedQuery(name = "Administrador.findByNombreadministrador", query = "SELECT a FROM Administrador a WHERE a.nombreadministrador = :nombreadministrador")
    , @NamedQuery(name = "Administrador.findByContrasenaadministrador", query = "SELECT a FROM Administrador a WHERE a.contrasenaadministrador = :contrasenaadministrador")})
public class Administrador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDADMINISTRADOR")
    private Integer idadministrador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBREADMINISTRADOR")
    private String nombreadministrador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CONTRASENAADMINISTRADOR")
    private String contrasenaadministrador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idadministradorFk")
    private List<Producto> productoList;

    public Administrador() {
    }

    public Administrador(Integer idadministrador) {
        this.idadministrador = idadministrador;
    }

    public Administrador(Integer idadministrador, String nombreadministrador, String contrasenaadministrador) {
        this.idadministrador = idadministrador;
        this.nombreadministrador = nombreadministrador;
        this.contrasenaadministrador = contrasenaadministrador;
    }

    public Integer getIdadministrador() {
        return idadministrador;
    }

    public void setIdadministrador(Integer idadministrador) {
        this.idadministrador = idadministrador;
    }

    public String getNombreadministrador() {
        return nombreadministrador;
    }

    public void setNombreadministrador(String nombreadministrador) {
        this.nombreadministrador = nombreadministrador;
    }

    public String getContrasenaadministrador() {
        return contrasenaadministrador;
    }

    public void setContrasenaadministrador(String contrasenaadministrador) {
        this.contrasenaadministrador = contrasenaadministrador;
    }

    @XmlTransient
    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idadministrador != null ? idadministrador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Administrador)) {
            return false;
        }
        Administrador other = (Administrador) object;
        if ((this.idadministrador == null && other.idadministrador != null) || (this.idadministrador != null && !this.idadministrador.equals(other.idadministrador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.rollinz.entities.Administrador[ idadministrador=" + idadministrador + " ]";
    }
    
}
