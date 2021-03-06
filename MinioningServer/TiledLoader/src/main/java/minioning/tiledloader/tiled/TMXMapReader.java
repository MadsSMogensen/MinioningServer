/*-
 * #%L
 * This file is part of libtiled-java.
 * %%
 * Copyright (C) 2004 - 2016 Thorbjørn Lindeijer <thorbjorn@lindeijer.nl>
 * Copyright (C) 2004 - 2016 Adam Turk <aturk@biggeruniverse.com>
 * Copyright (C) 2016 Mike Thomas <mikepthomas@outlook.com>
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package minioning.tiledloader.tiled;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import minioning.tiledloader.tiled.AnimatedTile;
import minioning.tiledloader.tiled.Map;
import minioning.tiledloader.tiled.MapLayer;
import minioning.tiledloader.tiled.MapObject;
import minioning.tiledloader.tiled.ObjectGroup;
import minioning.tiledloader.tiled.Tile;
import minioning.tiledloader.tiled.TileLayer;
import minioning.tiledloader.tiled.TileSet;
import minioning.tiledloader.tiled.Base64;
import minioning.tiledloader.tiled.BasicTileCutter;
import minioning.tiledloader.tiled.ImageHelper;

/**
 * The standard map reader for TMX files. Supports reading .tmx, .tmx.gz and
 * *.tsx files.
 *
 * @author Thorbjørn Lindeijer
 * @author Adam Turk
 * @author Mike Thomas
 * @version 0.17
 */
public class TMXMapReader {

    public long FLIPPED_HORIZONTALLY_FLAG = 0xFFFFFFFF80000000L;
    public long FLIPPED_VERTICALLY_FLAG = 0xFFFFFFFF40000000L;
    public long FLIPPED_DIAGONALLY_FLAG = 0xFFFFFFFF20000000L;

    private Map map;
    private String xmlPath;
    private String error;
    private final EntityResolver entityResolver = new MapEntityResolver();
    private TreeMap<Integer, TileSet> tilesetPerFirstGid;
    public final TMXMapReaderSettings settings = new TMXMapReaderSettings();
    private final HashMap<String, TileSet> cachedTilesets = new HashMap<>();

    public static final class TMXMapReaderSettings {
        public boolean reuseCachedTilesets = false;
    }

    /**
     * <p>Constructor for TMXMapReader.</p>
     */
    public TMXMapReader() {
    }

    String getError() {
        return error;
    }

    private static String makeUrl(String filename) throws MalformedURLException {
        final String url;
        if (filename.indexOf("://") > 0 || filename.startsWith("file:")) {
            url = filename;
        } else {
            url = new File(filename).toURI().toString();
        }
        return url;
    }

    private void setOrientation(String orientation) {
        try {
            map.setOrientation(Map.Orientation.valueOf(orientation));
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown orientation '" + orientation + "'");
        }
    }

    private void setStaggerAxis(String staggerAxis) {
        try {
            map.setStaggerAxis(Map.StaggerAxis.valueOf(staggerAxis));
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown stagger axis '" + staggerAxis + "'");
        }
    }

    private void setStaggerIndex(String staggerIndex) {
        try {
            map.setStaggerIndex(Map.StaggerIndex.valueOf(staggerIndex));
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown stagger index '" + staggerIndex + "'");
        }
    }

    private static String getAttributeValue(Node node, String attribname) {
        final NamedNodeMap attributes = node.getAttributes();
        String value = null;
        if (attributes != null) {
            Node attribute = attributes.getNamedItem(attribname);
            if (attribute != null) {
                value = attribute.getNodeValue();
            }
        }
        return value;
    }

    private static int getAttribute(Node node, String attribname, int def) {
        final String attr = getAttributeValue(node, attribname);
        if (attr != null) {
            return Integer.parseInt(attr);
        } else {
            return def;
        }
    }

    private static double getDoubleAttribute(Node node, String attribname, double def) {
        final String attr = getAttributeValue(node, attribname);
        if (attr != null) {
            return Double.parseDouble(attr);
        } else {
            return def;
        }
    }

