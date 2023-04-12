package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IRegistrationDateHolder
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.ZoneId
import java.util.*

class DateUtils {
    companion object {
        fun toDateTime(dt: Date): LocalDateTime =
            Instant.fromEpochMilliseconds(dt.toInstant().toEpochMilli()).toLocalDateTime(
                TimeZone.currentSystemDefault()
            )

        fun toJavaDate(dt: LocalDate): Date =
            Date.from(dt.toJavaLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

}


fun filterRegistrationDate(e: IRegistrationDateHolder, start: LocalDate, end: LocalDate): Boolean {
    return e.registrationDate.after(DateUtils.toJavaDate(start)) && e.registrationDate.before(DateUtils.toJavaDate(end))
}