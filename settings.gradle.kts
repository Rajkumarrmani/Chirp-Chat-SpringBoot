pluginManagement {
	repositories {
		maven { url = uri("https://repo.spring.io/snapshot") }
		gradlePluginPortal()
	}
}
rootProject.name = "LearningChirp"
include("app")
include("user")
include("chat")
include("notification")
include("common")
