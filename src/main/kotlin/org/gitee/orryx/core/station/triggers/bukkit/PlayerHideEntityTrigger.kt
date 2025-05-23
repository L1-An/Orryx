package org.gitee.orryx.core.station.triggers.bukkit

import org.bukkit.event.player.PlayerHarvestBlockEvent
import org.bukkit.event.player.PlayerHideEntityEvent
import org.gitee.orryx.api.adapters.entity.AbstractBukkitEntity
import org.gitee.orryx.core.station.triggers.AbstractPlayerEventTrigger
import org.gitee.orryx.core.station.triggers.AbstractPropertyPlayerEventTrigger
import org.gitee.orryx.module.wiki.Trigger
import org.gitee.orryx.module.wiki.TriggerGroup
import org.gitee.orryx.module.wiki.Type
import org.gitee.orryx.utils.abstract
import org.gitee.orryx.utils.toTarget
import taboolib.common.OpenResult
import taboolib.module.kether.ScriptContext

object PlayerHideEntityTrigger: AbstractPropertyPlayerEventTrigger<PlayerHideEntityEvent>("Player Hide Entity") {

    override val wiki: Trigger
        get() = Trigger.new(TriggerGroup.BUKKIT, event)
            .addParm(Type.TARGET, "entity", "实体")
            .description("当可见实体对玩家隐藏时触发")

    override val clazz
        get() = PlayerHideEntityEvent::class.java

    override fun read(instance: PlayerHideEntityEvent, key: String): OpenResult {
        return when(key) {
            "entity" -> OpenResult.successful(instance.entity.abstract())
            else -> OpenResult.failed()
        }
    }

    override fun write(instance: PlayerHideEntityEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}