package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.service.Services
import org.apache.commons.lang3.tuple.Pair

import javax.faces.model.SelectItem

import static java.util.concurrent.TimeUnit.HOURS
import static java.util.concurrent.TimeUnit.MILLISECONDS

class TimeZoneHelper {

    static List<SelectItem> getTimeZones(Locale locale) {
        def itemOf = { String tzId, String name ->
            int raw = TimeZone.getTimeZone(tzId).rawOffset

            def hours = MILLISECONDS.toHours raw
            def minutes = MILLISECONDS.toMinutes(raw) - HOURS.toMinutes(hours)

            def prefix = "UTC${raw < 0 ? '' : '+'}" + sprintf("%d:%02d", hours, minutes.abs())

            new SelectItem(tzId, "$name ($prefix)" as String)
        }

        Services.instance().timeZoneService.getZoneNames(locale).collect {
            Pair<String, String> kv -> itemOf(kv.key, kv.value) as SelectItem
        }
    }
}
