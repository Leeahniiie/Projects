
import java.util.List;
import java.util.ArrayList;

public class LinkedListDeque<T> implements Deque<T> {

    private class Node<T> {
        private T item;
        private Node prev;
        private Node next;

        private Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }
    private int size;
    private Node sentinel;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }
    public static void main(String[] args) {
        Deque<Integer> lld =  new LinkedListDeque<>();
    }

    @Override
    /* Adds element x to the front of a LinkedListDeque */
    public void addFirst(T x) {
        Node<T> filler = new Node(x, sentinel, sentinel.next);
        sentinel.next = filler;
        filler.next.prev = filler;
        size += 1;
    }

    @Override
    /* Adds element x to the back of a LinkedListDeque */
    public void addLast(T x) {
        Node<T> filler = new Node(x, sentinel.prev, sentinel);
        sentinel.prev.next = filler;
        sentinel.prev = filler;
        size++;
    }
    @Override
    /* Implements Lists visualization of a LinkedListDeque that is user readable */
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        int x = this.size;
        for (Node firstnode = sentinel.next; x != returnList.size(); firstnode = firstnode.next) {
            returnList.add((T) firstnode.item);
        }
        return returnList;
    }
    @Override
    /* Checks if a LinkedListDeque is empty */
    public boolean isEmpty() {
        if (this.size != 0) {
            return false;
        }
        return true;
    }
    @Override
    /* returns the number of elements in a LinkedListDeque */
    public int size() {
        return size;
    }
    @Override
    /* Removes the first element of a LinkedListDeque */
    public T removeFirst() {
        if (this.size() == 0) {
            return null;
        }
        Node<T> x = sentinel.next;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size--;
        return x.item;
    }
    @Override
    /* Removes the last element of a LinkedListDeque */
    public T removeLast() {
        if (this.size() == 0) {
            return null;
        }
        Node<T> x = sentinel.prev;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return x.item;
    }
    @Override
    /* Returns an item at a specified index of a LinkedlistDeque */
    public T get(int index) {
        Node returnnode =  sentinel;
        if (index >= this.size || index < 0) {
            return null;
        }
        for (int i = 0; i <= index; i++) {
            returnnode = returnnode.next;
        }
        return (T) returnnode.item;
    }

    @Override
    /* Returns an item at a specified index of LinkedlistDeque recursively */
    public T getRecursive(int index) {
        if (index >= this.size || index < 0) {
            return null;
        }
        Node returnnode = sentinel;
        Node x = (Node) helperRecursive(index + 1, returnnode);
        return (T) x.item;
    }
    /* Helper function used for GetRecursively Implementation */
    public Node helperRecursive(int index, Node returnnode) {
        if (index == 0) {
            return returnnode;
        }
        return helperRecursive(index - 1, returnnode.next);
    }
}
