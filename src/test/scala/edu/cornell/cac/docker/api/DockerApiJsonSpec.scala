package edu.cornell.cac.docker.api

import org.specs2.Specification
import play.api.libs.json._
import edu.cornell.cac.docker.api.json.FormatsV112._
import edu.cornell.cac.docker.api.entities._

import JsonComparison._


class DockerApiJsonSpec extends Specification  { def is =
  s2"""
  Testing JSON outputs for Docker REST API
    Testing JSON tester...
     dummy test: reorder objects $dummyTestPass1
     dummy test: value change $dummyTestFail1
     dummy test: expected is subest of actual $dummyTestPass2
     dummy test: actual is subset of expected $dummyTestFail2

    Testing ContainerConfiguration
     Simple volume check: $testCfgTest
  """.stripMargin

  // JSONCompare.compareJSON(expectedStr, actualStr, compareMode)
  lazy val dummyTestPass1 = compareJsonLenient(
    "{name: 'Ken', id: 1}",
    "{id:1, name: 'Ken'}"
  ) === true

  lazy val dummyTestFail1 = compareJsonLenient(
    "{name: 'Ken', id: 2}",
    "{id:1, name: 'Ken'}"
  ) === false

  lazy val dummyTestPass2 = compareJsonLenient(
    "{name: 'Ken'}",
    "{id:1, name: 'Ken'}"
  ) === true

  lazy val dummyTestFail2 = compareJsonLenient(
    "{id:1, name: 'Ken'}",
    "{name: 'Ken'}"
  ) === false

  // Container configuration tests
  // Test inclusion of Volume data in HostConfig and Bindings

  val outPathContainer = "/data"
  val outPathHost = "/tmp"
  val testCfg = ContainerConfiguration(Some("test_container"), Some(Seq("pwd")),
    volumes = Some(Map(outPathContainer -> DockerVolume(outPathContainer, outPathHost)))
  )
  lazy val testCfgExpected =
    """
      |{
      |    "Image" : "test_container",
      |    "Cmd" : [ "pwd" ],
      |    "Volumes": {
      |        "/data": {}
      |    },
      |    "HostConfig": {
      |        "Binds": ["/tmp:/data"]
      |    }
      |}
    """.stripMargin

  lazy val testCfgActual = Json.prettyPrint(Json.toJson(testCfg))

  lazy val testCfgTest = compareJsonLenient(
    testCfgExpected, testCfgActual
  ) === true

}
