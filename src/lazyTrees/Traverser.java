package lazyTrees;

/**
 * Traver interface to effect a traversal
 * @param <E>
 */
public interface Traverser<E>
{
   /**
    * In order to effect a traversal visit method is called
    */
   public void visit(E x);
}
