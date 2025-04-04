package org.gitee.orryx.core.kether.actions.game

import org.gitee.orryx.core.kether.ScriptManager.combinationParser
import org.gitee.orryx.core.message.PluginMessageHandler
import org.gitee.orryx.core.targets.PlayerTarget
import org.gitee.orryx.module.wiki.Action
import org.gitee.orryx.module.wiki.Type
import org.gitee.orryx.utils.*
import taboolib.module.kether.KetherParser

object OrryxModActions {

    @KetherParser(["ghost"], namespace = ORRYX_NAMESPACE, shared = true)
    private fun actionGhost() = combinationParser(
        Action.new("Orryx Mod额外功能", "设置鬼影状态", "ghost", true)
            .description("设置鬼影状态")
            .addEntry("时长", Type.LONG, false)
            .addEntry("密度", Type.INT)
            .addEntry("间隔", Type.INT, true, default = "0")
            .addContainerEntry(optional = true, default = "@self")
            .addContainerEntry(optional = true, default = "@self", head = "viewers", description = "可视玩家")
    ) {
        it.group(
            long(),
            int(),
            int().option().defaultsTo(0),
            theyContainer(true),
            command("viewers", then = container()).option()
        ).apply(it) { timeout, density, gap, container, viewers ->
            now {
                container.orElse(self()).forEachInstance<PlayerTarget> { player ->
                    viewers.orElse(self()).forEachInstance<PlayerTarget> { viewer ->
                        PluginMessageHandler.applyGhostEffect(viewer.getSource(), player.getSource(), timeout*50, density, gap)
                    }
                }
            }
        }
    }

    @KetherParser(["flicker"], namespace = ORRYX_NAMESPACE, shared = true)
    private fun actionFlicker() = combinationParser(
        Action.new("Orryx Mod额外功能", "滞留一道闪影", "flicker", true)
            .description("滞留一道闪影")
            .addEntry("时长", Type.LONG, false)
            .addEntry("透明度", Type.FLOAT, true, default = "1")
            .addContainerEntry(optional = true, default = "@self")
            .addContainerEntry(optional = true, default = "@self", head = "viewers", description = "可视玩家")
    ) {
        it.group(
            long(),
            float().option().defaultsTo(1f),
            theyContainer(true),
            command("viewers", then = container()).option()
        ).apply(it) { timeout, alpha, container, viewers ->
            now {
                container.orElse(self()).forEachInstance<PlayerTarget> { player ->
                    viewers.orElse(self()).forEachInstance<PlayerTarget> { viewer ->
                        PluginMessageHandler.applyFlickerEffect(viewer.getSource(), player.getSource(), timeout*50, alpha)
                    }
                }
            }
        }
    }

    @KetherParser(["mouse"], namespace = ORRYX_NAMESPACE, shared = true)
    private fun actionMouse() = combinationParser(
        Action.new("Orryx Mod额外功能", "设置是否呼出鼠标", "mouse", true)
            .description("设置是否呼出鼠标")
            .addEntry("是否呼出", Type.BOOLEAN, false)
            .addContainerEntry(optional = true, default = "@self")
    ) {
        it.group(
            bool(),
            theyContainer(true)
        ).apply(it) { show, container ->
            now {
                container.orElse(self()).forEachInstance<PlayerTarget> { player ->
                    PluginMessageHandler.applyMouseCursor(player.getSource(), show)
                }
            }
        }
    }

}