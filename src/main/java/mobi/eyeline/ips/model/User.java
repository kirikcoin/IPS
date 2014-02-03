package mobi.eyeline.ips.model;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Proxy(lazy = false)
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Хэш-код (SHA-256) пользовательского пароля.
     */
    @Column(name = "password", columnDefinition = "VARCHAR(255)", nullable = false)
    @NotNull
    private String password;

    /**
     * Логин пользователя для входа в систему.
     */
    @Column(name = "users_name", nullable = false)
    @NotEmpty
    private String login;

    /**
     * Полное имя пользователя.
     */
    @Column(name = "full_name", nullable = false)
    @NotEmpty(message = "{profile.edit.message.validationErrorFullName}")
    private String fullName;

    @Column(name = "email", nullable = false)
    @Email
    @NotEmpty(message = "{profile.edit.message.validationErrorEmail}")
    private String email;

    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(255)")
    @Type(type = "mobi.eyeline.ips.model.GenericEnumUserType", parameters = {
            @Parameter(name = "enumClass", value = "mobi.eyeline.ips.model.Role"),
            @Parameter(name = "identifierMethod", value = "getName"),
            @Parameter(name = "valueOfMethod", value = "fromName")
    })
    @NotNull
    private Role role;

    /**
     * Состояние валидности пользовательского аккаунта.
     * При выставлении вход в аккаунт невозможен.
     */
    @Column(name = "blocked", columnDefinition = "BIT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean blocked;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
