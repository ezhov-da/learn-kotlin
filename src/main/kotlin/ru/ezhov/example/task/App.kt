package ru.ezhov.example.task

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.JTextPane
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val tasksBoardComponent = TasksBoardComponent()

        val frame = JFrame()

        frame.add(tasksBoardComponent, BorderLayout.CENTER)
        frame.size = Dimension(700, 500)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}

private class TasksBoardComponent : JPanel() {
    private val tasksBoardResizedListeners = mutableSetOf<TasksBoardResizedListener>()
    private val taskComponents = mutableListOf<TaskComponent>()

    init {
        layout = null
        val component = this
        border = BorderFactory.createLineBorder(Color.RED)
        addMouseListener(object : MouseAdapter() {
            override fun mouseReleased(e: MouseEvent) {
                if (e.button == MouseEvent.BUTTON3) {
                    val newTaskCreatedCallBack = object : NewTaskCreatedCallBack {
                        override fun created(task: Task) {
                            val taskComponent = TaskComponent(
                                task = task,
                                taskBoard = component,
                                currentBoardWidth = component.size.width,
                                currentBoardHeight = component.size.height
                            )
                            MoveUtil.addMoveAction(taskComponent, taskComponent)
                            addTask(taskComponent = taskComponent)
                        }
                    }

                    NewTaskComponent(
                        newTaskCreatedCallBack,
                        Coordinates(
                            width = component.size.width,
                            height = component.size.height,
                            x = e.x,
                            y = e.y,
                        )
                    ).isVisible = true
                }
            }
        })

        addComponentListener(object : ComponentListener {
            override fun componentResized(e: ComponentEvent) {
                tasksBoardResizedListeners.forEach {
                    it.newSize(e.component.width, e.component.height)
                }
            }

            override fun componentMoved(e: ComponentEvent) {}

            override fun componentShown(e: ComponentEvent) {}

            override fun componentHidden(e: ComponentEvent) {}
        })
    }

    private fun addTask(taskComponent: TaskComponent) {
        taskComponents.add(taskComponent)
        val component = this
        if (taskComponents.size == 2) {
            val line = Line(taskComponents[0], taskComponents[1]).apply {
                size = component.size
                setLocation(0, 0)
                border = BorderFactory.createLineBorder(Color.BLACK)
            }
            tasksBoardResizedListeners.add(line)
            add(line)
        }

        add(taskComponent)
        tasksBoardResizedListeners.add(taskComponent)
        revalidate()
        repaint()
    }
}

private class TaskComponent(
    var task: Task, // TODO временно var
    val taskBoard: TasksBoardComponent,
    currentBoardWidth: Int,
    currentBoardHeight: Int,
) : JLabel(), TasksBoardResizedListener {
    init {
        text = task.name
        border = BorderFactory.createLineBorder(Color.GREEN)
        size = Dimension(100, 25)
        val taskComponent = this
        calculateAndSetLocation(currentBoardWidth, currentBoardHeight)

        addComponentListener(object : ComponentListener {
            override fun componentResized(e: ComponentEvent) {}

            override fun componentMoved(e: ComponentEvent) {
                val coordinates = Coordinates(
                    width = taskBoard.width,
                    height = taskBoard.height,
                    x = e.component.x,
                    y = e.component.y
                )

                task = Task(
                    coordinates = coordinates,
                    name = task.name, body = task.body
                )
            }

            override fun componentShown(e: ComponentEvent) {}

            override fun componentHidden(e: ComponentEvent) {}
        })
    }

    private fun calculateAndSetLocation(
        currentBoardWidth: Int,
        currentBoardHeight: Int,
    ) {
        val initWidth = task.coordinates.width
        val initHeight = task.coordinates.height
        val taskX = task.coordinates.x
        val taskY = task.coordinates.y

        val resultX = when (initWidth) {
            currentBoardWidth -> taskX
            else -> ((taskX.toFloat() / initWidth * currentBoardWidth)).toInt()
        }
        val resultY = when (initHeight) {
            currentBoardHeight -> taskY
            else -> ((taskY.toFloat() / initHeight * currentBoardHeight)).toInt()
        }

//        println(
//            "initWidth=$initWidth " +
//                "initHeight=$initHeight " +
//                "taskX=$taskX " +
//                "taskY=$taskY " +
//                "currentBoardWidth=$currentBoardWidth " +
//                "currentBoardHeight=$currentBoardHeight " +
//                "resultX=$resultX " +
//                "resultY=$resultY"
//        )

        setLocation(resultX, resultY)
    }

    override fun newSize(width: Int, height: Int) {
        calculateAndSetLocation(currentBoardWidth = width, currentBoardHeight = height)
    }
}

interface TasksBoardResizedListener {
    fun newSize(
        width: Int,
        height: Int,
    )
}

private class NewTaskComponent(
    private val newTaskCreatedCallBack: NewTaskCreatedCallBack,
    private val coordinates: Coordinates,
) : JDialog() {
    private val name: JTextField = JTextField()
    private val body: JTextPane = JTextPane()
    private val ok: JButton = JButton("Create")

    init {
        layout = BorderLayout()
        size = Dimension(300, 300)
        add(name, BorderLayout.NORTH)
        add(JScrollPane(body), BorderLayout.CENTER)
        add(JPanel().apply { add(ok) }, BorderLayout.SOUTH)
        setLocationRelativeTo(null)

        ok.addActionListener {
            newTaskCreatedCallBack.created(
                Task(
                    coordinates = coordinates,
                    name = name.text,
                    body = body.text,
                )
            )
            NewTaskComponent@ this.dispose()
        }
    }
}

private class Line(
    private val task1: TaskComponent,
    private val task2: TaskComponent
) : JPanel(), TasksBoardResizedListener {
    init {
        layout = null
        isOpaque = false

        val thisLine = this

        task1.addComponentListener(object : ComponentListener {
            override fun componentResized(e: ComponentEvent?) {
            }

            override fun componentMoved(e: ComponentEvent?) {
                thisLine.repaint()
            }

            override fun componentShown(e: ComponentEvent?) {
            }

            override fun componentHidden(e: ComponentEvent?) {
            }

        })

        task2.addComponentListener(object : ComponentListener {
            override fun componentResized(e: ComponentEvent?) {
            }

            override fun componentMoved(e: ComponentEvent?) {
                thisLine.repaint()
            }

            override fun componentShown(e: ComponentEvent?) {
            }

            override fun componentHidden(e: ComponentEvent?) {
            }
        })
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        val g2d = g as Graphics2D

        if (task1.y < task2.y) {
            val p1 = centerDown(task1)
            val p2 = centerTop(task2)
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y)
        } else if (task1.y > task2.y) {
            val p1 = centerTop(task1)
            val p2 = centerDown(task2)
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y)
        } else {
            val p1 = centerTop(task1)
            val p2 = centerTop(task2)
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y)
        }
    }

    private fun centerTop(task: TaskComponent): Point =
        Point(task.x + (task.width / 2), task.y)

    private fun centerDown(task: TaskComponent): Point =
        Point(task.x + (task.width / 2), (task.y - task.height) + (task.width / 2))

    override fun newSize(width: Int, height: Int) {
        size = Dimension(width, height)
        repaint()
    }
}

interface NewTaskCreatedCallBack {
    fun created(task: Task)
}

class Task(
    val coordinates: Coordinates,
    val name: String,
    val body: String
)

data class Coordinates(
    val width: Int,
    val height: Int,
    val x: Int,
    val y: Int,
)
