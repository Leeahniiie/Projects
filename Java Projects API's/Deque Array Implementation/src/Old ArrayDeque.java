//import java.util.List;
//import java.util.ArrayList;
//
//import static com.google.common.truth.Truth.assertThat;
//
//
//public class ArrayDeque2<T> implements Deque<T> {
//    public static void main(String[] args) {
//
//        Deque<Integer> lld = new ArrayDeque<>();
//        lld.addFirst(1);
//        lld.addFirst(5);
//        lld.addFirst(9);
//        lld.addFirst(6);
//        lld.removeLast();
//        lld.removeFirst();
//    }
//    private T[] array;
//    private int size;
//    private int nextfirst = 0;
//    private int initialfirst = 0;
//    private int nextlast = 7;
//
//    private int initiallast = 7;
//    public ArrayDeque() {
//        array = (T[]) new Object[8];
//        size = 0;
//    }
//    @Override
//    public void addFirst(T x) {
//        if (nextfirst >= array.length || array[nextfirst] != null) {
//            array = resize(array, size * 2);
//        }
//        array[nextfirst] = x;
//        nextfirst += 1;
//        size += 1;
//    }
//
//    public T[] resize(T[] ary, int capasity) {
//        T[] newArray = (T[]) new Object[(capasity)];
//        int x = initiallast;
//        System.arraycopy(ary, initialfirst, newArray, 0, nextfirst - initialfirst);
//        if (nextlast + 1 < array.length) {
//            System.arraycopy(ary, nextlast + 1, newArray, (capasity) - (x - nextlast), x - nextlast);
//        }
//        ary = newArray;
//        nextlast = capasity - (initiallast - nextlast) - 1;
//        initiallast = capasity - 1;
//
//        return ary;
//    }
//
//    @Override
//    public void addLast(T x) {
//        if (nextlast < 0 || array[nextlast] != null) {
//            array = resize(array, size * 2);
//
//        }
//        array[nextlast] = x;
//        nextlast -= 1;
//        size += 1;
//
//    }
//
//    @Override
//    public List<T> toList() {
//        List<T> returnList = new ArrayList<>();
//        if (this.size == 0) {
//            return returnList;
//        }
//        for (int i = 0; i < nextfirst; i += 1) {
//            returnList.add((T) array[nextfirst - i - 1]);
//        }
//        for (int i = 0; i < initiallast - nextlast; i += 1) {
//            returnList.add((T) array[initiallast - i]);
//        }
//
//
//        return returnList;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        if (this.size != 0) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int size() {
//        return size;
//    }
//
//    @Override
//    public T removeFirst() {
//        if (this.size == 0) {
//            initiallast = array.length - 1;
//            nextlast = initiallast;
//            nextfirst = 0;
//            initialfirst = 0;
//            return null;
//        }
//        T x = this.get(size - size);
//        if (this.size > 16 & this.size <= array.length / 4) {
//            array = resize(array, array.length / 2);
//            nextfirst = nextfirst - initialfirst;
//            initialfirst = 0;
//
//        }
//        //if the previous of next first is out of bounds
//        if (nextfirst - 1 < 0) {
//            array[initiallast] = null;
//            initiallast -= 1;
//            size -= 1;
//        } else {
//            nextfirst -= 1;
//            array[nextfirst] = null;
//            size -= 1;
//        }
//        if (this.size == 0) {
//            initiallast = array.length - 1;
//            nextlast = initiallast;
//            nextfirst = 0;
//            initialfirst = 0;
//        }
//
//        return x;
//    }
//    @Override
//    public T removeLast() {
//        if (this.size == 0) {
//            return null;
//        }
//        T x = this.get(size - 1);
//        if (this.size > 16 & this.size <= array.length / 4) {
//            array = resize(array, array.length / 2);
//            nextfirst = nextfirst - initialfirst;
//            initialfirst = 0;
//
//        }
//        // if there is no addlast and only values in the front of the array
//        if (initiallast == nextlast) {
//            array[initialfirst] = null;
//            initialfirst += 1;
//            size -= 1;
//        } else {
//            array[nextlast + 1] = null;
//            nextlast += 1;
//            size -= 1;
//        }
//        if (this.size == 0) {
//            initiallast = array.length - 1;
//            nextlast = initiallast;
//            nextfirst = 0;
//            initialfirst = 0;
//        }
//        return x;
//    }
//
//    @Override
//    public T get(int index) {
//        if (index >= this.size || index < 0) {
//            return null;
//        }
//        //if initialfirst is not at the 0th index of the array get, from the end of the array
////        if (nextfirst - (index + 1) < initialfirst) {
////            if (index == 0) {
////                return array[initialfirst];
////            } else {
////                return array[initiallast + 1 - index];
////            }
////        }
//        if (index == 0 & nextfirst!=0) {
//            return array[nextfirst - 1];
//        }
//        if (nextfirst == initialfirst & index == 0) {
//            return array[initiallast];
//        }
//        return array[Math.floorMod((nextfirst - (index + 1)) + (0 - initialfirst), array.length)];
//    }
//}
