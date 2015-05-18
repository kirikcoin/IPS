package mobi.eyeline.ips.service

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.AccessNumberRepository

@CompileStatic
class AccessNumbersService {

  private final AccessNumberRepository accessNumberRepository
  private final EsdpService esdpService

  AccessNumbersService(AccessNumberRepository accessNumberRepository,
                       EsdpService esdpService) {
    this.accessNumberRepository = accessNumberRepository
    this.esdpService = esdpService
  }

  void tryUpdateNumbers(User user,
                        Survey survey,
                        final Collection<Integer> accessNumberIds) {

    final accessNumbers = accessNumberIds
        .findAll { it > 0 }
        .collect { accessNumberRepository.get(it) }

    final handleRemoved = {
      accessNumberRepository.list(survey)
          .findAll { num -> !(num.id in accessNumberIds) }
          .each { num ->
        num.surveyStats = null
        accessNumberRepository.update num
      }
    }

    final handleAdded = {
      accessNumberIds
          .findAll { id -> !(id in accessNumberRepository.list(survey).collect { it.id }) }
          .each { id ->
        final num = accessNumberRepository.load(id)
        num.surveyStats = survey.statistics
        accessNumberRepository.update(num)
      }
    }

    esdpService.update(user, survey, accessNumbers)

    // Won't be called if ESDP call fails.
    handleRemoved()
    handleAdded()
  }
}
