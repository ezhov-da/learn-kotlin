package ru.ezhov.example.checklist.parser

import java.io.File

fun main() {
    val lines = """
        # Test
        *
        123
        *

        -- head
        - item
    """.trimIndent()
        .split("\n")
    val checkList = CheckList()
    var lastItemLine: Int? = null
    var currentItem: CheckListItem? = null

    lines.forEachIndexed { index, line ->
        if (line.startsWith("- ")) {
            var description: String? = null
            if (lastItemLine != null) {
                description = lines.subList(lastItemLine!! + 1, index).joinToString(separator = File.separator)
                currentItem!!.description = description.trim()
            }

            currentItem = CheckListItem()
            lastItemLine = index
            currentItem!!.title = line.substring(2)
            checkList.items.add(currentItem!!)
        } else {
            if (index == lines.size - 1) {
                if (lastItemLine != null && currentItem != null) {
                    currentItem!!.description =
                        lines.subList(0, lastItemLine!! + 1).joinToString(separator = File.separator).trim()
                }
            }
        }
    }

    println(checkList)
}


class CheckList {
    var title: String? = null
    val items: MutableList<CheckListItem> = mutableListOf()
    override fun toString(): String {
        return "CheckList(title=$title, items=$items)"
    }


}

class CheckListItem {
    var title: String? = null
    var blocks: MutableList<String> = mutableListOf()
    var description: String? = null
    override fun toString(): String {
        return "CheckListItem(title=$title, description=$description)"
    }
}
