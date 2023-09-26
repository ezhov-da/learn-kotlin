package ru.ezhov.example.kotlinparser

fun main() {
    val testClassName = "NewServiceTest"

    val constructorParams = """
        newsDao: NewsDao
        newsCommentDao: NewsCommentDao
        currentUserContext: CurrentUserContext
    """.trimIndent()

    val testNames = """
        should be failure when throw exception
        should be success when all good
    """.trimIndent()

    println(createTestClass(testClassName, constructorParams, testNames))
}

fun createTestClass(testClassName: String, constructorParams: String, testNames: String): String {

    val constructor = createConstructorParams(constructorParams)
    val tests = createTests(testNames)

    return TestClassBuilder(testClassName, constructor, tests).build()
}

fun createConstructorParams(constructorParams: String): List<ConstructorParams> =
    constructorParams
        .split("\n")
        .filter { it.isNotBlank() }
        .map { it.split(":").let { ar -> ConstructorParams(name = ar[0].trim(), type = ar[1].trim().replace(",", "")) } }

fun createTests(testNames: String): List<String> =
    testNames
        .split("\n")
        .filter { it.isNotBlank() }
        .map { it.trim() }

data class ConstructorParams(
    val name: String,
    val type: String,
)

class TestClassBuilder(
    private val testClassName: String,
    private val constructorParams: List<ConstructorParams>,
    private val testNames: List<String>
) {
    fun build(): String {
        val builder = StringBuilder()
        val testWord = "test"
        val testClassNameAsFieldAndClass = if (testClassName.lowercase().endsWith(testWord)) {
            testClassName.substring(0, testClassName.length - testWord.length)
        } else {
            testClassName
        }.let { testClassNameAsField ->
            testClassNameAsField.replaceFirstChar { it.lowercase() } to testClassNameAsField
        }

        builder.append("import io.mockk.Runs").append("\n")
        builder.append("import io.mockk.every").append("\n")
        builder.append("import io.mockk.just").append("\n")
        builder.append("import io.mockk.mockk").append("\n")
        builder.append("import io.mockk.slot").append("\n")
        builder.append("import io.mockk.verify").append("\n")
        builder.append("import org.assertj.core.api.Assertions.assertThat").append("\n")
        builder.append("import org.assertj.core.api.Assertions.assertThatThrownBy").append("\n")
        builder.append("import org.junit.jupiter.api.BeforeEach").append("\n")
        builder.append("import org.junit.jupiter.api.Test").append("\n")
        builder.append("\n")
        builder.append("internal class $testClassName {").append("\n")
        constructorParams.forEach { constructor ->
            builder.append("    private lateinit var ${constructor.name}: ${constructor.type}").append("\n")
        }
        builder.append("    private lateinit var ${testClassNameAsFieldAndClass.first}: ${testClassNameAsFieldAndClass.second}").append("\n")
        builder.append("\n")
        builder.append("    @BeforeEach").append("\n")
        builder.append("    fun beforeEach() {").append("\n")
        constructorParams.forEach { constructor ->
            builder.append("        ${constructor.name} = mockk()").append("\n")
        }
        builder.append("        ${testClassNameAsFieldAndClass.first} = ${testClassNameAsFieldAndClass.second}(").append("\n")
        constructorParams.forEach { constructor ->
            builder.append("            ${constructor.name} = ${constructor.name},").append("\n")
        }
        builder.append("        )").append("\n")
        builder.append("    }").append("\n")
        builder.append("\n")
        testNames.forEach { testName ->
            builder.append("    @Test").append("\n")
            builder.append("    fun `$testName`() {}").append("\n")
            builder.append("\n")
        }
        builder.append("}").append("\n")
        builder.append("\n")

        return builder.toString()
    }
}

// createTestClass(_v1, _v2, _v3)
