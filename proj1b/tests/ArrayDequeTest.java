import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

    @Test
    public void addFirstTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(lld1.toList()).containsExactly("back").inOrder();

        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }
    @Test
    public void addLastTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }
    @Test
    public void ToListTest() {
        Deque<Character> lld2 = new ArrayDeque<>();
        lld2.addFirst('c');
        lld2.addFirst('c');
        lld2.addFirst('a');
        lld2.addFirst('e');
        assertThat(lld2.toList()).containsExactly('e', 'a', 'c', 'c').inOrder();
        Deque<Integer> lld3 = new ArrayDeque<>();
        assertThat(lld3.toList()).containsExactly().inOrder();

    }
    @Test
    public void IsEmptyTest() {
        Deque<Character> lld2 = new ArrayDeque<>();
        assertThat(lld2.isEmpty()).isTrue();
        assertThat(lld2.size()).isEqualTo(0);

        Deque<Integer> lld3 = new ArrayDeque<>();
        lld3.addFirst(1);
        lld3.addFirst(2);
        lld3.addFirst(3);
        lld3.addFirst(4);

        assertThat(lld3.isEmpty()).isFalse();
        assertThat(lld3.size()).isEqualTo(4);
    }
    @Test
    public void GetTest() {
        Deque<Character> lld2 = new ArrayDeque<>();
        assertThat(lld2.get(4)).isNull();

        Deque<Integer> lld3 = new ArrayDeque<>();
        lld3.addFirst(1);
        lld3.addFirst(2);
        lld3.addFirst(3);
        lld3.addFirst(4);

        assertThat(lld3.get(-1)).isNull();
        assertThat(lld3.get(1)).isEqualTo(3);

        Deque<Integer> lld4 = new ArrayDeque<>();
        lld4.addFirst(5);
        lld4.addFirst(6);
        lld4.addFirst(3);
        lld4.addFirst(7);
        assertThat(lld4.get(5)).isNull();
        lld4.addLast(7);
        lld4.addLast(8);
        lld4.addFirst(4);
        lld4.get(6);
        assertThat(lld4.get(6)).isEqualTo(8);

        Deque<Integer> ad = new ArrayDeque<>();
        ad.addLast(0);
        ad.removeFirst();
        ad.addLast(2);
        ad.removeLast();
        ad.addFirst(4);
        ad.addFirst(5);
        ad.removeFirst();
        ad.addFirst(7);
        ad.get(0);
        ad.removeLast();
        ad.removeLast();
        ad.addFirst(11);
        ad.addFirst(12);
        ad.get(0);
        ad.removeLast();
        ad.removeLast();
        ad.addLast(16);
        assertThat(ad.removeLast()).isEqualTo(16);
    }
    @Test
    public void TestAllRemove() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(8);
        assertThat(lld1.removeLast()).isEqualTo(8);
        assertThat(lld1.removeLast()).isEqualTo(null);
        assertThat(lld1.removeFirst()).isEqualTo(null);
        lld1.removeFirst();
        assertThat(lld1.toList()).isEmpty();
        lld1.addFirst(3);
        assertThat(lld1.size()).isEqualTo(1);
        lld1.removeFirst();
        assertThat(lld1.size()).isEqualTo(0);
        Deque<Integer> lld = new ArrayDeque<>();
        lld.addFirst(1);
        lld.addFirst(5);
        lld.addFirst(9);
        lld.addFirst(6);
        assertThat(lld.removeLast()).isEqualTo(1);
        assertThat(lld.removeFirst()).isEqualTo(6);
        lld.removeFirst();
        assertThat(lld.removeFirst()).isEqualTo(5);
        assertThat(lld.removeFirst()).isEqualTo(null);
        Deque<Integer> lld6 = new ArrayDeque<>();
        lld6.addFirst(2);
        assertThat(lld6.removeLast()).isEqualTo(2);
        assertThat(lld6.removeLast()).isEqualTo(null);
        Deque<Integer> lld8 = new ArrayDeque<>();
        lld8.removeLast();
        assertThat(lld8.size()).isEqualTo(0);
        lld8.addFirst(4);
        assertThat(lld8.size()).isEqualTo(1);
        lld8.addFirst(0);
        lld8.removeFirst();
        assertThat(lld8.size()).isEqualTo(1);
        assertThat(lld8.toList()).containsExactly(4);
        lld8.removeLast();
        assertThat(lld8.size()).isEqualTo(0);
        assertThat(lld8.toList()).isEmpty();
        Deque<Integer> lld5 = new ArrayDeque<>();
        lld5.addLast(7);
        assertThat(lld5.removeFirst()).isEqualTo(7);
        assertThat(lld5.size()).isEqualTo(0);
        Deque<Integer> lld2 = new ArrayDeque<>();
        lld2.addFirst(5);
        lld2.addFirst(3);
        lld2.removeFirst();
        assertThat(lld2.size()).isEqualTo(1);
    }

    @Test
    public void removeFirst() {
        Deque<Integer> lld = new ArrayDeque<>();
        Deque<Integer> lld2 = new ArrayDeque<>();
        Deque<Integer> lld3 = new ArrayDeque<>();

        for (int i = 0; i < 20; i+=1) {
            lld.addFirst(i);
        }
        assertThat(lld.size() == 20);

        for (int i = 0; i < 20; i+= 1) {
            lld.removeLast();
        }
        assertThat(lld.toList()).containsExactly();
        assertThat(lld2.toList()).containsExactly();
        assertThat(lld2.size()).isEqualTo(0);
        assertThat((lld.size())).isEqualTo(0);

        lld2.addFirst(3);
        lld2.addFirst(4);
        lld2.addFirst(2);
        lld2.addFirst(8);
        lld2.addFirst(9);
        lld2.addFirst(6);
        lld2.addFirst(9);
        lld2.addFirst(1);

        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();
        lld2.removeFirst();

        assertThat(lld2.toList()).containsExactly();

        lld3.addLast(6);
        lld3.addLast(9);
        lld3.addLast(0);
        lld3.addLast(3);
        lld3.addLast(5);
        lld3.addLast(4);
        lld3.addLast(8);
        lld3.addLast(4);

        lld3.removeLast();
        lld3.removeLast();
        lld3.removeLast();
        lld3.removeLast();
        lld3.removeLast();
        lld3.removeLast();
        lld3.removeLast();
        lld3.removeLast();

        assertThat(lld3.toList()).containsExactly();
    }
    @Test
    public void removeEdge() {
        Deque<Integer> lld = new ArrayDeque<>();
        Deque<Integer> lld2 = new ArrayDeque<>();

        lld.addFirst(5);
        lld.addFirst(8);
        lld.addFirst(9);

        lld.removeFirst();
        lld.removeFirst();
        lld.removeFirst();

        lld.addFirst(9);
        assertThat(lld.size()).isEqualTo(1);

        lld2.addLast(7);
        lld2.addLast(7);
        lld2.addLast(7);

        lld2.removeLast();
        lld2.removeLast();
        lld2.removeLast();

        lld2.addLast(7);

        assertThat(lld2.size()).isEqualTo(1);

        Deque<Integer> ad = new ArrayDeque<>();
        ad.addLast(0);
        ad.removeFirst();
        ad.addFirst(2);
        ad.removeLast();
        ad.addLast(4);
        ad.removeLast();
        ad.addFirst(6);
        ad.removeLast();
        ad.addLast(8);
        ad.get(0);
        ad.addLast(10);
        ad.addLast(11);
        ad.addLast(12);
        ad.get(0);
        ad.get(3);
        ad.get(1);
        ad.removeFirst();
        assertThat(ad.removeFirst()).isEqualTo(10);
    }

    @Test
    public void RemoveUpTo500() {
        Deque<Integer> lld = new ArrayDeque<>();

        for (int i = 0; i < 500; i+=1) {
            lld.addLast(i);
        }
        for (int i = 499; i >= 0; i-=1) {
            //lld.removeLast();
            assertThat(lld.removeLast()).isEqualTo(i);
        }
    }

    @Test
    public void mysteryTest(){
        ArrayDeque<Integer> lld = new ArrayDeque<>();
        lld.addFirst(0);
        lld.addFirst(1);
        for (int i = 0; i < lld.size(); i++) {
            //System.out.println(lld.get(i));
        }
      //  System.out.println();

        lld.removeLast();
        for (int i = 0; i < lld.size(); i++) {
           // System.out.println(lld.get(i));
        }
       // System.out.println();

        lld.addLast(3);
        lld.addLast(4);


        for (int i = 0; i < lld.size(); i++) {
           System.out.println(lld.get(i));
        }
        System.out.println();
        System.out.print("HERE");
        System.out.println(lld.get(0));
        lld.removeLast();

        for (int i = 0; i < lld.size(); i++) {
            System.out.println(lld.get(i));
        }
        System.out.println();
    }
}
