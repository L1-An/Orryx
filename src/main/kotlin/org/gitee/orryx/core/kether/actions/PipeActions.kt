package org.gitee.orryx.core.kether.actions

import org.gitee.orryx.core.kether.ScriptManager.addOrryxCloseable
import org.gitee.orryx.core.kether.ScriptManager.combinationParser
import org.gitee.orryx.core.station.pipe.PipeBuilder
import org.gitee.orryx.module.wiki.Action
import org.gitee.orryx.module.wiki.Type
import taboolib.module.kether.KetherParser
import taboolib.module.kether.run
import java.util.*

object PipeActions {

    /**
     * # 管式任务
     * 阻塞kether语句直到执行完毕
     *```
     * tell pipe <uuid: UUID> <timeout: Long> trigger <String> onComplete {
     *   tell "随便运行最后一行返回值"
     *   1
     * } onBrock {
     *   tell "随便运行最后一行返回值"
     *   2
     * } onPeriod {
     *   break
     *   tell "tick"
     * } period 5
     * ```
     * */
    @KetherParser(["pipe"], shared = true)
    private fun pipe() = combinationParser(
        Action.new("Pipe管式任务", "管式任务", "pipe", true)
            .description("阻塞kether语句直到执行完毕")
            .addEntry("UUID，唯一ID名", Type.STRING)
            .addEntry("多少Ticks后完成", Type.LONG)
            .addEntry("中断触发器使用 , 分割Key", Type.STRING, optional = true, head = "trigger")
            .addEntry("完成时运行脚本 { action }", Type.ANY, optional = true, head = "onComplete")
            .addEntry("中断时运行脚本 { action }", Type.ANY, optional = true, head = "onBrock")
            .addEntry("每周期 tick 时运行脚本 { action }", Type.ANY, optional = true, head = "onPeriod")
            .addEntry("周期Tick", Type.LONG, optional = true, head = "period")
            .result("完成或中断运行脚本返回值", Type.ANY)
    ) {
        it.group(
            text(),
            long(),
            command("trigger", then = text()).option(),
            command("onComplete", then = action()).option(),
            command("onBrock", then = action()).option(),
            command("onPeriod", then = action()).option(),
            command("period", then = long()).option()
        ).apply(it) { uuid, timeout, triggers, onComplete, onBrock, onPeriod, period ->
            future {
                val pipe = PipeBuilder()
                    .uuid(UUID.fromString(uuid))
                    .timeout(timeout)
                if (onComplete != null) {
                    pipe.onComplete {
                        run(onComplete)
                    }
                }
                if (onBrock != null) {
                    pipe.onBrock {
                        run(onBrock)
                    }
                }
                if (triggers != null) {
                    pipe.brokeTriggers(*triggers.uppercase().split(",").toTypedArray())
                }
                if (onPeriod != null) {
                    pipe.periodTask(period ?: 1) {
                        run(onPeriod)
                    }
                }
                val task = pipe.build()
                addOrryxCloseable(task.result) { task.broke() }
                task.result
            }
        }
    }

}