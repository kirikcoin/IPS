#!
#
#   Exports answers to the specified survey in CSV format.
#

SURVEY_ID=31664

mysql --default-character-set=utf8 -h 192.168.10.19 -u ips -ppassword ips <<< "
  SELECT
    r.registered,
    r.msisdn,
    ifnull(r.source, '') AS C2S,
    ifnull(
      (
        SELECT
          DISTINCT group_concat(ifnull(a.answer_text, qo.answer) SEPARATOR ';') AS answer
        FROM answers a
          JOIN pages q ON q.id = a.question_id
          LEFT JOIN question_options qo ON qo.id = a.option_id
        WHERE a.respondent_id = r.id
        ORDER BY q.id ASC
      ), '') AS answers

  FROM respondents r
  WHERE r.survey_id = ${SURVEY_ID} ORDER BY registered ASC" | sed 's/\t/;/g'
