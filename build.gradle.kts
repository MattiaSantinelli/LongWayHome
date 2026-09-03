plugins {
    id("java")
    id("application")
}

group = "it.unicam.cs.mpgc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javafxVersion = "21"
val platform = "win" // Impostato per Windows

dependencies {
    // Dipendenze dirette per JavaFX
    implementation("org.openjfx:javafx-controls:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-fxml:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-base:$javafxVersion:$platform")

    // Dipendenze per JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Dipendenze per Gson
    implementation("com.google.code.gson:gson:2.13.2")
}

application {
    mainClass.set("it.unicam.cs.mpgc.rpg130324.App")
}

tasks.test {
    useJUnitPlatform()
}