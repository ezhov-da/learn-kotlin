package ru.ezhov.example.gitlabci

class JobComponentRepository {
    private val map: MutableMap<String, JobComponent> = mutableMapOf()

    fun add(component: JobComponent) {
        map[component.job.name] = component
    }

    fun componentBy(jobName: String): JobComponent? = map[jobName]
}
