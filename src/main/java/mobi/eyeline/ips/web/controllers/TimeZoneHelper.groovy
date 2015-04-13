package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import mobi.eyeline.ips.service.Services
import org.apache.commons.lang3.tuple.Pair

import javax.faces.model.SelectItem
import java.text.SimpleDateFormat

import static java.lang.Math.abs
import static java.util.concurrent.TimeUnit.HOURS
import static java.util.concurrent.TimeUnit.MILLISECONDS

@CompileStatic
class TimeZoneHelper {

  static List<SelectItem> getTimeZones(Locale locale) {
    def itemOf = { String tzId, String name ->
      int raw = TimeZone.getTimeZone(tzId).rawOffset

      def hours = MILLISECONDS.toHours raw
      def minutes = MILLISECONDS.toMinutes(raw) - HOURS.toMinutes(hours)

      def prefix = "UTC${raw < 0 ? '' : '+'}" + sprintf("%d:%02d", hours, abs(minutes))

      new SelectItem(tzId, "$name ($prefix)" as String)
    }

    Services.instance().timeZoneService.getZoneNames(locale).collect {
      Pair<String, String> kv -> itemOf(kv.key, kv.value) as SelectItem
    }
  }

  /**
   * Formats the date using the same exact pattern as the
   * {@link mobi.eyeline.util.jsf.components.input_date.InputDate InputDate component}.
   *
   * @see mobi.eyeline.util.jsf.components.input_date.InputDateRenderer InputDateRenderer
   */
  static String formatDateTime(Date date, TimeZone tz) {
    // See InputDateRenderer for the date format.
    def dateFormat = new SimpleDateFormat('dd.MM.yyyy HH:mm:ss')
    dateFormat.timeZone = tz
    return dateFormat.format(date)
  }
}
