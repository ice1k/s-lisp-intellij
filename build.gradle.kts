import org.gradle.language.base.internal.plugins.CleanRule
import org.jetbrains.grammarkit.GrammarKitPluginExtension
import org.jetbrains.grammarkit.tasks.*
import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.*
import java.nio.file.*
import java.net.URL
import java.util.stream.Collectors

val isCI = !System.getenv("CI").isNullOrBlank()
val commitHash = kotlin.run {
	val process: Process = Runtime.getRuntime().exec("git rev-parse --short HEAD")
	process.waitFor()
	@Suppress("RemoveExplicitTypeArguments")
	val output = process.inputStream.use {
		process.inputStream.use {
			it.readBytes().let<ByteArray, String>(::String)
		}
	}
	process.destroy()
	output.trim()
}

val pluginComingVersion = "0.1"
val pluginVersion = if (isCI) "$pluginComingVersion-$commitHash" else pluginComingVersion
val packageName = "org.ice1000.slisp"
val kotlinVersion = "1.2.41"

group = packageName
version = pluginVersion

plugins {
	java
	id("org.jetbrains.intellij") version "0.3.5"
	id("org.jetbrains.grammarkit") version "2018.1.7"
	kotlin("jvm") version "1.2.41"
}

apply { plugin("org.jetbrains.grammarkit") }
configure<GrammarKitPluginExtension> {
	grammarKitRelease = "2017.1.5"
}

intellij {
	updateSinceUntilBuild = false
	instrumentCode = true
	val root = "/home/ice1000/.local/share/JetBrains/Toolbox/apps"
	localPath = "$root/IDEA-U/ch-0/182.3684.101"
	alternativeIdePath = "$root/PyCharm-C/ch-0/182.3684.100"
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<PatchPluginXmlTask> {
	changeNotes(file("res/META-INF/change-notes.html").readText())
	pluginDescription(file("res/META-INF/descriptions.html").readText())
	version(pluginComingVersion)
	pluginId(packageName)
}

java.sourceSets {
	"main" {
		withConvention(KotlinSourceSet::class) {
			listOf(java, kotlin).forEach { it.srcDirs("src", "gen") }
		}
		resources.srcDirs("res")
	}

	"test" {
		withConvention(KotlinSourceSet::class) {
			listOf(java, kotlin).forEach { it.srcDirs("test") }
		}
		resources.srcDirs("testData")
	}
}

repositories { mavenCentral() }

dependencies {
	compileOnly(kotlin("stdlib", kotlinVersion))
	compile(kotlin("stdlib-jdk8", kotlinVersion).toString()) {
		exclude(module = "kotlin-runtime")
		exclude(module = "kotlin-reflect")
		exclude(module = "kotlin-stdlib")
	}
	compile("org.eclipse.mylyn.github", "org.eclipse.egit.github.core", "2.1.5") {
		exclude(module = "gson")
	}
	testCompile(kotlin("test-junit", kotlinVersion))
	testCompile("junit", "junit", "4.12")
}

task("displayCommitHash") {
	group = "help"
	description = "Display the newest commit hash"
	doFirst { println("Commit hash: $commitHash") }
}

task("isCI") {
	group = "help"
	description = "Check if it's running in a continuous-integration"
	doFirst { println(if (isCI) "Yes, I'm on a CI." else "No, I'm not on CI.") }
}

// Don't specify type explicitly. Will be incorrectly recognized
val parserRoot = Paths.get("org", "ice1000", "slisp")!!
val lexerRoot = Paths.get("gen", "org", "ice1000", "slisp")!!
fun path(more: Iterable<*>) = more.joinToString(File.separator)
fun bnf(name: String) = Paths.get("grammar", "$name-grammar.bnf").toString()
fun flex(name: String) = Paths.get("grammar", "$name-lexer.flex").toString()

val genParser = task<GenerateParser>("genParser") {
	group = tasks["init"].group
	description = "Generate the Parser and PsiElement classes"
	source = bnf("slisp")
	targetRoot = "gen/"
	pathToParser = path(parserRoot + "SLispParser.java")
	pathToPsiRoot = path(parserRoot + "psi")
	purgeOldFiles = true
}

val genLexer = task<GenerateLexer>("genLexer") {
	group = genParser.group
	description = "Generate the Lexer"
	source = flex("slisp")
	targetDir = path(lexerRoot)
	targetClass = "SLispLexer"
	purgeOldFiles = true
}

val cleanGenerated = task("cleanGenerated") {
	group = tasks["clean"].group
	description = "Remove all generated codes"
	doFirst {
		delete("gen", "pinpoint-piggy")
	}
}

tasks.withType<KotlinCompile> {
	dependsOn(genParser, genLexer)
	kotlinOptions {
		jvmTarget = "1.8"
		languageVersion = "1.2"
		apiVersion = "1.2"
		freeCompilerArgs = listOf("-Xenable-jvm-default")
	}
}

tasks.withType<Delete> {
	dependsOn(cleanGenerated)
}

