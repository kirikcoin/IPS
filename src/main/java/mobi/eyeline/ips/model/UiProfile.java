package mobi.eyeline.ips.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "skin")
    private Skin skin;

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

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public static enum Skin {
        ARAKS,
        MOBAK
    }
}
