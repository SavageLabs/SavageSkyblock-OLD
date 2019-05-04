package net.prosavage.savageskyblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.jnbt.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Schematic {

    private byte[] blocks;
    private byte[] data;
    private short width;
    private short length;
    private short height;
    private List<Tag> tileEntities;
    private List<Tag> entities;

    public Schematic(byte[] blocks, byte[] data, short width, short length, short height, List<Tag> tileEntities, List<Tag> entities) {
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.length = length;
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
     * @return the length
     */
    public short getLength() {
        return length;
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

        short length = getLength();
        short width = getWidth();
        short height = getHeight();

        loc.subtract(width / 2, height / 2, length / 2); // Centers the schematic

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
        //Tile Entities
        for (Tag tag : tileEntities) {
            if (!(tag instanceof CompoundTag))
                continue;
            CompoundTag t = (CompoundTag) tag;
            Map<String, Tag> tags = t.getValue();

            int x = getChildTag(tags, "x", IntTag.class).getValue();
            int y = getChildTag(tags, "y", IntTag.class).getValue();
            int z = getChildTag(tags, "z", IntTag.class).getValue();

            String id = getChildTag(tags, "id", StringTag.class).getValue();
            if (id.equalsIgnoreCase("Chest")) {
                List<Tag> items = getChildTag(tags, "Items", ListTag.class).getValue();
                Block block = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ()).getBlock();
                if (block.getState() instanceof Chest) {
                    Chest chest = (Chest) block.getState();
                    for (Tag item : items) {
                        if (!(item instanceof CompoundTag))
                            continue;
                        Map<String, Tag> itemtag = ((CompoundTag) item).getValue();
                        byte slot = getChildTag(itemtag, "Slot", ByteTag.class).getValue();
                        String name = (getChildTag(itemtag, "id", StringTag.class).getValue()).toLowerCase().replace("minecraft:", "");
                        Byte amount = getChildTag(itemtag, "Count", ByteTag.class).getValue();
                        Material material = Material.getMaterial(name.toUpperCase());
                        if(material != null){
                            chest.getBlockInventory().setItem(slot, new ItemStack(material, amount));
                        }
                    }
                    chest.update();
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
        List<Tag> TileEntities = getChildTag(schematic, "TileEntities", ListTag.class).getValue();
        List<Tag> entities = getChildTag(schematic, "Entities", ListTag.class).getValue();
        return new Schematic(blocks, blockData, width, length, height, TileEntities, entities);
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
