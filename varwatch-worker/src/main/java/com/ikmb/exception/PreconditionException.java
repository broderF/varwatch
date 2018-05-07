/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.exception;

/**
 *
 * @author bfredrich
 */
public class PreconditionException extends Exception {

    public PreconditionException(String precondition_not_fullfilled) {
        super(precondition_not_fullfilled);
    }

}