    private <T> T unmarshalClass(Class<T> clazz, Node node) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (T) unmarshaller.unmarshal(node);
    }

    private Image unmarshalImage(Node t, String baseDir) throws IOException {
        Image img = null;

        String source = getAttributeValue(t, "source");

        if (source != null) {
            if (checkRoot(source)) {
                source = makeUrl(source);
            } else {
                source = makeUrl(baseDir + source);
            }
            img = ImageIO.read(new URL(source));
        } else {
            NodeList nl = t.getChildNodes();

            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if ("data".equals(node.getNodeName())) {
                    Node cdata = node.getFirstChild();
                    if (cdata != null) {
                        String sdata = cdata.getNodeValue();
                        char[] charArray = sdata.trim().toCharArray();
                        byte[] imageData = Base64.decode(charArray);
                        img = ImageHelper.bytesToImage(imageData);

                        // Deriving a scaled instance, even if it has the same
                        // size, somehow makes drawing of the tiles a lot
                        // faster on various systems (seen on Linux, Windows
                        // and MacOS X).
                        img = img.getScaledInstance(
                                img.getWidth(null), img.getHeight(null),
                                Image.SCALE_FAST);
                    }
                    break;
                }
            }
        }

        return img;
    }

    private TileSet unmarshalTilesetFile(InputStream in, String filename)
            throws Exception {
        TileSet set = null;
        Node tsNode;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //builder.setErrorHandler(new XMLErrorHandler());
            Document tsDoc = builder.parse(in, ".");

            String xmlPathSave = xmlPath;
            if (filename.indexOf(File.separatorChar) >= 0) {
                xmlPath = filename.substring(0,
                        filename.lastIndexOf(File.separatorChar) + 1);
            }

            NodeList tsNodeList = tsDoc.getElementsByTagName("tileset");

            // There can be only one tileset in a .tsx file.
            tsNode = tsNodeList.item(0);
            if (tsNode != null) {
                set = unmarshalTileset(tsNode);
                if (set.getSource() != null) {
                    System.out.println("Recursive external tilesets are not supported.");
                }
                set.setSource(filename);
            }

            xmlPath = xmlPathSave;
        } catch (SAXException e) {
            error = "Failed while loading " + filename + ": "
                    + e.getLocalizedMessage();
        }

        return set;
    }

    private TileSet unmarshalTileset(Node t) throws Exception {
        String source = getAttributeValue(t, "source");
        String basedir = getAttributeValue(t, "basedir");
        int firstGid = getAttribute(t, "firstgid", 1);

        String tilesetBaseDir = xmlPath;

        if (basedir != null) {
            tilesetBaseDir = basedir; //makeUrl(basedir);
        }

        if (source != null) {
            String filename = tilesetBaseDir + source;
            //if (checkRoot(source)) {
            //    filename = makeUrl(source);
            //}

            TileSet ext = null;

            try {
                InputStream in = new URL(makeUrl(filename)).openStream();
                ext = unmarshalTilesetFile(in, filename);
                setFirstGidForTileset(ext, firstGid);
            } catch (FileNotFoundException fnf) {
                error = "Could not find external tileset file " + filename;
            }

            if (ext == null) {
                error = "Tileset " + source + " was not loaded correctly!";
            }

            return ext;
        } else {
            final int tileWidth = getAttribute(t, "tilewidth", map != null ? map.getTileWidth() : 0);
            final int tileHeight = getAttribute(t, "tileheight", map != null ? map.getTileHeight() : 0);
            final int tileSpacing = getAttribute(t, "spacing", 0);
            final int tileMargin = getAttribute(t, "margin", 0);

            final String name = getAttributeValue(t, "name");

            TileSet set;
            if (settings.reuseCachedTilesets) {
                set = cachedTilesets.get(name);
                if (set != null) {
                    setFirstGidForTileset(set, firstGid);
                    return set;
                }
                set = new TileSet();
                cachedTilesets.put(name, set);
            } else {
                set = new TileSet();
            }

            set.setName(name);
            set.setBaseDir(basedir);
            setFirstGidForTileset(set, firstGid);

            boolean hasTilesetImage = false;
            NodeList children = t.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);

                if (child.getNodeName().equalsIgnoreCase("image")) {
                    if (hasTilesetImage) {
                        System.out.println("Ignoring illegal image element after tileset image.");
                        continue;
                    }

                    String imgSource = getAttributeValue(child, "source");
                    String transStr = getAttributeValue(child, "trans");

                    if (imgSource != null) {
                        // Not a shared image, but an entire set in one image
                        // file. There should be only one image element in this
                        // case.
                        hasTilesetImage = true;

                        // FIXME: importTileBitmap does not fully support URLs
                        String sourcePath = imgSource;
                        if (!new File(imgSource).isAbsolute()) {
                            sourcePath = tilesetBaseDir + imgSource;
                        }

                        if (transStr != null) {
                            if (transStr.startsWith("#")) {
                                transStr = transStr.substring(1);
                            }

                            int colorInt = Integer.parseInt(transStr, 16);
                            Color color = new Color(colorInt);
                            set.setTransparentColor(color);
                        }

                        set.importTileBitmap(sourcePath, new BasicTileCutter(
                                tileWidth, tileHeight, tileSpacing, tileMargin));
                    }
                } else if (child.getNodeName().equalsIgnoreCase("tile")) {
                    Tile tile = unmarshalTile(set, child, tilesetBaseDir);
                    if (!hasTilesetImage || tile.getId() > set.getMaxTileId()) {
                        set.addTile(tile);
                    } else {
                        Tile myTile = set.getTile(tile.getId());
                        myTile.setProperties(tile.getProperties());
                        //TODO: there is the possibility here of overlaying images,
                        //      which some people may want
                    }
                }
            }

            return set;
        }
    }

    private MapObject readMapObject(Node t) throws Exception {
        final String name = getAttributeValue(t, "name");
        final String type = getAttributeValue(t, "type");
        final String gid = getAttributeValue(t, "gid");
        final double x = getDoubleAttribute(t, "x", 0);
        final double y = getDoubleAttribute(t, "y", 0);
        final double width = getDoubleAttribute(t, "width", 0);
        final double height = getDoubleAttribute(t, "height", 0);

        MapObject obj = new MapObject(x, y, width, height);
        obj.setShape(obj.getBounds());
        if (name != null) {
            obj.setName(name);
        }
        if (type != null) {
            obj.setType(type);
        }
        if (gid != null) {
            long tileId = Long.parseLong(gid);
            if (tileId > Integer.MAX_VALUE) {
                // Read out the flags
                // TODO: Save these flags somewhere
                long flippedHorizontally = tileId & FLIPPED_HORIZONTALLY_FLAG;
                long flippedVertically = tileId & FLIPPED_VERTICALLY_FLAG;
                long flippedDiagonally = tileId & FLIPPED_DIAGONALLY_FLAG;

                // Clear the flags
                tileId &= ~(FLIPPED_HORIZONTALLY_FLAG |
                            FLIPPED_VERTICALLY_FLAG |
                            FLIPPED_DIAGONALLY_FLAG);
            }
            Tile tile = getTileForTileGID((int) tileId);
            obj.setTile(tile);
        }

        NodeList children = t.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if ("image".equalsIgnoreCase(child.getNodeName())) {
                String source = getAttributeValue(child, "source");
                if (source != null) {
                    if (!new File(source).isAbsolute()) {
                        source = xmlPath + source;
                    }
                    obj.setImageSource(source);
                }
                break;
            } else if ("ellipse".equalsIgnoreCase(child.getNodeName())) {
                obj.setShape(new Ellipse2D.Double(x, y, width, height));
            } else if ("polygon".equalsIgnoreCase(child.getNodeName()) || "polyline".equalsIgnoreCase(child.getNodeName())) {
                Path2D.Double shape = new Path2D.Double();
                final String pointsAttribute = getAttributeValue(child, "points");
                StringTokenizer st = new StringTokenizer(pointsAttribute, ", ");
                boolean firstPoint = true;
                while (st.hasMoreElements()) {
                    double pointX = Double.parseDouble(st.nextToken());
                    double pointY = Double.parseDouble(st.nextToken());
                    if (firstPoint) {
                        shape.moveTo(x + pointX, y + pointY);
                        firstPoint = false;
                    } else {
                        shape.lineTo(x + pointX, y + pointY);
                    }
                }
                shape.closePath();
                obj.setShape(shape);
                obj.setBounds((Rectangle2D.Double) shape.getBounds2D());
            }
        }

        Properties props = new Properties();
        readProperties(children, props);

        obj.setProperties(props);
        return obj;
    }

    /**
     * Reads properties from amongst the given children. When a "properties"
     * element is encountered, it recursively calls itself with the children of
     * this node. This function ensures backward compatibility with tmx version
     * 0.99a.
     *
     * Support for reading property values stored as character data was added in
     * Tiled 0.7.0 (tmx version 0.99c).
     *
     * @param children the children amongst which to find properties
     * @param props the properties object to set the properties of
     */
    private static void readProperties(NodeList children, Properties props) {
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if ("property".equalsIgnoreCase(child.getNodeName())) {
                final String key = getAttributeValue(child, "name");
                String value = getAttributeValue(child, "value");
                if (value == null) {
                    Node grandChild = child.getFirstChild();
                    if (grandChild != null) {
                        value = grandChild.getNodeValue();
                        if (value != null) {
                            value = value.trim();
                        }
                    }
                }
                if (value != null) {
                    props.setProperty(key, value);
                }
            } else if ("properties".equals(child.getNodeName())) {
                readProperties(child.getChildNodes(), props);
            }
        }
    }

    private Tile unmarshalTile(TileSet set, Node t, String baseDir)
            throws Exception {
        Tile tile = null;
        NodeList children = t.getChildNodes();
        boolean isAnimated = false;

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if ("animation".equalsIgnoreCase(child.getNodeName())) {
                isAnimated = true;
                break;
            }
        }

        try {
            if (isAnimated) {
                tile = unmarshalClass(AnimatedTile.class, t);
            } else {
                tile = unmarshalClass(Tile.class, t);
            }
        } catch (JAXBException e) {
            error = "Failed creating tile: " + e.getLocalizedMessage();
            return tile;
        }

        tile.setTileSet(set);

        readProperties(children, tile.getProperties());

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if ("image".equalsIgnoreCase(child.getNodeName())) {
                Image img = unmarshalImage(child, baseDir);
                tile.setImage(img);
            } else if ("animation".equalsIgnoreCase(child.getNodeName())) {
                // TODO: fill this in once TMXMapWriter is complete
            }
        }

        return tile;
    }

    private MapLayer unmarshalObjectGroup(Node t) throws Exception {
        ObjectGroup og = null;
        try {
            og = unmarshalClass(ObjectGroup.class, t);
        } catch (JAXBException e) {
            // todo: replace with log message
            e.printStackTrace();
            return og;
        }

        final int offsetX = getAttribute(t, "x", 0);
        final int offsetY = getAttribute(t, "y", 0);
        og.setOffset(offsetX, offsetY);

        // Add all objects from the objects group
        NodeList children = t.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if ("object".equalsIgnoreCase(child.getNodeName())) {
                og.addObject(readMapObject(child));
            }
        }

        Properties props = new Properties();
        readProperties(children, props);
        og.setProperties(props);

        return og;
    }

    /**
     * Loads a map layer from a layer node.
     *
     * @param t the node representing the "layer" element
     * @return the loaded map layer
     * @throws Exception
     */
    private MapLayer readLayer(Node t) throws Exception {
        final int layerWidth = getAttribute(t, "width", map.getWidth());
        final int layerHeight = getAttribute(t, "height", map.getHeight());

        TileLayer ml = new TileLayer(layerWidth, layerHeight);

        final int offsetX = getAttribute(t, "x", 0);
        final int offsetY = getAttribute(t, "y", 0);
        final int visible = getAttribute(t, "visible", 1);
        String opacity = getAttributeValue(t, "opacity");

        ml.setName(getAttributeValue(t, "name"));

        if (opacity != null) {
            ml.setOpacity(Float.parseFloat(opacity));
        }

        readProperties(t.getChildNodes(), ml.getProperties());

        for (Node child = t.getFirstChild(); child != null;
                child = child.getNextSibling()) {
            String nodeName = child.getNodeName();
            if ("data".equalsIgnoreCase(nodeName)) {
                String encoding = getAttributeValue(child, "encoding");
                String comp = getAttributeValue(child, "compression");

                if ("base64".equalsIgnoreCase(encoding)) {
                    Node cdata = child.getFirstChild();
                    if (cdata != null) {
                        char[] enc = cdata.getNodeValue().trim().toCharArray();
                        byte[] dec = Base64.decode(enc);
                        ByteArrayInputStream bais = new ByteArrayInputStream(dec);
                        InputStream is;

                        if ("gzip".equalsIgnoreCase(comp)) {
                            final int len = layerWidth * layerHeight * 4;
                            is = new GZIPInputStream(bais, len);
                        } else if ("zlib".equalsIgnoreCase(comp)) {
                            is = new InflaterInputStream(bais);
                        } else if (comp != null && !comp.isEmpty()) {
                            throw new IOException("Unrecognized compression method \"" + comp + "\" for map layer " + ml.getName());
                        } else {
                            is = bais;
                        }

                        for (int y = 0; y < ml.getHeight(); y++) {
                            for (int x = 0; x < ml.getWidth(); x++) {
                                int tileId = 0;
                                tileId |= is.read();
                                tileId |= is.read() << 8;
                                tileId |= is.read() << 16;
                                tileId |= is.read() << 24;

                                setTileAtFromTileId(ml, y, x, tileId);
                            }
                        }
                    }
                } else if ("csv".equalsIgnoreCase(encoding)) {
                    String csvText = child.getTextContent();

                    if (comp != null && !comp.isEmpty()) {
                        throw new IOException("Unrecognized compression method \"" + comp + "\" for map layer " + ml.getName() + " and encoding " + encoding);
                    }

                    String[] csvTileIds = csvText
                            .trim() // trim 'space', 'tab', 'newline'. pay attention to additional unicode chars like \u2028, \u2029, \u0085 if necessary
                            .split("[\\s]*,[\\s]*");

                    if (csvTileIds.length != ml.getHeight() * ml.getWidth()) {
                        throw new IOException("Number of tiles does not match the layer's width and height");
                    }

                    for (int y = 0; y < ml.getHeight(); y++) {
                        for (int x = 0; x < ml.getWidth(); x++) {
                            String sTileId = csvTileIds[x + y * ml.getWidth()];
                            int tileId = Integer.parseInt(sTileId);

                            setTileAtFromTileId(ml, y, x, tileId);
                        }
                    }
                } else {
                    int x = 0, y = 0;
                    for (Node dataChild = child.getFirstChild();
                            dataChild != null;
                            dataChild = dataChild.getNextSibling()) {
                        if ("tile".equalsIgnoreCase(dataChild.getNodeName())) {
                            int tileId = getAttribute(dataChild, "gid", -1);
                            setTileAtFromTileId(ml, y, x, tileId);

                            x++;
                            if (x == ml.getWidth()) {
                                x = 0;
                                y++;
                            }
                            if (y == ml.getHeight()) {
                                break;
                            }
                        }
                    }
                }
            } else if ("tileproperties".equalsIgnoreCase(nodeName)) {
                for (Node tpn = child.getFirstChild();
                        tpn != null;
                        tpn = tpn.getNextSibling()) {
                    if ("tile".equalsIgnoreCase(tpn.getNodeName())) {
                        int x = getAttribute(tpn, "x", -1);
                        int y = getAttribute(tpn, "y", -1);

                        Properties tip = new Properties();

                        readProperties(tpn.getChildNodes(), tip);
                        ml.setTileInstancePropertiesAt(x, y, tip);
                    }
                }
            }
        }

        // This is done at the end, otherwise the offset is applied during
        // the loading of the tiles.
        ml.setOffset(offsetX, offsetY);

        // Invisible layers are automatically locked, so it is important to
        // set the layer to potentially invisible _after_ the layer data is
        // loaded.
        // todo: Shouldn't this be just a user interface feature, rather than
        // todo: something to keep in mind at this level?
        ml.setVisible(visible == 1);

        return ml;
    }

    /**
     * Helper method to set the tile based on its global id.
     *
     * @param ml tile layer
     * @param y y-coordinate
     * @param x x-coordinate
     * @param tileId global id of the tile as read from the file
     */
    private void setTileAtFromTileId(TileLayer ml, int y, int x, int tileId) {
        ml.setTileAt(x, y, getTileForTileGID(tileId));
    }

    /**
     * Helper method to get the tile based on its global id
     *
     * @param tileId global id of the tile
     * @return    <ul><li>{@link Tile} object corresponding to the global id, if
     * found</li><li><code>null</code>, otherwise</li></ul>
     */
    private Tile getTileForTileGID(int tileId) {
        Tile tile = null;
        java.util.Map.Entry<Integer, TileSet> ts = findTileSetForTileGID(tileId);
        if (ts != null) {
            tile = ts.getValue().getTile(tileId - ts.getKey());
        }
        return tile;
    }

    private void buildMap(Document doc) throws Exception {
        Node item, mapNode;

        mapNode = doc.getDocumentElement();

        if (!"map".equals(mapNode.getNodeName())) {
            throw new Exception("Not a valid tmx map file.");
        }

        // Get the map dimensions and create the map
        int mapWidth = getAttribute(mapNode, "width", 0);
        int mapHeight = getAttribute(mapNode, "height", 0);

        if (mapWidth > 0 && mapHeight > 0) {
            map = new Map(mapWidth, mapHeight);
        } else {
            // Maybe this map is still using the dimensions element
            NodeList l = doc.getElementsByTagName("dimensions");
            for (int i = 0; (item = l.item(i)) != null; i++) {
                if (item.getParentNode() == mapNode) {
                    mapWidth = getAttribute(item, "width", 0);
                    mapHeight = getAttribute(item, "height", 0);

                    if (mapWidth > 0 && mapHeight > 0) {
                        map = new Map(mapWidth, mapHeight);
                    }
                }
            }
        }

        if (map == null) {
            throw new Exception("Couldn't locate map dimensions.");
        }

        // Load other map attributes
        String orientation = getAttributeValue(mapNode, "orientation");
        int tileWidth = getAttribute(mapNode, "tilewidth", 0);
        int tileHeight = getAttribute(mapNode, "tileheight", 0);
        int hexsidelength = getAttribute(mapNode, "hexsidelength", 0);
        String staggerAxis = getAttributeValue(mapNode, "staggeraxis");
        String staggerIndex = getAttributeValue(mapNode, "staggerindex");

        if (orientation != null) {
            setOrientation(orientation.toUpperCase());
        } else {
            map.setOrientation(Map.Orientation.ORTHOGONAL);
        }

        if (tileWidth > 0) {
            map.setTileWidth(tileWidth);
        }
        if (tileHeight > 0) {
            map.setTileHeight(tileHeight);
        }
        if (hexsidelength > 0) {
            map.setHexSideLength(hexsidelength);
        }

        if (staggerAxis != null) {
            setStaggerAxis(staggerAxis.toUpperCase());
        }

        if (staggerIndex != null) {
            setStaggerIndex(staggerIndex.toUpperCase());
        }

        // Load properties
        readProperties(mapNode.getChildNodes(), map.getProperties());

        // Load tilesets first, in case order is munged
        tilesetPerFirstGid = new TreeMap<>();
        NodeList l = doc.getElementsByTagName("tileset");
        for (int i = 0; (item = l.item(i)) != null; i++) {
            map.addTileset(unmarshalTileset(item));
        }

        // Load the layers and objectgroups
        for (Node sibs = mapNode.getFirstChild(); sibs != null;
                sibs = sibs.getNextSibling()) {
            if ("layer".equals(sibs.getNodeName())) {
                MapLayer layer = readLayer(sibs);
                if (layer != null) {
                    map.addLayer(layer);
                }
            } else if ("objectgroup".equals(sibs.getNodeName())) {
                MapLayer layer = unmarshalObjectGroup(sibs);
                if (layer != null) {
                    map.addLayer(layer);
                }
            }
        }
        tilesetPerFirstGid = null;
    }

    private Map unmarshal(InputStream in) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        try {
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(entityResolver);
            InputSource insrc = new InputSource(in);
            insrc.setSystemId(xmlPath);
            insrc.setEncoding("UTF-8");
            doc = builder.parse(insrc);
        } catch (SAXException e) {
            // todo: replace with log message
            e.printStackTrace();
            throw new Exception("Error while parsing map file: "
                    + e.toString());
        }

        buildMap(doc);

        return map;
    }

    /**
     * <p>readMap.</p>
     *
     * @param filename a {@link java.lang.String} object.
     * @return a {@link tiled.core.Map} object.
     * @throws java.lang.Exception if any.
     */
    public Map readMap(String filename) throws Exception {
        xmlPath = filename.substring(0,
                filename.lastIndexOf(File.separatorChar) + 1);

        String xmlFile = makeUrl(filename);
        //xmlPath = makeUrl(xmlPath);

        URL url = new URL(xmlFile);
        InputStream is = url.openStream();

        // Wrap with GZIP decoder for .tmx.gz files
        if (filename.endsWith(".gz")) {
            is = new GZIPInputStream(is);
        }

        Map unmarshalledMap = unmarshal(is);
        unmarshalledMap.setFilename(filename);

        map = null;

        return unmarshalledMap;
    }

    /**
     * <p>readMap.</p>
     *
     * @param in a {@link java.io.InputStream} object.
     * @return a {@link tiled.core.Map} object.
     * @throws java.lang.Exception if any.
     */
    public Map readMap(InputStream in) throws Exception {
        xmlPath = makeUrl(".");

        Map unmarshalledMap = unmarshal(in);

        //unmarshalledMap.setFilename(xmlFile)
        //
        return unmarshalledMap;
    }

    /**
     * <p>readTileset.</p>
     *
     * @param filename a {@link java.lang.String} object.
     * @return a {@link tiled.core.TileSet} object.
     * @throws java.lang.Exception if any.
     */
    public TileSet readTileset(String filename) throws Exception {
        String xmlFile = filename;

        xmlPath = filename.substring(0,
                filename.lastIndexOf(File.separatorChar) + 1);

        xmlFile = makeUrl(xmlFile);
        xmlPath = makeUrl(xmlPath);

        URL url = new URL(xmlFile);
        return unmarshalTilesetFile(url.openStream(), filename);
    }

    /**
     * <p>readTileset.</p>
     *
     * @param in a {@link java.io.InputStream} object.
     * @return a {@link tiled.core.TileSet} object.
     * @throws java.lang.Exception if any.
     */
    public TileSet readTileset(InputStream in) throws Exception {
        return unmarshalTilesetFile(in, ".");
    }

    /**
     * <p>accept.</p>
     *
     * @param pathName a {@link java.io.File} object.
     * @return a boolean.
     */
    public boolean accept(File pathName) {
        try {
            String path = pathName.getCanonicalPath();
            if (path.endsWith(".tmx") || path.endsWith(".tsx")
                    || path.endsWith(".tmx.gz")) {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }

    private class MapEntityResolver implements EntityResolver {

        @Override
        public InputSource resolveEntity(String publicId, String systemId) {
            if (systemId.equals("http://mapeditor.org/dtd/1.0/map.dtd")) {
                return new InputSource(TMXMapReader.class.getResourceAsStream(
                        "resources/map.dtd"));
            }
            return null;
        }
    }

    /**
     * This utility function will check the specified string to see if it starts
     * with one of the OS root designations. (Ex.: '/' on Unix, 'C:' on Windows)
     *
     * @param filename a filename to check for absolute or relative path
     * @return <code>true</code> if the specified filename starts with a
     * filesystem root, <code>false</code> otherwise.
     */
    public static boolean checkRoot(String filename) {
        File[] roots = File.listRoots();

        for (File root : roots) {
            try {
                String canonicalRoot = root.getCanonicalPath().toLowerCase();
                if (filename.toLowerCase().startsWith(canonicalRoot)) {
                    return true;
                }
            } catch (IOException e) {
                // Do we care?
            }
        }

        return false;
    }

    /**
     * Get the tile set and its corresponding firstgid that matches the given
     * global tile id.
     *
     *
     * @param gid a global tile id
     * @return the tileset containing the tile with the given global tile id, or
     * <code>null</code> when no such tileset exists
     */
    private java.util.Map.Entry<Integer, TileSet> findTileSetForTileGID(int gid) {
        return tilesetPerFirstGid.floorEntry(gid);
    }

    private void setFirstGidForTileset(TileSet tileset, int firstGid) {
        tilesetPerFirstGid.put(firstGid, tileset);
    }
}
