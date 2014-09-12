package mobi.eyeline.ips.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Table(name = "ui_profiles")
@Proxy(lazy = false)
@Cache(usage = READ_WRITE)
public class UiProfile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @NotNull
    @Column(name = "icon", columnDefinition = "BLOB")
    private byte[] icon;

    @Column(name = "skin")
    @NotNull
    private String skin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }
}
