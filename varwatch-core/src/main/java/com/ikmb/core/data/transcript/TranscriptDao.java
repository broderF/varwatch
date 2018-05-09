/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.transcript;

/**
 *
 * @author broder
 */
public interface TranscriptDao {

    public Transcript getByName(String transcriptName);

}
