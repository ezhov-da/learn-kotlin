package ru.ezhov.example.gitlabci

import org.yaml.snakeyaml.Yaml
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File
import javax.swing.JFrame
import javax.swing.SwingUtilities

// https://www.mkammerer.de/blog/snakeyaml-and-kotlin/

fun main() {

    val gitlab = readGitlab(File("D:\\repository\\work\\.gitlab-ci.yml"))

    SwingUtilities.invokeLater {
        val frame = JFrame("test")

        val jobComponentRepository = JobComponentRepository()

        val contentPane = frame.contentPane
        contentPane.layout = BorderLayout()
        contentPane.add(GitlabPanel(gitlab, jobComponentRepository), BorderLayout.CENTER)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)
        frame.size = Dimension(800, 600)
        val gp = GlassGitlabPanel(gitlab, jobComponentRepository)
        frame.glassPane = gp
        gp.isVisible = true
        frame.isVisible = true
    }
}

private fun readGitlab(file: File): Gitlab {
    val yaml = Yaml()
    val obj: Map<String, Any> = file.bufferedReader().use { yaml.load(it) }

    var stages: Stages? = null
    var variables: Variables? = null
    var workflow: Workflow? = null
    val jobs = mutableListOf<Job>()


    obj.forEach { (k, v) ->
        when (k) {
            "stages" -> stages = buildStages(v)
            "variables" -> variables = buildVariables(v)
            "default" -> buildDefault(v)
            "workflow" -> workflow = buildWorkflow(v)
            else -> jobs.add(buildJob(k = k, v = v))
        }
    }

    return Gitlab(
        stages = stages,
        variables = variables,
        jobs = jobs,
    )
}

fun buildStages(v: Any): Stages {
//    println("buildStages: ${v.javaClass.name} $v")

    return Stages(v as ArrayList<String>)
}

fun buildVariables(v: Any): Variables {
//    println("bulidVariables: ${v.javaClass.name} $v")

    return Variables(v as LinkedHashMap<String, String>)
}

fun buildWorkflow(v: Any): Workflow {
//    println("bulidWorkflow: ${v.javaClass.name} $v")

    val map = v as LinkedHashMap<String, ArrayList<String>>
    var rules: Rules? = null
    map.forEach { (k, v) ->
        when (k) {
            "rules" -> rules = buildRules(v)
        }
    }

    return Workflow(rules = rules!!)
}

fun buildDefault(v: Any) {
    println("bulidDefault: ${v.javaClass.name} $v")
}

fun buildJob(k: String, v: Any): Job {
    println("buildJob: $k - ${v.javaClass.name} $v")

    val name = k
    val map = v as LinkedHashMap<String, Any>

    var rules: Rules? = null
    var extends: Extends? = null
    var stage: String? = null
    var variables: Variables? = null
    var needs: Needs? = null
    var dependencies: Dependencies? = null

    map.forEach { (k, v) ->
        when (k) {
            "rules" -> rules = buildRules(v)
            "extends" -> extends = buildExtends(v)
            "variables" -> variables = buildVariables(v)
            "needs" -> needs = buildNeeds(v)
            "stage" -> stage = v as String
            "dependencies" -> dependencies = buildDependencies(v)
        }
    }

    return Job(
        name = name,
        stage = stage,
        rules = rules,
        extends = extends,
        variables = variables,
        needs = needs,
        dependencies = dependencies,
    )
}

fun buildRules(v: Any): Rules {
    println("buildRule: ${v.javaClass.name} $v")
    val map = v as ArrayList<LinkedHashMap<String, String>>
    val rules = map.map { v ->
        Rule(`if` = v["if"], `when` = v["when"])
    }

    return Rules(rules)
}

fun buildExtends(v: Any): Extends {
    println("buildExtends: ${v.javaClass.name} $v")
    return when (v::class) {
        String::class -> Extends(values = listOf(v as String))
        else -> Extends(values = v as ArrayList<String>)
    }
}

fun buildNeeds(v: Any): Needs {
    println("buildNeeds: ${v.javaClass.name} $v")

    return Needs(values = v as ArrayList<String>)
}

fun buildDependencies(v: Any): Dependencies {
    println("buildDependencies: ${v.javaClass.name} $v")

    return Dependencies(values = v as ArrayList<String>)
}


data class Gitlab(
    val stages: Stages?,
    val variables: Variables?,
    val jobs: List<Job>,
) {
    fun stage(jobName: String): String? {
        val map = jobs.groupBy { it.name }.mapValues { it.value.first() }

        fun recursiveSearchStage(jobName: String): String? {
            return when (val job = map[jobName]) {
                null -> null
                else -> when (job.stage) {
                    null -> {
                        when (job.extends != null && job.extends.values.isNotEmpty()) {
                            true -> job.extends.values.map { recursiveSearchStage(it) }.firstOrNull()
                            else -> null
                        }
                    }

                    else -> job.stage
                }
            }
        }

        return recursiveSearchStage(jobName)
    }
}

data class Stages(
    val values: List<String>
)

data class Variables(val values: Map<String, String>)

data class Default(
    val tags: List<String>,
    val image: Image,
    val interruptible: Boolean,
)

data class Image(
    val name: String,
    val entrypoint: List<String>,
)

data class Workflow(
    val rules: Rules,
)

data class Rules(
    val values: List<Rule>
)

data class Rule(
    val `if`: String?,
    val `when`: String? = null,
)

data class Extends(val values: List<String>)

data class Needs(val values: List<String>)
data class Dependencies(val values: List<String>)

data class Job(
    val name: String,
    val stage: String?,
    val variables: Variables?,
    val rules: Rules?,
    val extends: Extends?,
    val needs: Needs?,
    val dependencies: Dependencies?,
)
