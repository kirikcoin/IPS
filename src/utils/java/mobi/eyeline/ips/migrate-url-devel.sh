#!/usr/bin/env bash

PREFIX='ipstest.ips'
BACKUP_DIR='/tmp/ips-devel'

for SERVICE in ${PREFIX}*
do
  echo -n "> $SERVICE"

  cp ${SERVICE} ${BACKUP_DIR}
  TMP_NAME=".${SERVICE}.migrated"

  SID=${SERVICE:17:5}
  echo -n "  ID=[$SID]"
  cat ${SERVICE} | \
    sed 's/"http:\/\/ips.eyeline.mobi\/ussd\/index.jsp?survey_id=\([0-9]*\)"/"http:\/\/ips.eyeline.mobi\/service\/\1"/' >${TMP_NAME}
  mv ${TMP_NAME} ${SERVICE}

  cat ${SERVICE} | \
    sed "s/\"http:\/\/ips.eyeline.mobi\/inform\"/\"http:\/\/ips.eyeline.mobi\/inform?survey_id=$SID\"/" >${TMP_NAME}
  mv ${TMP_NAME} ${SERVICE}

  echo ""

done;
