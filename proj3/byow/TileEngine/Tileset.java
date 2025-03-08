package byow.TileEngine;

import java.awt.Color;
import java.util.Random;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    private static final Color brown = new Color(104,90,83);
    public static final TETile AVATAR = new TETile('@', Color.white, brown, "you", "skeleton_walkS.png");
    public static final TETile AVATARA = new TETile('@', Color.white, brown, "you",
            "skeleton_walkA.png");
    public static final TETile AVATARD = new TETile('@', Color.white, brown, "you",
            "skeleton_walkD.png");
    public static final TETile AVATARW = new TETile('@', Color.white, brown, "you",
            "skeleton_walkW.png");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall", "wall_mid.png");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor", "floor_7.png");
    public static final TETile NOTHING = new TETile(' ', Color.BLACK, Color.BLACK, "Custom Tile");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, brown, "ladder", "floor_ladder.png");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "bomb", "bomb_f1.png");

    }



