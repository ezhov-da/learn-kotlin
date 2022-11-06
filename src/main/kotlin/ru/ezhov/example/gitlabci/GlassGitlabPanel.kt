package ru.ezhov.example.gitlabci

import java.awt.Color
import java.awt.Graphics
import javax.swing.JComponent

class GlassGitlabPanel(private val gitlab: Gitlab, private val jobComponentRepository: JobComponentRepository) : JComponent() {

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        g.color = Color.red

        gitlab.jobs.forEach { job ->
            if (job.name == "build:gradle:scheduled:build_cache") {
                jobComponentRepository.componentBy(job.name)?.let { currentJobComponent ->
                    job.extends?.values?.forEach { extend ->
                        jobComponentRepository.componentBy(extend)?.let { extendJobComponent ->
                            println("x-locationOnScreen-${currentJobComponent.locationOnScreen.x} y-locationOnScreen-${currentJobComponent.locationOnScreen.y}")
                            println("x-location-${currentJobComponent.location.x} y-location-${currentJobComponent.location.y}")

                            g.drawLine(
                                currentJobComponent.locationOnScreen.x,
                                currentJobComponent.locationOnScreen.y,
                                extendJobComponent.x,
                                extendJobComponent.y,
                            )
                        }
                    }
                }
            }
        }
    }
}


