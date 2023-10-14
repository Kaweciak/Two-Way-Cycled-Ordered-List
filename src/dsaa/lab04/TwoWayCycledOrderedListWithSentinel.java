package dsaa.lab04;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class TwoWayCycledOrderedListWithSentinel<E> implements IList<E>{

    private class Element{
        public Element(E e) {
            this.object = e;
            next = null;
            prev = null;
        }
        public Element(E e, Element next, Element prev) {
            this.object = e;
            this.next = next;
            this.prev = prev;
        }
        // add element e after this
        public void addAfter(Element elem) {
            elem.next = next;
            elem.prev = this;
            next.prev = elem;
            next = elem;
        }
        // assert it is NOT a sentinel
        public void remove() {
            assert this != sentinel;
            next.prev = prev;
            prev.next = next;
        }
        E object;
        Element next=null;
        Element prev=null;
    }


    Element sentinel;
    int size;

    private class InnerIterator implements Iterator<E>{
        Element pos, last;
        int index;
        public InnerIterator() {
            pos = sentinel.next;
            index = 0;
        }
        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            last = pos;
            pos = pos.next;
            index++;
            return last.object;
        }
    }

    private class InnerListIterator implements ListIterator<E>{
        Element pos, last;
        int index;

        public InnerListIterator() {
            pos = sentinel.next;
            index = 0;
        }
        @Override
        public boolean hasNext() {
            return (index < size);
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();

            last = pos;
            pos = pos.next;
            index++;
            return last.object;
        }

        @Override
        public void add(E arg0) {
            throw new UnsupportedOperationException();
        }
        @Override
        public boolean hasPrevious() {
            return (index > 0);
        }
        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }
        @Override
        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            pos = pos.prev;
            last = pos;
            index--;
            return last.object;
        }
        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        @Override
        public void set(E arg0) {
            throw new UnsupportedOperationException();
        }
    }
    public TwoWayCycledOrderedListWithSentinel() {
        sentinel = new Element(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    //@SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        int index = 0;
        if(!(e instanceof Comparable)) throw new UnsupportedOperationException();
        Element pos = sentinel.next;
        Comparable<E> comparable = (Comparable<E>)e;
        while(index < size && comparable.compareTo(pos.object) >= 0)
        {
            pos = pos.next;
            index++;
        }
        size++;
        pos.prev.addAfter(new Element(e));
        return true;
    }

    private Element getElement(int index) {
        Element pos = sentinel.next;
        if(index < 0 || index >= size || sentinel.next == sentinel)
        {
            throw new NoSuchElementException();
        }
        while(index-- > 0)
        {
            pos = pos.next;
        }
        return pos;
    }

    private Element getElement(E obj) {
        Element pos = sentinel.next;
        while(pos != sentinel)
        {
            if(obj.equals(pos.object)) return pos;
            pos = pos.next;
        }
        return null;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void clear() {
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public boolean contains(E element) {
        Element pos = sentinel.next;
        while(pos != sentinel)
        {
            if(pos.object.equals(element)) return true;
            pos = pos.next;
        }
        return false;
    }

    @Override
    public E get(int index) {
        if(index >= size || size < 0) throw new NoSuchElementException();
        Element pos = sentinel.next;
        while(index-- > 0)
        {
            pos = pos.next;
        }
        return pos.object;
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(E element) {
        int index = 0;
        Element pos = sentinel.next;
        while(pos != sentinel)
        {
            if(pos.object.equals(element)) return index;
            pos = pos.next;
            index++;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new InnerIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new InnerListIterator();
    }

    @Override
    public E remove(int index) {
        if(index < 0 || index >= size) throw new NoSuchElementException();
        Element pos = sentinel.next;
        while(index-- > 0)
        {
            pos = pos.next;
        }
        E res = pos.object;
        pos.remove();
        size--;
        return res;
    }

    @Override
    public boolean remove(E e) {
        Element pos = sentinel.next;
        while(pos != sentinel)
        {
            if(pos.object.equals(e))
            {
                pos.remove();
                size--;
                return true;
            }
            pos = pos.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    //@SuppressWarnings("unchecked")
    public void add(TwoWayCycledOrderedListWithSentinel<E> other) {
        if(this == other) return;
        Element pos = sentinel.next, otherPos = other.sentinel.next, temp;
        while(pos != sentinel && otherPos != other.sentinel)
        {
            if(((Comparable)pos.object).compareTo(otherPos.object) <= 0)
            {
                pos = pos.next;
            }
            else
            {
                temp = otherPos.next;
                pos.prev.addAfter(otherPos);
                otherPos = temp;
            }
        }
        if(otherPos != other.sentinel)
        {
            otherPos.prev = sentinel.prev;
            other.sentinel.prev.next = sentinel;
            sentinel.prev.next = otherPos;
            sentinel.prev = other.sentinel.prev;
        }
        size += other.size;
        other.clear();
    }

    //@SuppressWarnings({ "unchecked", "rawtypes" })
    public void removeAll(E e) {
        Element pos = sentinel.next;
        while(pos != sentinel)
        {
            if(pos.object.equals(e))
            {
                pos.remove();
                size--;
            }
            pos = pos.next;
        }
    }
}
