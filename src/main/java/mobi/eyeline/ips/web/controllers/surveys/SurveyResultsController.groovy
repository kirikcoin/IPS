package mobi.eyeline.ips.web.controllers.surveys

import au.com.bytecode.opencsv.CSVWriter
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


        OutputStreamWriter writer = null;
        CSVWriter csvWriter =null;
        int count = answerRepository.count(
                getSurvey(),
                periodStart,
                periodEnd,
                filter)
        int limit = 100;

        try{
            writer = new OutputStreamWriter(os, "UTF-8");
            csvWriter = new CSVWriter(writer, ';' as char);
            List<String[]> headerRecords = new ArrayList<String[]>();
            //add header record
            headerRecords.add([strings['results.list.csv.msisdn'],
                    strings['results.list.csv.question.number'],
                    strings['results.list.csv.question.text'],
                    strings['results.list.csv.questionoption.number'],
                    strings['results.list.csv.questionoption.text'],
                    strings['results.list.csv.date']] as String[]);
            csvWriter.writeAll(headerRecords);

            for (int i = 0; i <= count / limit; i++) {
                writeCSVData(getRecords(i*limit, limit),csvWriter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                csvWriter.close();
            }
        }
    }

    private List<SurveySession> getRecords(int offset, int limit) {
        return answerRepository.list(
                getSurvey(),
                periodStart,
                periodEnd,
                filter,
                limit,
                offset)
    }

    private void writeCSVData(List<SurveySession> sessions, CSVWriter csvWriter) throws IOException {

        List<String[]> data  = toStringArray(sessions);
        csvWriter.writeAll(data);
    }

    private List<String[]> toStringArray(List<SurveySession> sessions) {
        List<String[]> records = new ArrayList<String[]>();
        for(SurveySession item:sessions) {
            for(Answer answer:item.answers) {

                records.add([item.respondent.msisdn,
                        answer.question.activeIndex,
                        answer.question.title,
                        answer.option.activeIndex,
                        answer.option.answer,
                        answer.date] as String[]);
            }
        }

        return records;
    }


}
