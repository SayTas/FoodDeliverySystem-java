package org.fooddelivery.storage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class XMLStorage {

    public static void save(Object obj, String filePath) {
        try (XMLEncoder encoder = new XMLEncoder(
                new BufferedOutputStream(new FileOutputStream(filePath)))) {
            encoder.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object load(String filePath) {
        try (XMLDecoder decoder = new XMLDecoder(
                new BufferedInputStream(new FileInputStream(filePath)))) {
            return decoder.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}