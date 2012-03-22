abstract class RIterator implements java.util.Iterator<EIterator>  
{	
    public abstract EIterator next();
    public abstract boolean hasNext();
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}