package edu.cornell.cac.docker.api.entities

import edu.cornell.cac.docker.api._
import play.api.libs.json._

trait DockerVolume extends DockerEntity {
  def containerPath: Option[String]
  def hostPath: Option[String]
}

sealed case class ContainerVolume(containerPath: Option[String], hostPath: Option[String] = None) extends DockerVolume {
  override def toString = s"$containerPath"
}

sealed case class BindMountVolume(containerPath: Option[String], hostPath: Option[String], `type`: String = "ro") extends DockerVolume{
  override def toString = s"$containerPath:$hostPath:${`type`}"
}
		
object DockerVolume {
  
	  private val pattern = """^([^:]+):(/[^:]+):(ro|rw)$""".r

	  def apply(path: Option[String], hostPath: Option[String]): DockerVolume = ContainerVolume(path)

	  def unapply(v: DockerVolume): Option[(Option[String], Option[String])] = {
	    v match {
	      case bind: BindMountVolume => Some((bind.containerPath, bind.hostPath))
	      case vol: ContainerVolume => Some((vol.containerPath, None))
	      case _ => None
	    }
	  }
	  
	  def fromString(s: String): Option[BindMountVolume] = s match {
	    case pattern(containerPath, hostPath, rwType) => Some(BindMountVolume(Some(containerPath), Some(hostPath), rwType))
	    case _ => None
	  }
	  
}