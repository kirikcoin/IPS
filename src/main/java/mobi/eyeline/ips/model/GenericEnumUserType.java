package mobi.eyeline.ips.model;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.EnumType;
import org.hibernate.type.TypeResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Custom enum mapping.
 *
 * @see <a href="https://community.jboss.org/wiki/Java5EnumUserType">Original implementation</a>
 */
public class GenericEnumUserType extends EnumType {

  private Class<? extends Enum> enumClass;
  private Method identifierMethod;
  private Method valueOfMethod;
  private static final String defaultIdentifierMethodName = "name";
  private static final String defaultValueOfMethodName = "valueOf";
  private AbstractSingleColumnStandardBasicType type;
  private int sqlType;

  @Override
  public void setParameterValues(Properties parameters) {
    final String enumClassName = parameters.getProperty("enumClass");
    try {
      enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
    } catch (ClassNotFoundException e) {
      throw new HibernateException(e);
    }

    final String identifierMethodName =
        parameters.getProperty("identifierMethod", defaultIdentifierMethodName);

    Class<?> identifierType;
    try {
      identifierMethod = enumClass.getMethod(identifierMethodName);
      identifierType = identifierMethod.getReturnType();

    } catch (NoSuchMethodException | SecurityException e) {
      throw new HibernateException(e);
    }

    final TypeResolver tr = new TypeResolver();
    type = (AbstractSingleColumnStandardBasicType) tr.basic(identifierType.getName());
    if (type == null) {
      throw new HibernateException("Unsupported identifier type " + identifierType.getName());
    }
    sqlType = type.sqlType();

    final String valueOfMethodName =
        parameters.getProperty("valueOfMethod", defaultValueOfMethodName);

    valueOfMethod =
        MethodUtils.getMatchingAccessibleMethod(enumClass, valueOfMethodName, identifierType);
    if (valueOfMethod == null) {
      throw new HibernateException("No valueOf method found: class = " + enumClass +
          ", method = " + valueOfMethodName + ", parameter type = " + identifierType);
    }
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SessionImplementor session,
                            Object owner) throws SQLException {

    final Object identifier = type.get(rs, names[0], session);
    try {
      return valueOfMethod.invoke(enumClass, identifier);

    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new HibernateException(e);
    }
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SessionImplementor session) throws HibernateException {
    try {
      final Object identifier =
          (value != null) ? identifierMethod.invoke(value) : null;
      st.setObject(index, identifier, sqlType);

    } catch (IllegalAccessException |
        IllegalArgumentException |
        InvocationTargetException |
        SQLException e) {
      throw new HibernateException(e);
    }
  }

  @Override
  public Class<? extends Enum> returnedClass() {
    return enumClass;
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{sqlType};
  }
}
