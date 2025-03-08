package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {

    public static void main(String[] args) {
    }
    private T[] array;
    private int size;
    private static final int RESIZER = 16;
    private int nextFirst = 7;
    private int nextLast = 0;
    public ArrayDeque() {
        array = (T[]) new Object[8];
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        if (this.size == array.length) {
            array = resize(array, size * 2);
        }
        array[nextFirst] = x;
        nextFirst = Math.floorMod(nextFirst - 1 + array.length, array.length);
        size += 1;

    }

    @Override
    public void addLast(T x) {
        if (this.size == array.length) {
            array = resize(array, size * 2);
        }
        array[nextLast] = x;
        nextLast = Math.floorMod(nextLast + 1, array.length);
        size += 1;
    }

    public T[] resize(T[] ary, int capasity) {
        T[] newArray = (T[]) new Object[(capasity)];
        for (int i = 0; i < this.size; i++) {
            newArray[i] = this.get(i);
        }
        ary = newArray;
        nextLast = this.size;
        nextFirst = ary.length - 1;
        return ary;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        if (this.size == 0) {
            return returnList;
        }
        for (int i = 0; i < this.size; i++) {
            returnList.add(this.get(i));
        }
        return returnList;
    }
    @Override
    public boolean isEmpty() {
        if (this.size != 0) {
            return false;
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    //removes the first value of the ToList
    public T removeFirst() {
        if (this.size == 0) {
            return null;
        }
        if (array.length > RESIZER & this.size <= array.length / 4) {
            array = resize(array, array.length / 2);
        }
        T x = this.get(0);
        nextFirst = Math.floorMod(nextFirst + 1, array.length);
        array[nextFirst] = null;
        size -= 1;
        return x;
    }

    @Override
    //removes the last value of the ToList
    public T removeLast() {
        if (this.size == 0) {
            return null;
        }
        if (array.length > RESIZER & this.size <= array.length / 4) {
            array = resize(array, array.length / 2);
        }
        T x = this.get(this.size - 1);
        nextLast = Math.floorMod(nextLast - 1 + array.length, array.length);
        array[nextLast] = null;
        size -= 1;
        return x;
    }

    @Override
    //gets the value in a ToList at a specified index
    public T get(int index) {
        return array[Math.floorMod(nextFirst + 1 + index, array.length)];
    }

    @Override
    public T getRecursive(int index) {
        return get(index);
    }

    @Override

    public Iterator<T> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<T> {
        private int wizPos;
        public DequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }
        public T next() {
            T returnItem = array[Math.floorMod(nextFirst + 1 + wizPos, array.length)];
            wizPos += 1;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Deque otherSet) {
            if (this.size != otherSet.size()) {
                return false;
            }
            for (T x : this) {
                if (!otherSet.toList().contains(x)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.toList().toString();
    }
}

