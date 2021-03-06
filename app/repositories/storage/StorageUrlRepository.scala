package repositories.storage

import com.datastax.driver.core.Row
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._
import com.websudos.phantom.keys.PartitionKey
import com.websudos.phantom.reactivestreams._
import conf.connection.DataConnection
import domain.storage.StorageUrl

import scala.concurrent.Future

/**
 * Created by hashcode on 2016/01/05.
 */
class StorageUrlRepository extends CassandraTable[StorageUrlRepository, StorageUrl] {

  object organisationId extends StringColumn(this) with PartitionKey[String]

  object storageUrlId extends StringColumn(this)  with PrimaryKey[String]

  object name extends StringColumn(this)

  object url extends StringColumn(this)


  override def fromRow(r: Row): StorageUrl = {
    StorageUrl(
      organisationId(r),
      storageUrlId(r),
      name(r),
      url(r)
    )
  }
}

object StorageUrlRepository extends StorageUrlRepository with RootConnector {
  override lazy val tableName = "storageurls"

  override implicit def space: KeySpace = DataConnection.keySpace

  override implicit def session: Session = DataConnection.session

  def save(link: StorageUrl): Future[ResultSet] = {
    insert
        .value(_.organisationId, link.organisationId)
      .value(_.storageUrlId, link.storageUrlId)
      .value(_.name, link.name)
      .value(_.url, link.url)
      .future()
  }

  def getFileResultById(organisationId: String, storageUrlId: String): Future[Option[StorageUrl]] = {
    select.where(_.organisationId eqs organisationId).and (_.storageUrlId eqs storageUrlId).one()
  }

  def findAll: Future[Seq[StorageUrl]] = {
    select.fetchEnumerator() run Iteratee.collect()
  }
  def getStorageUrl(organisationId: String): Future[Seq[StorageUrl]] = {
    select.where(_.organisationId eqs organisationId).fetchEnumerator() run Iteratee.collect()
  }

  def deleteById(organisationId: String, storageUrlId: String): Future[ResultSet] = {
    delete.where(_.organisationId eqs organisationId).and (_.storageUrlId eqs storageUrlId).future()
  }
}
