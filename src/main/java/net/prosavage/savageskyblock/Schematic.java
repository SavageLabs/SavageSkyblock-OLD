package net.prosavage.savageskyblock;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Schematic {

    private byte[] blocks;
    private byte[] data;
    private short width;
    private short lenght;
    private short height;
    private List<Tag> tileEntities;
    List<Tag> entities;

    public Schematic(byte[] blocks, byte[] data, short width, short lenght, short height, List<Tag> tileEntities, List<Tag> entities) {
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.lenght = lenght;
        this.height = height;
        this.tileEntities = tileEntities;
        this.entities = entities;
    }

    /**
     * @return the blocks
     */
    public byte[] getBlocks() {
        return blocks;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @return the width
     */
    public short getWidth() {
        return width;
    }

    /**
     * @return the lenght
     */
    public short getLenght() {
        return lenght;
    }

    /**
     * @return the height
     */
    public short getHeight() {
        return height;
    }

    public void pasteSchematic(Location loc) {
        byte[] blocks = getBlocks();
        byte[] blockData = getData();

        short length = getLenght();
        short width = getWidth();
        short height = getHeight();

        loc.subtract(width / 2, height/2, length / 2); // Centers the schematic

        //LoadBlocks
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    Block block = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ()).getBlock();
                    block.setTypeIdAndData(blocks[index], blockData[index], true);
                }
            }
        }

    }

    public static Schematic loadSchematic(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        NBTInputStream nbtStream = new NBTInputStream(stream);

        CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
        stream.close();
        nbtStream.close();
        Map<String, Tag> schematic = schematicTag.getValue();

        if (!schematic.containsKey("Blocks")) {
            throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
        }

        short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
        short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
        short height = getChildTag(schematic, "Height", ShortTag.class).getValue();

        String materials = getChildTag(schematic, "Materials", StringTag.class).getValue();
        if (!materials.equals("Alpha")) {
            throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
        }

        byte[] blocks = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
        byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class).getValue();
        List<Tag> tileEntities = getChildTag(schematic, "TileEntities", ListTag.class).getValue();
        List<Tag> entities = getChildTag(schematic, "Entities", ListTag.class).getValue();
        return new Schematic(blocks, blockData, width, length, height, tileEntities, entities);
    }

    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
}
