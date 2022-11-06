package ru.ezhov.example.gitlabci

import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane

class GitlabPanel(private val gitlab: Gitlab, private val jobComponentRepository: JobComponentRepository) : JPanel() {
    init {
        layout = BorderLayout()

        add(WithoutStages(gitlab, jobComponentRepository = jobComponentRepository), BorderLayout.NORTH)
        add(
            JScrollPane(
                StagesComponent(gitlab, jobComponentRepository = jobComponentRepository)
            ),
            BorderLayout.CENTER
        )
    }
}

class WithoutStages(private val gitlab: Gitlab, private val jobComponentRepository: JobComponentRepository) : JPanel() {
    init {
        layout = GridLayout(6, 4)

        val jobsWithoutStage = gitlab.jobs.filter { gitlab.stage(it.name) == null }.sortedBy { it.name }
        jobsWithoutStage.forEach {
            val com = JobComponent(it)
            jobComponentRepository.add(com)
            add(com)
        }

    }
}

class StagesComponent(private val gitlab: Gitlab, private val jobComponentRepository: JobComponentRepository) : JPanel() {
    init {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        val s = this
        gitlab.stages?.values?.forEach { stage ->
            s.add(Stage(gitlab = gitlab, stageName = stage, jobComponentRepository = jobComponentRepository))
        }
    }
}

class Stage(private val gitlab: Gitlab, private val stageName: String, private val jobComponentRepository: JobComponentRepository) : JPanel() {
    init {
        layout = BorderLayout()
        border = BorderFactory.createTitledBorder(stageName)
//            add(JLabel(stageName), BorderLayout.NORTH)
        add(JobStagePanel(gitlab = gitlab, stageName = stageName, jobComponentRepository = jobComponentRepository), BorderLayout.CENTER)
    }
}

class JobStagePanel(private val gitlab: Gitlab, private val stageName: String, private val jobComponentRepository: JobComponentRepository) : JPanel() {
    init {
        layout = GridLayout(10, 1)

        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        val s = this
        val jobs = gitlab.jobs.filter { it.stage == stageName }.sortedBy { it.name }

        jobs.forEach { job ->
            val com = JobComponent(job = job)
            jobComponentRepository.add(com)
            s.add(com)
        }
    }
}

class JobComponent(val job: Job) : JLabel() {
    init {
        text = job.name
        addMouseListener(object : MouseAdapter() {
            override fun mouseReleased(e: MouseEvent?) {
                println("location - ${JobComponent@ location}")
                println("locationOnScreen - ${JobComponent@ locationOnScreen}")
            }
        })
    }
}


