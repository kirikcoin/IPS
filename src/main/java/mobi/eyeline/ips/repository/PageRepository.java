package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.Page;

public class PageRepository extends BaseRepository<Page, Integer> {

  public PageRepository(DB db) {
    super(db);
  }
}
