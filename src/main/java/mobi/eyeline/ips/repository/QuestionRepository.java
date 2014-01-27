package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.Question;

public class QuestionRepository extends BaseRepository<Question, Integer> {

    public QuestionRepository(DB db) {
        super(db);
    }
}
