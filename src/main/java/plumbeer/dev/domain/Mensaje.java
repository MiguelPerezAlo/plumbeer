package plumbeer.dev.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Mensaje.
 */
@Entity
@Table(name = "mensaje")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mensaje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "asunto")
    private String asunto;
    
    @Lob
    @Column(name = "cuerpo")
    private String cuerpo;
    
    @Column(name = "fecha")
    private ZonedDateTime fecha;
    
    @Column(name = "leido")
    private Boolean leido;
    
    @ManyToOne
    @JoinColumn(name = "emisor_id")
    private User emisor;

    @ManyToOne
    @JoinColumn(name = "receptor_id")
    private User receptor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsunto() {
        return asunto;
    }
    
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }
    
    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public Boolean getLeido() {
        return leido;
    }
    
    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public User getEmisor() {
        return emisor;
    }

    public void setEmisor(User user) {
        this.emisor = user;
    }

    public User getReceptor() {
        return receptor;
    }

    public void setReceptor(User user) {
        this.receptor = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mensaje mensaje = (Mensaje) o;
        if(mensaje.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mensaje.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mensaje{" +
            "id=" + id +
            ", asunto='" + asunto + "'" +
            ", cuerpo='" + cuerpo + "'" +
            ", fecha='" + fecha + "'" +
            ", leido='" + leido + "'" +
            '}';
    }
}
