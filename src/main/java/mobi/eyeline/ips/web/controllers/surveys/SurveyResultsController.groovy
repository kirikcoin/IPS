package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Answer
import mobi.eyeline.ips.model.SurveySession
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.faces.context.FacesContext

@CompileStatic
class SurveyResultsController extends BaseSurveyController {
    private static final Logger logger = LoggerFactory.getLogger(SurveyResultsController)

    private final AnswerRepository answerRepository = Services.instance().answerRepository
    private final RespondentRepository respondentRepository = Services.instance().respondentRepository

    Date periodStart = survey.startDate
    Date periodEnd = survey.endDate
    String filter

    DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            List getRows(int startPos,
                         int count,
                         DataTableSortOrder sortOrder) {


                return answerRepository.list(
                        getSurvey(),
                        periodStart,
                        periodEnd,
                        filter,
                        sortOrder.columnId,
                        sortOrder.asc,
                        count,
                        startPos)
            }

            @Override
            int getRowsCount() {
                return answerRepository.count(
                        getSurvey(),
                        periodStart,
                        periodEnd,
                        filter)
            }
        }
    }


    void download(FacesContext context, OutputStream os) {

        List<SurveySession> listForDownload = answerRepository.list(
                getSurvey(),
                periodStart,
                periodEnd,
                filter)

        PrintWriter writer = null;
        String text;
        try{
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, "UTF-8")));
            text = strings['results.list.csv.msisdn'] +
                    ";" + strings['results.list.csv.question.number'] +
                    ";\"" + strings['results.list.csv.question.text']  + "\""+
                    ";" + strings['results.list.csv.questionoption.number']  +
                    ";\"" + strings['results.list.csv.questionoption.text']  + "\""+
                    ";" + strings['results.list.csv.date']
            writer.println(text);

            for(SurveySession item:listForDownload) {
                for(Answer answer:item.answers) {
                    text = item.respondent.msisdn +
                            ";" + answer.question.activeIndex +
                            ";\"" + answer.question.title + "\"" +
                            ";" + answer.option.activeIndex +
                            ";\"" + answer.option.answer + "\"" +
                            ";" + answer.date

                    if(text != null && text.length()>0) {
                        writer.println(text);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }
}
