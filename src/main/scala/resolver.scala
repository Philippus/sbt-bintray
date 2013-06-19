package bintray

import java.io.File
import org.apache.ivy.plugins.resolver.{ DependencyResolver, IBiblioResolver }
import org.apache.ivy.plugins.repository.{ AbstractRepository, Repository }
import bintry._

case class BintrayRepository(
  underlying: Repository, bty: Client#Repo#Package)
 extends AbstractRepository {
  def getResource(src: String) = underlying.getResource(src)
  def get(src: String, dest: File) = underlying.get(src, dest)
  override def put(src: File, dest: String, overwrite: Boolean) {
    // put with bintray
    println("putting %s to %s overwrite %s"
            .format(src, dest, overwrite))
    bty.mvnUpload(dest, src)
  }
  def list(parent: String) = underlying.list(parent)
}

case class BintrayResolver(
  name: String, url: String, bty: Client#Repo#Package)
     extends IBiblioResolver {
  setName(name)
  setM2compatible(true)
  setRoot(url)
  // wrap repository
  override def setRepository(repository: Repository) =
    super.setRepository(BintrayRepository(repository, bty))
}
