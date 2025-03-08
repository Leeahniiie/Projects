package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.platform.engine.TestTag;

import java.awt.*;
import java.util.ArrayList;

public class InteractWorld {
    CreateWorld world;
    TETile[][] tiles;
    TERenderer ter = new TERenderer();
    ArrayList<Integer> flowerLoc = new ArrayList();

    boolean ExitCheck = false;

    /* if we dont have a condition for L and N
    if we have a static save that is true and the user loads a new world
    they will be taken to the previously saved world
     */
    static boolean Save = false;
    int n = 0;
    int x;
    int y;

    static ArrayList<Integer> SaveLoc = new ArrayList();

    public InteractWorld() {
        //start the avatar in the first room
        //if wall ignore walking
        //change tile to avatar tile if walking unless wall
        StdDraw.enableDoubleBuffering();
        ter.initialize(91, 43);
        drawFrame("CS61B: THE GAME");
        StartGame();
        this.tiles = world.getTiles();
        AvatarStart(false, tiles);
        ter.renderFrame(tiles);
        MoveAvatar(tiles,9223372036854775807L);


    }

    private int StartGame() {
        Character GameType;
        if (StdDraw.hasNextKeyTyped()) {
            GameType = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (GameType == 'n') {
                this.world = new CreateWorld();
                //drawString("Enter Seed",91/2, 41 / 2 - 5.5);
               // MySeed();
                return 0;
            } else if (GameType == 'l') {
                //put seed
                this.world = new CreateWorld();
                this.x = SaveLoc.get(0);
                this.y = SaveLoc.get(1);
                return 0;
            } else if (GameType == 'q') {
                System.exit(0);
            } else {
                StartGame();
            }
        } else {
            for (int i = 0; i < 300000; i++) {
                StdDraw.pause(1);
                if (StdDraw.hasNextKeyTyped()) {
                    StartGame();
                    break;
                }
            }
        }
        return 0;
    }

    private void Clear(){
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(91.0, 41.0, 91.0, 41.0);
    }

    private void AnotherWorld() {
        Clear();
        drawString("You Have 7 Seconds to Detonate All of the Bombs ",91/2, 41/2);
        StdDraw.pause(2000);
        CreateWorld NewWorld = new CreateWorld();
        NewWorld.initalizeWorld();
        //make tiles a different scheme and put coins everywhere
        //put 6 coins
        NewWorld.OtherWorld(NewWorld.RRange(0, 80), NewWorld.RRange(0,30));
        TETile[][] newTile = NewWorld.getTiles();
            int z = 0;
            while(z <= 5) {
                int x = world.RRange(0,80);
                int y = world.RRange(0,30);
                if (newTile[x][y] == Tileset.FLOOR) {
                    newTile[x][y] = Tileset.TREE;
                    z++;
                }
            }
            AvatarStart(true, newTile);
            ter.renderFrame(newTile);
            //avatar is starting in the background of the other world possibly
            MoveAvatar(newTile, 1390);
            Clear();
            drawString("You Failed To Save Yourself",91/2, 41/2);
            StdDraw.pause(4000);
            System.exit(0);
        //if all coins are collected go back to other world

    }
    private void AvatarStart(boolean anotherworld, TETile[][] tile) {
        boolean floor = false;
        while (floor == false) {
            int x = world.RRange(0, 80);
            int y = world.RRange(0, 30);
            if (tile[x][y] == Tileset.FLOOR) {
                tile[x][y] = Tileset.AVATAR;
                System.out.println("here");
                floor = true;
                this.x = x;
                this.y = y;
            }
        }
        if (anotherworld == false) {
            while (floor == true) {
                int x = world.RRange(0, 80);
                int y = world.RRange(0, 30);
                if (tiles[x][y] == Tileset.FLOOR) {
                    tiles[x][y] = Tileset.FLOWER;
                    flowerLoc.add(x);
                    flowerLoc.add(y);
                    floor = false;
                }
            }
        }
    }
    private void MoveAvatar(TETile[][] tile, long Time) {
        //code works just add correct timing;
        //if the avatar goes runs into the water tile, go to another world and collect coins
        for (int j = 0; j < Time; j++) {
            HeadsUp(tile);
            if (StdDraw.hasNextKeyTyped()) {
                ;LightsOut();
            char move = Character.toLowerCase(StdDraw.nextKeyTyped());
                    if (ExitCheck == true) {
                        if (move == 'q' && !StdDraw.hasNextKeyTyped()) {
                            System.exit(0);
                        } else {
                            ExitCheck = false;
                        }
                    }
                    if (move == ':') {
                        ExitCheck = true;
                    }
            if (move == 'w') {
                // move up
                if (tile[x][y+1] != Tileset.WALL) {
                    if (tile[x][y+1] == Tileset.TREE) {
                        n++;
                        if (n == 6) {
                            PriorWorld();
                        }
                    }
                    tile[x][y] = Tileset.FLOOR;
                    y = y + 1;
                    if (tile[x][y] == Tileset.FLOWER) {
                        AnotherWorld();
                    }
                    tile[x][y] = Tileset.AVATARW;
                    ter.renderFrame(tile);
                }
            } else if (move == 's') {
                // move down
                if (tile[x][y-1] != Tileset.WALL) {
                    if (tile[x][y-1] == Tileset.TREE) {
                        n++;
                        if (n == 6) {
                            PriorWorld();
                        }
                    }
                    tile[x][y] = Tileset.FLOOR;
                    y = y - 1;
                    if (tile[x][y] == Tileset.FLOWER) {
                        AnotherWorld();
                    }
                    tile[x][y] = Tileset.AVATAR;
                    ter.renderFrame(tile);
                }
            } else if (move == 'a') {
                if (tile[x-1][y] != Tileset.WALL) {
                    if (tile[x-1][y] == Tileset.TREE) {
                        n++;
                        if (n == 6) {
                            PriorWorld();
                        }
                    }
                    tile[x][y] = Tileset.FLOOR;
                    x = x - 1;
                    if (tile[x][y] == Tileset.FLOWER) {
                        AnotherWorld();
                    }
                    tile[x][y] = Tileset.AVATARA;
                    ter.renderFrame(tile);
                }
            } else if (move == 'd') {
                // move right
                if (tile[x+1][y] != Tileset.WALL) {
                    if (tile[x+1][y] == Tileset.TREE) {
                        n++;
                        if (n == 6) {
                            PriorWorld();
                        }
                    }
                    tile[x][y] = Tileset.FLOOR;
                    x = x + 1;
                    if (tile[x][y] == Tileset.FLOWER) {
                        AnotherWorld();
                    }
                    tile[x][y] = Tileset.AVATARD;
                    ter.renderFrame(tile);
                }
            }
            }
            StdDraw.pause(1);
        }
        StdDraw.show();
    }

