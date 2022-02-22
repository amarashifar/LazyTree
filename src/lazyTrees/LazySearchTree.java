package lazyTrees;

import java.util.NoSuchElementException;

/**
 * This class contains the BST with an inner Node class.
 * Different methods help modify the generic BST such as insert, remove, or softly delete.
 * @author Amir(ali) Marashifar 
 */
public class LazySearchTree<E extends Comparable< ? super E > >
        implements Cloneable
{
    /**
     * Inner Node Class
     */
    private class LazySTNode
    {
        // use public access so the tree or other classes can access members 
        public LazySTNode lftChild, rtChild;
        public E Item;
        boolean deleted;

        public LazySTNode(E d, LazySTNode lft, LazySTNode rt)
        {
            lftChild = lft;
            rtChild = rt;
            Item = d;
        }

        public LazySTNode()
        { this(null, null, null); }

        // function stubs -- for use only with AVL Trees when we extend
        // Don't really need them for this part? Right?
    }

    // Continuation of LazySearchTreeF
    protected int mSize;
    protected LazySTNode mRoot;
    protected int mSizehard;

    /**
     * LazySearchTree Constructor
     */
    public LazySearchTree() {
        clear();
    }


    /**
     * returns soft nodes
     */
    public int size() {
        return mSize;
    }

    /**
     * resets the tree
     */
    public void clear() {
        mSize = 0; mRoot = null;
    }

    /**accessor for mSizeHard
     * @return Hard Nodes
     **/
    public int sizeHard() {
        return mSizehard;
    }

    /**
     * public method for finding the min value
     * @return the smallest node in the tree
     */
    public E findMin()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMin(mRoot).Item;
    }

    /**
     * public method for finding the max value
     * @return largest node in the tree
     */
    public E findMax()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMax(mRoot).Item;
    }

    /**
     * public version of find method.
     * @param x, our generic data
     * @return our desired data
     */
    public E find( E x )
    {
        LazySTNode resultNode;
        resultNode = find(mRoot, x);
        if (resultNode == null)
            throw new NoSuchElementException();
        return resultNode.Item;
    }

    /**
     * checks if the data exists in the tree
     */
    public boolean contains(E x)  { return find(mRoot, x) != null; }


    /**
     * Public version of insert that inserts a node
     * @param x, generic data
     * @return true if insertion was successful
     */
    public boolean insert( E x )
    {
        int oldSize = mSize;
        mRoot = insert(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * Public version of remove that remove a node
     * @param x, generic data
     * @return true if removal was successful
     */
    public boolean remove(E x)
    {
        int oldSize = mSize;
        mRoot = remove(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * public method of traverseHard
     * @param func function object
     * @param <F> implements traverser?
     */
    public < F extends Traverser<? super E > >
    void traverseHard(F func)
    {
        traverseHard(func, mRoot);
    }

    /**
     * public method of traverseSoft
     * @param func function object
     * @param <F> implements traverser
     */
    public < F extends Traverser<? super E > >
    void traverseSoft(F func)
    {
        traverseSoft(func, mRoot);
    }

    // private helper methods ----------------------------------------
    /**
    recursive method for finding the min value
    return the min node that's not deleted
     **/

    /**
     * Protected method for finding the min value.
     * @param root, node we start with our recursion
     * @return the smallest node in the BST
     */
    protected LazySTNode findMin(LazySTNode root )
    {
        var smaller = new LazySTNode();
        if (root == null) //base case
            return null;

        if (root.lftChild == null)
            return root;

        smaller = findMin(root.lftChild);

        if (smaller != null)
            return smaller;

        return findMin(root.lftChild);
    }

    /**
     * Protected method for finding the max value.
     * @param root, node we start with our recursion
     * @return largest node in BST
     */
    protected LazySTNode findMax(LazySTNode root )
    {
        var Larger = new LazySTNode();
        if (root == null) //base case
            return null;

        if (root.rtChild == null)
            return root;

        Larger = findMax(root.rtChild);

        if (Larger != null)
            return Larger;
        return findMax(root.rtChild);
    }

    /**
     * Protected version of insertion of generic data
     * @param root, node we start with our recursion
     * @param x, generic data
     * @return our inserted root.
     */
    protected LazySTNode insert(LazySTNode root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
        {
            mSize++;
            mSizehard++;
            return new LazySTNode(x, null, null);
        }

        compareResult = x.compareTo(root.Item);
        if ( compareResult < 0 )
            root.lftChild = insert(root.lftChild, x);
        else if ( compareResult > 0 )
            root.rtChild = insert(root.rtChild, x);
        else if (root.deleted) {
            root.deleted = false;
            mSize++;
            return root;
        }
        return root;
    }


    /**
     * Protected method of remove.
     * Softly deletes our desired node
     * @param root, node we start with our recursion
     * @param x, generic data
     * @return removed node
     */
    protected LazySTNode remove(LazySTNode root, E x)
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null) //base case
            return null;

        compareResult = x.compareTo(root.Item);
        if ( compareResult < 0 )
            root.lftChild = remove(root.lftChild, x);
        else if ( compareResult > 0 )
            root.rtChild = remove(root.rtChild, x);
            // found the node
        else //if there are no children, hopefully.
        {
            root.deleted = true;
            mSize--;
        }
        return root;
    }

    /**
     * Protected method is for traversing hard
     * @param func function object
     * @param treeNode out desired node
     * @param <F> implement traverser? for subclassses
     */
    protected <F extends Traverser<? super E>>
    void traverseHard(F func, LazySTNode treeNode)
    {
        if (treeNode == null)
            return;
        traverseHard(func, treeNode.lftChild);
        func.visit(treeNode.Item);
        traverseHard(func, treeNode.rtChild);
    }


    /**
     * Protected version of traverseSoft
     * @param func function object
     * @param treeNode our desired node
     * @param <F> implement traverser? for subclassses
     */
    protected <F extends Traverser<? super E>>
    void traverseSoft(F func, LazySTNode treeNode)
    {
        if (treeNode == null)
            return;
        traverseSoft(func, treeNode.lftChild);
        if (!treeNode.deleted) {
            func.visit(treeNode.Item);
        }
        traverseSoft(func, treeNode.rtChild);
    }

    /**
     * Private method returns our desired node
     * @param root, node we start with our recursion
     * @param x, Generic param
     * @return desired node
     */
    protected LazySTNode find(LazySTNode root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.Item);
        if (compareResult < 0)
            return find(root.lftChild, x);
        if (compareResult > 0)
            return find(root.rtChild, x);
        if (root.deleted) {
            return null;
        }
        return root;   // found
    }


}
