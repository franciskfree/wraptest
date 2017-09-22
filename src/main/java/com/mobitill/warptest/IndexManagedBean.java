/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.warptest;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author franc
 */
@Named(value = "indexManagedBean")
@ViewScoped
public class IndexManagedBean implements Serializable{

    private static final long serialVersionUID = -1115170250789856183L;
    
    private String value;
    private String value2;

    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {
    }
    
    public void indexAction(){
        setValue2(getValue());
        System.out.println("was clicked");
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
    
}
