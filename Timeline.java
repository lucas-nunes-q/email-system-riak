/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.facom39701.riakapp1.model;

import java.util.ArrayList;

/**
 *
 * @author docker
 */
public class Timeline {
    private String owner ;
    private TimelineType type ;
    private ArrayList < String > messages ;
    public Timeline () {
    }
    
    public Timeline ( final String owner , final TimelineType type ) {
        this . owner = owner ;
        this . type = type ;
        this . messages = new ArrayList < String >();
    }
    
    public String getOwner () {
        return owner ;
    }
    
    public void setOwner ( final String owner ) {
        this . owner = owner ;
    }
    
    public TimelineType getType () {
        return type ;
    }
    
    public void setType ( final TimelineType type ) {
        this . type = type ;
    }
    
    public ArrayList < String > getMessages () {
        return messages ;
    }
}
