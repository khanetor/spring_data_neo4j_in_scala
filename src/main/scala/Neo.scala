import org.springframework.data.neo4j.rest.SpringCypherRestGraphDatabase
import resource._

/**
 * Created by khanguyen on 5/13/15.
 */
object Neo {
  private val scrgdb = new SpringCypherRestGraphDatabase("http://localhost:7474/db/data/", "neo4j", "12345678")

  def executeNeo[T](block: (SpringCypherRestGraphDatabase => T)) = {
    val transaction = for {
      tx <- managed(scrgdb.beginTx())
    } yield {
        val result = block(scrgdb)
        tx.success
        result
      }
    transaction.acquireFor(identity)
  }

}
