package lazyTrees;


class PrintObject<E> implements Traverser<E>
{
    /**
     * This method in functor class traverses and prints out the data.
     * @param x, generic data
     */
    public void visit(E x)
    {
        System.out.print( x + " ");
    }
};
