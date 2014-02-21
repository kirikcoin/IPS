package mobi.eyeline.ips.model;

import mobi.eyeline.ips.web.validators.LoginPasswordValidator;
import mobi.eyeline.ips.web.validators.PhoneValidator;
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
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Proxy(lazy = false)
public class User implements Serializable {

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
    @NotEmpty(message = "{profile.edit.message.validationErrorLoginEmpty}")
    @Pattern(regexp = LoginPasswordValidator.LOGIN_PASSWORD_REGEXP,
            message = "{client.dialog.validation.loginerror}")
    private String login;

    /**
     * Полное имя пользователя.
     */
    @Column(name = "full_name", nullable = false)
    @NotEmpty(message = "{profile.edit.message.validationErrorFullNameEmpty}")
    private String fullName;

    @Column(name = "email", nullable = false)
    @Email(message = "{profile.edit.message.validationErrorEmail}" )
    @NotEmpty(message = "{profile.edit.message.validationErrorEmailEmpty}" )
    private String email;

    /**
     * Компания (юридическое лицо и т.п.). Опционально.
     */
    @Column(name = "company")
    private String company;

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

    @Column(name = "phone_number")
    @Pattern(regexp = PhoneValidator.PHONE_REGEXP,
            message = "{invalid.phone.number}")
    private String phoneNumber;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return !(id != null ? !id.equals(user.id) : user.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
