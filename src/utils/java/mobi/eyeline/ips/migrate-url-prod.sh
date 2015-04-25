#!/usr/bin/env bash

PREFIX='ips.ips'
BACKUP_DIR='/tmp/ips-prod'

for SERVICE in ${PREFIX}*
do
  echo -n "> $SERVICE"

  cp ${SERVICE} ${BACKUP_DIR}
  TMP_NAME=".${SERVICE}.migrated"

  SID=${SERVICE:17:5}
  echo -n "  ID=[$SID]"
  cat ${SERVICE} | \
    sed 's/"https:\/\/ips.eyeline.mobi\/ussd\/index.jsp?survey_id=\([0-9]*\)"/"https:\/\/ips.eyeline.mobi\/service\/\1"/' >${TMP_NAME}
  mv ${TMP_NAME} ${SERVICE}

  cat ${SERVICE} | \
    sed "s/\"https:\/\/ips.eyeline.mobi\/inform\"/\"https:\/\/ips.eyeline.mobi\/inform?survey_id=$SID\"/" >${TMP_NAME}
  mv ${TMP_NAME} ${SERVICE}

  echo ""

done;
