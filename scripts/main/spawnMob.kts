package main
//WayZer 版权所有(请勿删除版权注解)
import mindustry.ctype.ContentType
import mindustry.game.Team
import mindustry.type.UnitType

name = "扩展功能: 召唤单位"

command("spawn", "召唤单位") {
    usage = "[类型ID=列出] [队伍ID,默认为sharded] [数量=1]"
    permission = id.replace("/", ".")
    aliases = listOf("召唤")
    body {
        val list = content.getBy<UnitType>(ContentType.unit)
        val type = arg.getOrNull(0)?.toIntOrNull()?.let { list.items.getOrNull(it) } ?: returnReply(
            "[red]请输入类型ID: {list}"
                .with("list" to list.mapIndexed { i, type -> "[yellow]$i[green]($type)" }.joinToString())
        )
        val team = arg.getOrNull(1)?.let { s ->
            s.toIntOrNull()?.let { Team.all.getOrNull(it) } ?: returnReply(
                "[red]请输入队伍ID: {list}"
                    .with("list" to Team.baseTeams.mapIndexed { i, type -> "[yellow]$i[green]($type)" }.joinToString())
            )
        } ?: Team.sharded
        val num = arg.getOrNull(2)?.toIntOrNull() ?: 1
        repeat(num) {
            type.create(team).apply {
                if (player != null) set(player!!.unit().x, player!!.unit().y)
                else team.data().core()?.let {
                    set(it.x, it.y)
                }
                add()
            }
        }
        reply("[green]成功为 {team} 生成 {num} 只 {type}".with("team" to team, "num" to num, "type" to type.name))
    }
}