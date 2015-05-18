package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.AccessNumber;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.repository.AccessNumberRepository;

import java.util.ArrayList;
import java.util.Collection;

public class AccessNumbersService {

  private final AccessNumberRepository accessNumberRepository;

  public AccessNumbersService(AccessNumberRepository accessNumberRepository) {
    this.accessNumberRepository = accessNumberRepository;
  }

  public void tryUpdateNumbers(Survey survey, Collection<Integer> accessNumberIds) {
    final Collection<AccessNumber> accessNumbers = new ArrayList<>(accessNumberIds.size());
    for (Integer id : accessNumberIds) {
      if (id > 0) {
        accessNumbers.add(accessNumberRepository.get(id));
      }
    }

    // TODO: load entities, try to perform ESDP update, then persist.
  }

}
