package edu.cornell.cac.docker.api

import org.skyscreamer.jsonassert.JSONCompare.compareJSON
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.JSONCompareResult
import org.specs2.Specification


class DockerApiJsonSpec extends Specification  { def is =
  s2"""Testing JSON outputs for Docker REST API
     silly test $dummyTestPass1
     silly test $dummyTestFail1
     silly test $dummyTestPass2
     silly test $dummyTestFail2
  """.stripMargin

  // JSONCompare.compareJSON(expectedStr, actualStr, compareMode)
  lazy val dummyTestPass1 = compareJSON(
    "{name: 'Ken', id: 1}",
    "{id:1, name: 'Ken'}",
    JSONCompareMode.LENIENT
  ).passed() === true

  lazy val dummyTestFail1 = compareJSON(
    "{name: 'Ken', id: 2}",
    "{id:1, name: 'Ken'}",
    JSONCompareMode.LENIENT
  ).passed() === false

  lazy val dummyTestPass2 = compareJSON(
    "{name: 'Ken'}",
    "{id:1, name: 'Ken'}",
    JSONCompareMode.LENIENT
  ).passed() === true

  lazy val dummyTestFail2 = compareJSON(
    "{id:1, name: 'Ken'}",
    "{name: 'Ken'}",
    JSONCompareMode.LENIENT
  ).passed() === false
}
