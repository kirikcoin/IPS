package mobi.eyeline.ips.util.cdi

import groovy.transform.CompileStatic

import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.ContextNotActiveException
import javax.enterprise.context.Dependent
import javax.enterprise.inject.Produces
import javax.faces.context.ExternalContext
import javax.faces.context.FacesContext

@SuppressWarnings("GroovyUnusedDeclaration")
@CompileStatic
@ApplicationScoped
class FacesProducer {

  @Produces
  @Dependent
  @SuppressWarnings(["GrMethodMayBeStatic", "GroovyUnusedDeclaration"])
  FacesContext getFacesContext() {
    final ctx = FacesContext.currentInstance
    if (!ctx) {
      throw new ContextNotActiveException('FacesContext is not available')
    }

    return ctx
  }

  @Produces
  @Dependent
  @SuppressWarnings(["GrMethodMayBeStatic", "GroovyUnusedDeclaration"])
  ExternalContext getExternalContext(FacesContext facesContext) {
    facesContext.externalContext
  }
}
