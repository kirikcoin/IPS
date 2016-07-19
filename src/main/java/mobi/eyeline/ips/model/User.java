package mobi.eyeline.ips.model;

import mobi.eyeline.ips.validation.MaxSize;
import mobi.eyeline.ips.web.validators.LoginPasswordValidator;
import mobi.eyeline.ips.web.validators.PhoneValidator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Table(name = "users")
@Proxy(lazy = false)
@Cache(usage = READ_WRITE)
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
  @MaxSize(70)
  private String login;

  /**
   * Полное имя пользователя.
   */
  @Column(name = "full_name", nullable = false)
  @NotEmpty(message = "{profile.edit.message.validationErrorFullNameEmpty}")
  @Pattern(regexp = "^[^'%\"\\\\]*$", message = "{client.validation.fullname}")
  @MaxSize(70)
  private String fullName;

  @Column(name = "email", nullable = false)
  @Email(message = "{profile.edit.message.validationErrorEmail}")
  @NotEmpty(message = "{profile.edit.message.validationErrorEmailEmpty}")
  @MaxSize(70)
  private String email;

  /**
   * Компания (юридическое лицо и т.п.). Опционально.
   */
  @Column(name = "company")
  @MaxSize(70)
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
  @MaxSize(30)
  private String phoneNumber;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "locale")
  private Locale locale = Locale.EN;

  /**
   * Актуально только для роли "Менеджер".
   * При выставлении данного флага становятся доступны только опросы,
   * созданные данным пользователем.
   *
   * @see Survey#owner
   */
  @Column(name = "only_own_surveys", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean onlyOwnSurveysVisible;

  @OneToMany(mappedBy = "owner")
  private List<Survey> createdSurveys;

  @Column(name = "allow_invitations", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean canSendInvitations = false;

  @Column(name = "show_c2s", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean showC2s = false;

  @Column(name = "show_all_clients", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean showAllClients = false;

  @Column(name = "esdp_provider")
  private String esdpProvider;

  @Column(name = "esdp_login")
  private String esdpLogin;

  @Column(name = "esdp_password")
  private String esdpPasswordHash;

  @Column(name = "time_zone_id")
  private String timeZoneId = "Europe/Moscow";

  @OneToOne(cascade = ALL)
  @JoinColumn(name = "ui_profile_id")
  @Cache(usage = READ_WRITE)
  private UiProfile uiProfile;

  @JoinColumn(name = "manager_id")
  @ManyToOne
  @Cache(usage = READ_WRITE)
  private User manager;

  @Column(name = "allow_c2s", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean allowC2s = true;

  @Column(name = "allow_overall_stats", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean allowOverallStats = true;

  @Column(name = "allow_telegram", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean allowTelegram = true;

  @Column(name = "allow_preview", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean allowPreview = true;

  @Column(name = "allow_profile_change", columnDefinition = "BIT", nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean allowProfileChange = true;

  public User() {
  }

  public UiProfile getUiProfile() {
    return uiProfile;
  }

  public void setUiProfile(UiProfile uiProfile) {
    this.uiProfile = uiProfile;
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

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public boolean isOnlyOwnSurveysVisible() {
    return onlyOwnSurveysVisible;
  }

  public void setOnlyOwnSurveysVisible(boolean onlyOwnSurveysVisible) {
    this.onlyOwnSurveysVisible = onlyOwnSurveysVisible;
  }

  public List<Survey> getCreatedSurveys() {
    return createdSurveys;
  }

  public void setCreatedSurveys(List<Survey> createdSurveys) {
    this.createdSurveys = createdSurveys;
  }

  public boolean isCanSendInvitations() {
    return canSendInvitations;
  }

  public void setCanSendInvitations(boolean canSendInvitations) {
    this.canSendInvitations = canSendInvitations;
  }

  public boolean isShowC2s() {
    return showC2s;
  }

  public void setShowC2s(boolean showC2s) {
    this.showC2s = showC2s;
  }

  public boolean isShowAllClients() {
    return showAllClients;
  }

  public void setShowAllClients(boolean showAllClients) {
    this.showAllClients = showAllClients;
  }

  public String getEsdpProvider() {
    return esdpProvider;
  }

  public void setEsdpProvider(String esdpProvider) {
    this.esdpProvider = esdpProvider;
  }

  public String getEsdpLogin() {
    return esdpLogin;
  }

  public void setEsdpLogin(String esdpLogin) {
    this.esdpLogin = esdpLogin;
  }

  public String getEsdpPasswordHash() {
    return esdpPasswordHash;
  }

  public void setEsdpPasswordHash(String esdpPasswordHash) {
    this.esdpPasswordHash = esdpPasswordHash;
  }

  public String getTimeZoneId() {
    return timeZoneId;
  }

  public void setTimeZoneId(String timeZoneId) {
    this.timeZoneId = timeZoneId;
  }

  public User getManager() {
    return manager;
  }

  public void setManager(User manager) {
    this.manager = manager;
  }

  public boolean isAllowC2s() {
    return allowC2s;
  }

  public void setAllowC2s(boolean allowC2s) {
    this.allowC2s = allowC2s;
  }

  public boolean isAllowOverallStats() {
    return allowOverallStats;
  }

  public void setAllowOverallStats(boolean allowOverallStats) {
    this.allowOverallStats = allowOverallStats;
  }

  public boolean isAllowTelegram() {
    return allowTelegram;
  }

  public void setAllowTelegram(boolean allowTelegram) {
    this.allowTelegram = allowTelegram;
  }

  public boolean isAllowPreview() {
    return allowPreview;
  }

  public void setAllowPreview(boolean allowPreview) {
    this.allowPreview = allowPreview;
  }

  public boolean isAllowProfileChange() {
    return allowProfileChange;
  }

  public void setAllowProfileChange(boolean allowProfileChange) {
    this.allowProfileChange = allowProfileChange;
  }

  @AssertTrue(message = "uiProfile set for non-manager role")
  private boolean isValidUiProfile() {
    return (role == Role.MANAGER) ? (uiProfile != null) : (uiProfile == null);
  }

  @AssertTrue(message = "Manager must have 'manager' field unset")
  private boolean isValidManagerLink() {
    return role == Role.MANAGER ? manager == null : manager != null;
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

  public String toTraceString() {
    return "User{" +
        "id=" + id +
        ", password='" + password + '\'' +
        ", login='" + login + '\'' +
        ", fullName='" + fullName + '\'' +
        ", email='" + email + '\'' +
        ", company='" + company + '\'' +
        ", role=" + role +
        ", blocked=" + blocked +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", locale=" + locale +
        ", onlyOwnSurveysVisible=" + onlyOwnSurveysVisible +
        '}';
  }
}
