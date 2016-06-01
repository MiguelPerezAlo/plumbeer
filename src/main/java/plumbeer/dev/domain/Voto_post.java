package plumbeer.dev.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Voto_post.
 */
@Entity
@Table(name = "voto_post")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Voto_post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "positivo")
    private Boolean positivo;
    
    @Lob
    @Column(name = "motivo")
    private String motivo;
    
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "votante_id")
    private User votante;

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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getVotante() {
        return votante;
    }

    public void setVotante(User user) {
        this.votante = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Voto_post voto_post = (Voto_post) o;
        if(voto_post.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, voto_post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Voto_post{" +
            "id=" + id +
            ", positivo='" + positivo + "'" +
            ", motivo='" + motivo + "'" +
            '}';
    }
}
