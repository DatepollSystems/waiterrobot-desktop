package org.datepollsystems.waiterrobot.mediator.app

import org.datepollsystems.waiterrobot.mediator.core.ID
import java.util.*
import java.util.prefs.Preferences
import kotlin.properties.Delegates

object Settings {
    private const val SETTINGS_NODE_NAME = "org.datepollsystems.waiterrobot.mediator.settings"
    private val preferences = Preferences.userRoot().node(SETTINGS_NODE_NAME)

    var accessToken: String? by preferences.nullableString()
    var refreshToken: String? by preferences.nullableString()
    var loginPrefix: String? by preferences.nullableString()

    private var _instanceId: String? by preferences.nullableString()

    var organisationId by Delegates.notNull<ID>()
    val instanceId: String
        get() {
            val id = _instanceId
            if (id != null) return id

            return synchronized(this) {
                _instanceId ?: UUID.randomUUID().toString().also {
                    _instanceId = it
                }
            }
        }
}
