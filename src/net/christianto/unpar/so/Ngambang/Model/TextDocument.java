/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.christianto.unpar.so.Ngambang.Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import net.christianto.unpar.so.Ngambang.Exception.InvalidDocFormatException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Gunawan Christianto
 */
public class TextDocument {

    public ArrayList<String> authors;
    public String document = "";
    public String location;

    final String f_docInfo = "docinfo.json";
    final String f_document = "document.txt";

    public TextDocument(String location) {
        this.location = location;
    }

    public TextDocument() {
        this.location = "";
    }

    /**
     * Load selected file as this object
     *
     * @param location
     * @throws IOException
     * @throws ParseException
     * @throws InvalidDocFormatException
     */
    public void load(String location) throws IOException, ParseException, InvalidDocFormatException {
        ZipFile zif = new ZipFile(location);
        this.location = location;
        if (zif.getEntry(f_docInfo) == null || zif.getEntry(f_document) == null) {
            throw new InvalidDocFormatException("The file required are not available.");
        }
        BufferedReader bDocInfo = new BufferedReader(
                new InputStreamReader(zif.getInputStream(zif.getEntry(f_docInfo)))
        );
        BufferedReader bDocument = new BufferedReader(
                new InputStreamReader(zif.getInputStream(zif.getEntry(f_document)))
        );

        //load bunch of authors
        authors = new ArrayList<>();
        JSONArray author = (JSONArray) (new JSONParser()).parse(bDocInfo);
        this.authors.addAll(author);
        //load data
        bDocument.lines().forEach(data -> {
            this.document += data;
        });
    }

    /**
     * Save this object as a file on location
     *
     * @param location
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void save(String location) throws FileNotFoundException, IOException {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location is not set");
        }
        FileOutputStream fout = new FileOutputStream(location);
        ZipOutputStream zout = new ZipOutputStream(fout);

        //writing document first
        ZipEntry docInfo = new ZipEntry(f_docInfo);
        zout.putNextEntry(docInfo);
        if (authors == null) {
            authors = new ArrayList<>();
        }
        if (!authors.contains(UserSetting.authorName)) {
            authors.add(UserSetting.authorName);
        }
        JSONArray authors = new JSONArray();
        authors.addAll(this.authors);
        zout.write(authors.toJSONString().getBytes());
        zout.closeEntry();

        ZipEntry docu = new ZipEntry(f_document);
        zout.putNextEntry(docu);
        zout.write(this.document.getBytes());
        zout.closeEntry();

        zout.close();
        fout.close();
    }

    /**
     * Save this file to saved location.
     *
     * @throws IOException
     */
    public void save() throws IOException {
        this.save(location);
    }

    /**
     * Load document from saved location
     *
     * @throws IOException
     * @throws ParseException
     * @throws InvalidDocFormatException
     */
    public void load() throws IOException, ParseException, InvalidDocFormatException {
        this.load(location);
    }
}
