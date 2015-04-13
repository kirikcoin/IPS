package mobi.eyeline.ips.web.validators

import org.hibernate.validator.internal.engine.path.PathImpl

import javax.validation.ConstraintViolation
import javax.validation.Path
import javax.validation.metadata.ConstraintDescriptor

class SimpleConstraintViolation<T> implements ConstraintViolation<T> {
  final Path propertyPath
  final String message

  final String messageTemplate = null
  final T rootBean = null
  final Class<T> rootBeanClass = null
  final Object leafBean = null
  final Object[] executableParameters = new Object[0]
  final Object executableReturnValue = null
  final Object invalidValue = null
  final ConstraintDescriptor constraintDescriptor = null

  @Override
  def <U> U unwrap(Class<U> type) { null }

  SimpleConstraintViolation(String propertyPath, String message) {
    this.propertyPath = PathImpl.createPathFromString(propertyPath)
    this.message = message
  }
}
