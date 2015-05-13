import org.neo4j.graphdb.RelationshipType;

/**
 * Created by khanguyen on 5/13/15.
 */
public enum RelType implements RelationshipType {
    IS_FRIEND_OF, LIKE,
    KNOWS, LIKES
}
