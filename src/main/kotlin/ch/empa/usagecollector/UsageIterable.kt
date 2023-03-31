package ch.empa.usagecollector

import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IRegistrationDateHolder
import ch.ethz.sis.openbis.generic.asapi.v3.dto.common.interfaces.IRegistratorHolder

interface UsageIterable: IRegistrationDateHolder, IRegistratorHolder {
}