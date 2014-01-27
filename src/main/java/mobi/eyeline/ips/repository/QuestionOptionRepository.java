package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.QuestionOption;

public class QuestionOptionRepository extends BaseRepository<QuestionOption, Integer> {

    public QuestionOptionRepository(DB db) {
        super(db);
    }
}
