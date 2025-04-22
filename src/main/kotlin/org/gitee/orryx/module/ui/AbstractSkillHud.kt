package org.gitee.orryx.module.ui

import org.bukkit.entity.Player
import org.gitee.orryx.core.common.timer.SkillTimer
import org.gitee.orryx.core.key.BindKeyLoaderManager
import org.gitee.orryx.core.key.IBindKey
import org.gitee.orryx.core.key.IGroup
import org.gitee.orryx.core.skill.IPlayerSkill
import org.gitee.orryx.utils.job
import java.util.concurrent.CompletableFuture

abstract class AbstractSkillHud(override val viewer: Player, override val owner: Player): ISkillHud {

    override fun getShowSkills(): CompletableFuture<Map<IBindKey, String?>?> {
        return owner.job {
            it.bindKeyOfGroup[BindKeyLoaderManager.getGroup(it.group)] ?: emptyMap()
        }
    }

    override fun getCountdown(skill: IPlayerSkill): Long {
        return SkillTimer.getCountdown(skill.player, skill.key)
    }

    override fun setGroup(group: IGroup): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        owner.job {
            it.setGroup(group.key).thenApply { bool ->
                future.complete(bool)
            }
        }
        return future
    }
}