    private void MoveAvatarString(TETile[][] tile, String movements) {
        movements = movements.toLowerCase();
        char[] move = movements.toCharArray();
        for (int i = 0; i < move.length; i++){
            HeadsUp(tile);
            if (ExitCheck == true) {
                if (move[i] == 'q' && !StdDraw.hasNextKeyTyped()) {
                    System.exit(0);
                } else {
                    ExitCheck = false;
                }
            }
            if (move[i] == ':') {
                ExitCheck = true;
            } else if  (move[i] == 'w') {
                y+=1;
            } else if (move[i] == 's') {
                y-=1;
            } else if (move[i] == 'a') {
                x-=1;
            }else {
                x+=1;
            }
        }
    }

    private void HeadsUp(TETile[][] tile) {
        int x = (int) Math.round(StdDraw.mouseX());
        int y = (int) Math.round(StdDraw.mouseY());

        String HUI = "Hello World";
        try {
            TETile t = tile[x][y];
            if (t == Tileset.FLOOR) HUI = "Dungeon floor";
            else if (t == Tileset.WALL) HUI = "Cobblestone wall";
            else if (t == Tileset.NOTHING) HUI = "The world beyond ours";
            else if (t == Tileset.TREE) HUI = "Bomb detonation in 3..2.";
            else if (t == Tileset.FLOWER) HUI = "Climb down the ladder";
            else if (t == Tileset.AVATAR || t == Tileset.AVATARA
                    || t == Tileset.AVATARW || t == Tileset.AVATARD) HUI = "A Lonely Skelly";
        } catch (ArrayIndexOutOfBoundsException ignored) {
            HUI = "The world beyond ours";
        }

        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(4.5, 42, HUI.length(), 0.7);
        StdDraw.setPenColor(Color.WHITE);
        Font fontSmall = new Font("Monaco", Font.PLAIN, 10);
        StdDraw.setFont(fontSmall);
        StdDraw.text(4.5, 42, HUI);
        StdDraw.show();
    }
    private void PriorWorld() {
        x = flowerLoc.get(0);
        y = flowerLoc.get(1);
        tiles[flowerLoc.get(0)][flowerLoc.get(1)] = Tileset.AVATAR;
        Clear();
        drawString("Congrats! You Wont Die ",91/2, 41/2);
        StdDraw.pause(2000);
        ter.renderFrame(tiles);
        MoveAvatar(tiles,9223372036854775807L);
    }

    private void drawString(String s, double width, double height) {
        StdDraw.setPenColor(Color.WHITE);
        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width, height, s);
        StdDraw.show();
    }
    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        int width = 91;
        int height = 41;
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 2 + height/3, s);
        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height / 2, "New Game (N)");
        StdDraw.text(width / 2, height / 2 - 2, "Load (L)");
        StdDraw.text(width / 2, height / 2 - 4, "Quit (Q)");
        StdDraw.show();
    }
    public String MySeed() {
        String str = "";
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(fontBig);
        for (int j = 0; j < 300000; j++) {
            StdDraw.pause(1);
            if (StdDraw.hasNextKeyTyped()) {
                char letter = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (letter == 's') {
                break;
            }
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(91 / 2.0, 41 / 2 - 6.7, str.length(), 0.7);
                StdDraw.setPenColor(Color.WHITE);
            str = str + letter;
                StdDraw.text(91 / 2.0,41 / 2 - 6.7, str);
            StdDraw.show();
            }
        }
        return str;
    }

    private void LightsOut() {
    }

    public static void main(String[] args) {
        InteractWorld ract = new InteractWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(91,41);
        ter.renderFrame(ract.tiles);
    }
}
