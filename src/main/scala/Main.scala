import resource._

/**
 * Created by khanguyen on 5/6/15.
 */
object Main extends App {

  def createIndexOnUserName = {
    Neo.executeNeo { db =>
      db.queryEngine().query(s"CREATE INDEX ON :${NodeLabels.User.name}(name)", null).finish()
    } match {
      case Left(errorList) => println(errorList)
      case Right(result) => println(result)
    }
  }

  def deleteAllNodesAndRelationships = {
    Neo.executeNeo { db =>
      val nodeIterator = db.getAllNodes.iterator
      while (nodeIterator.hasNext) {
        val node = nodeIterator.next
        val relationshipIterator = node.getRelationships.iterator
        while (relationshipIterator.hasNext) {
          relationshipIterator.next.delete
        }

        node.delete
      }
    } match {
      case Left(errorList) => println(errorList)
      case Right(result) => println("Successfully delete all nodes and relationships")
    }
  }

  def create3People = {
    Neo.executeNeo { db =>
      val jack = db.createNode(NodeLabels.User)
      jack.setProperty("name", "Jack")

      val jane = db.createNode(NodeLabels.User)
      jane.setProperty("name", "Jane")

      val peter = db.createNode(NodeLabels.User)
      peter.setProperty("name", "Peter")
    } match {
      case Left(errorList) => println("Fail to create users")
      case Right(result) => println("Successfully create users")
    }
  }

  def create3PeopleAndMakeFriends = {
    Neo.executeNeo { db =>
      val tom = db.createNode(NodeLabels.User)
      tom.setProperty("name", "Tom")

      val jerry = db.createNode(NodeLabels.User)
      jerry.setProperty("name", "Jerry")

      val ron = db.createNode(NodeLabels.User)
      ron.setProperty("name", "Ron")

      ron.createRelationshipTo(tom, RelType.IS_FRIEND_OF)
      ron.createRelationshipTo(jerry, RelType.IS_FRIEND_OF)
    } match {
      case Left(errorList) => println("Fail to create users and relationships")
      case Right(result) => println("Successfully create users and relationships")
    }
  }

  def create3MoviesAndHaveSomePeopleLikeTheMovies = {
    Neo.executeNeo { db =>
      val theMatrix = db.createNode(NodeLabels.Movie)
      theMatrix.setProperty("title", "The Matrix")

      val forrestGump = db.createNode(NodeLabels.Movie)
      forrestGump.setProperty("title", "Forrest Gump")

      for (userIterator <- managed(db.findNodesByLabelAndProperty(NodeLabels.User, "name", "Ron").iterator)) {
        if (userIterator.hasNext) {
          val ron = userIterator.next
          ron.createRelationshipTo(theMatrix, RelType.LIKE)
          ron.createRelationshipTo(forrestGump, RelType.LIKE)
        }
      }

    } match {
      case Left(el) => println(el)
      case Right(result) => println("Successful")
    }
  }

  def findMoviesYourFriendsLike = {
    Neo.executeNeo { db =>

      val params = new java.util.HashMap[String, AnyRef]()
      params.put("name", "Tom")
      val result = db.queryEngine.query( """MATCH (you:User {name: {name}})-[:IS_FRIEND_OF]-(friend:User)-[:LIKE]->(movie:Movie) RETURN movie""", params).iterator

      while (result.hasNext) {
        println(result.next())
      }
    }
  }

  def getFloatValueFromANode = {
    Neo.executeNeo { db =>
      val node = db.createNode()
      node.setProperty("aFloatNumber", 5.5f)

      val aFloatNumber = node.getProperty("aFloatNumber").asInstanceOf[Float]

      println(aFloatNumber + 4.7f)
      node.delete
    } match {
      case Left(el) => System.err.println(el)
      case Right(result) => println(result)
    }
  }
}

