package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CreateWorld {

    Random random;
    int counter = 2;
    ArrayList<Integer> prev;
    ArrayList<Integer> curr;
    final Integer WIDTH = 80;
    final Integer HEIGHT = 30;
    private TETile[][] tiles;

    public CreateWorld() {
        tiles = new TETile[WIDTH+11][HEIGHT+11];
        this.random = new Random();
        initalizeWorld();
        int one = RRange(4, 7);
        int two = RRange(4, 8);
        int three = RRange(5, 7);
        int four = RRange(4, 6);
        int five = RRange(3, 6);
        int six = RRange(4, 8);
        MakeRoom(one - 2, 0);
        MakeRoom(two, 1);
        MakeRoom(three, 2);
        MakeRoom(four, 3);
        MakeRoom(five, 4);
        MakeRoom(six, 5);
    }
    public CreateWorld(long seed) {
        tiles = new TETile[WIDTH+11][HEIGHT+11];
        initalizeWorld();
        this.random = new Random(seed);
        int one = RRange(4, 7);
        int two = RRange(4, 8);
        int three = RRange(5, 7);
        int four = RRange(4, 6);
        int five = RRange(3, 6);
        int six = RRange(4, 8);
        MakeRoom(one - 2, 0);
        MakeRoom(two, 1);
        MakeRoom(three, 2);
        MakeRoom(four, 3);
        MakeRoom(five, 4);
        MakeRoom(six, 5);
    }
    public void initalizeWorld() {
        for (int x = 0; x < WIDTH + 11; x++) {
            for (int y = 0; y < HEIGHT + 11; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public void MakeRoom(int n, int variable) {
        while (n > 0) {
            if (variable == 0) {
                HelperRoom(RRange(0, 12), RRange(0,30));
            }
            if (variable == 1) {
                HelperRoom(RRange(13, 26), RRange(0, 30));
            }
            if (variable == 2) {
                HelperRoom(RRange(27, 40), RRange(0,30));
            }
            if (variable == 3) {
                HelperRoom(RRange(41, 54), RRange(0,30));
            }
            if (variable == 4) {
                HelperRoom(RRange(55, 67), RRange(0,30));
            }
            if (variable == 5) {
                HelperRoom(RRange(68, 80), RRange(0,30));
            }
            n--;
        }
    }

    private void HelperRoom(int Latitude, int Longitude){
        int x = RRange(3,5);
        int y = RRange(3,5);
        int lat = Latitude;
        int lon = Longitude;
        if (counter == 2) {
            prev = new ArrayList<>(Arrays.asList(RRange(lat, lat+x), RRange(lon, lon+y)));
            counter--;
        }
        else if (counter == 1) {
            int temp = RRange(lat + 1, lat + x-1);
            int temp2 = RRange(lon + 1, lon + y-1);
            curr = new ArrayList<>(Arrays.asList(temp, temp2));
            counter = 0;
        }
        if (counter == 0) {
            ConnectHallway();
            prev = curr;
            counter = 1;
        }
        for (int i = (2 + x) * (2 + y); i > 0;) {
            for (int j = x + 1; j >= 0; j--) {
                if (lat == Latitude || lat == Latitude + x + 1 || lon == Longitude || lon == Longitude + y + 1) {
                    if (tiles[lat][lon] == Tileset.NOTHING) {
                        tiles[lat][lon] = Tileset.WALL;
                    }
                } else {
                    tiles[lat][lon] = Tileset.FLOOR;
                }
                lat++;
                i--;
            }
            lon++;
            lat = Latitude;
        }
    }

    private void ConnectHallway() {
        boolean dec = true;
        ArrayList<Integer> priority;
        ArrayList<Integer> room;
        if (prev.get(1) > curr.get(1)) {
            priority = prev;
            room = curr;
        } else {
            priority = curr;
            room = prev;
        }
        int HoV = RRange(0,1);
        if (priority.get(0) > room.get(0)) {
            MakeHallwayInc(HoV, priority, room, dec);
        } else {
            dec = false;
            MakeHallwayInc(HoV, priority, room, dec);
        }
    }

    private void MakeHallwayInc(int Hov, ArrayList<Integer> priority, ArrayList<Integer> other, boolean dec) {
        int x = priority.get(0);
        int y = priority.get(1);
        if (Hov == 0) {
            if (dec == true) { //go left then down
                for (int i = priority.get(0) ; i > other.get(0); i--) {
                    tiles[i][priority.get(1)] = Tileset.FLOOR;
                    if (tiles[i][priority.get(1) + 1] != Tileset.FLOOR) {
                        tiles[i][priority.get(1) + 1] = Tileset.WALL;
                    }
                    if ((priority.get(1) - 1 >= 0) && tiles[i][priority.get(1) - 1] != Tileset.FLOOR) {
                        tiles[i][priority.get(1) - 1] = Tileset.WALL;
                    }
                    x = i;
                }
                for (int i = priority.get(1) ; i > other.get(1); i--) {
                    tiles[x][i] = Tileset.FLOOR;
                    if (tiles[x + 1][i] != Tileset.FLOOR) {
                        tiles[x + 1][i] = Tileset.WALL;
                    }
                    if ((x - 1 >= 0) && tiles[x - 1][i] != Tileset.FLOOR) {
                        tiles[x - 1][i] = Tileset.WALL;
                    }
                }
            } else { // go right then down
                for (int i = priority.get(0) ; i < other.get(0); i++) {
                    tiles[i][priority.get(1)] = Tileset.FLOOR;
                    if (tiles[i][priority.get(1) + 1] != Tileset.FLOOR) {
                        tiles[i][priority.get(1) + 1] = Tileset.WALL;
                    }
                    if ((priority.get(1) - 1 >= 0) && tiles[i][priority.get(1) - 1] != Tileset.FLOOR) {
                        tiles[i][priority.get(1) - 1] = Tileset.WALL;
                    }
                    x = i;
                }
                for (int i = priority.get(1) ; i > other.get(1); i--) {
                    tiles[x][i] = Tileset.FLOOR;
                    if (tiles[x + 1][i] != Tileset.FLOOR) {
                        tiles[x + 1][i] = Tileset.WALL;
                    }
                    if ((x - 1 >= 0) && tiles[x - 1][i] != Tileset.FLOOR) {
                        tiles[x - 1][i] = Tileset.WALL;
                    }
                }
            }
        } else {
            if (dec == true) { //go down then left
                for (int i = priority.get(1) ; i > other.get(1); i--) {
                    tiles[priority.get(0)][i] = Tileset.FLOOR;
                    if (tiles[priority.get(0) + 1][i] != Tileset.FLOOR) {
                        tiles[priority.get(0) + 1][i] = Tileset.WALL;
                    }
                    if ((priority.get(0) - 1 >= 0) && tiles[priority.get(0) - 1][i] != Tileset.FLOOR) {
                        tiles[priority.get(0) - 1][i] = Tileset.WALL;
                    }
                    y = i;
                }
                for (int i = priority.get(0) ; i > other.get(0); i--) {
                    tiles[i][y] = Tileset.FLOOR;
                    if (tiles[i][y+1] != Tileset.FLOOR) {
                        tiles[i][y+1] = Tileset.WALL;
                    }
                    if ((y-1 >= 0) && tiles[i][y-1] != Tileset.FLOOR) {
                        tiles[i][y-1] = Tileset.WALL;
                    }
                }
            } else { // go down then right
                for (int i = priority.get(1); i > other.get(1); i--) {
                    tiles[priority.get(0)][i] = Tileset.FLOOR;
                    if (tiles[priority.get(0) + 1][i] != Tileset.FLOOR) {
                        tiles[priority.get(0) + 1][i] = Tileset.WALL;
                    }
                    if ((priority.get(0) - 1 >= 0) && tiles[priority.get(0) - 1][i] != Tileset.FLOOR) {
                        tiles[priority.get(0) - 1][i] = Tileset.WALL;
                    }
                    y = i;
                }
                for (int i = priority.get(0); i < other.get(0); i++) {
                    tiles[i][y] = Tileset.FLOOR;
                    if (tiles[i][y+1] != Tileset.FLOOR) {
                        tiles[i][y+1] = Tileset.WALL;
                    }
                    if ((y-1 >= 0) && tiles[i][y-1] != Tileset.FLOOR) {
                        tiles[i][y-1] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public void OtherWorld(int Latitude, int Longitude) {
        int x = 10;
        int y = 7;
        int lat = Latitude;
        int lon = Longitude;
        for (int i = (2 + x) * (2 + y); i > 0;) {
            for (int j = x + 1; j >= 0; j--) {
                if (lat == Latitude || lat == Latitude + x + 1 || lon == Longitude || lon == Longitude + y + 1) {
                    if (tiles[lat][lon] == Tileset.NOTHING) {
                        tiles[lat][lon] = Tileset.WALL;
                    }
                } else {
                    tiles[lat][lon] = Tileset.FLOOR;
                }
                lat++;
                i--;
            }
            lon++;
            lat = Latitude;
        }
    }
    public int RRange (int min, int max){
        int rand = random.nextInt(max - min + 1) + min;
        return rand;
    }

    public static void main(String[] args) {
        CreateWorld yee = new CreateWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(92,41);
        ter.renderFrame(yee.getTiles());
    }
}