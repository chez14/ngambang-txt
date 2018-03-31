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

/**
 *
 * @author hayashi
 */
public class TextDocument {
    ArrayList<String> authors;
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
    
    public void load(String location) throws IOException{
        ZipFile zif = new ZipFile(location);
        this.location = location;
        BufferedReader bDocInfo = new BufferedReader(
                new InputStreamReader(zif.getInputStream(zif.getEntry(f_docInfo)))
        );
        BufferedReader bDocument = new BufferedReader(
                new InputStreamReader(zif.getInputStream(zif.getEntry(f_document)))
        );
        
        //load bunch of authors
        authors = new ArrayList<>();
        bDocInfo.lines().forEach(author -> {
            authors.add(author.trim());
        });
        
        //load data
        bDocument.lines().forEach(data->{
            this.document += data;
        });
    }
    
    public void save(String location) throws FileNotFoundException, IOException {
        if(location == null || location.isEmpty())
            throw new IllegalArgumentException("Location is not set");
        FileOutputStream fout = new FileOutputStream(location);
        ZipOutputStream zout = new ZipOutputStream(fout);
        
        //writing document first
        ZipEntry docInfo = new ZipEntry(f_docInfo);
        zout.putNextEntry(docInfo);
        if(!authors.contains(UserSetting.authorName)){
            authors.add(UserSetting.authorName);
        }
        authors.forEach(author->{
            try {
                zout.write((authors + "\n").getBytes());
            } catch (IOException ex) {
            }
        });
        zout.closeEntry();
        
        ZipEntry docu = new ZipEntry(f_document);
        zout.putNextEntry(docu);
        zout.write(this.document.getBytes());
        zout.closeEntry();
        
        zout.close();
        fout.close();
    }
    
    public void save() throws IOException {
        this.save(location);
    }
    
    public void load() throws IOException{
        this.load(location);
    }
}
