plugins {
  id("java-library")
  id("application")
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.2")
}

application {
  mainClass = "com.github.gilhamiltonjr.minesweeper.JMinesweeper"
}

tasks.test {
  useJUnitPlatform()
}