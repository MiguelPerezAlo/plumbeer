package plumbeer.dev.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Votacion.
 */
@Entity
@Table(name = "votacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Votacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "positivo")
    private Boolean positivo;
    
    @Lob
    @Column(name = "motivo")
    private String motivo;
    
    @ManyToOne
    @JoinColumn(name = "votante_id")
    private User votante;

    @ManyToOne
    @JoinColumn(name = "votado_id")
    private User votado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPositivo() {
        return positivo;
    }
    
    public void setPositivo(Boolean positivo) {
        this.positivo = positivo;
    }

    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public User getVotante() {
        return votante;
    }

    public void setVotante(User user) {
        this.votante = user;
    }

    public User getVotado() {
        return votado;
    }

    public void setVotado(User user) {
        this.votado = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Votacion votacion = (Votacion) o;
        if(votacion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, votacion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Votacion{" +
            "id=" + id +
            ", positivo='" + positivo + "'" +
            ", motivo='" + motivo + "'" +
            '}';
    }
}
