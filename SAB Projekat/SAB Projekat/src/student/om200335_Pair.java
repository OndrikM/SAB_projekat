/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import rs.etf.sab.operations.PackageOperations.Pair;

/**
 *
 * @author f1
 */
public class om200335_Pair<A,B> implements Pair<A,B> {
    private A first;
    private B second;

    public om200335_Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public A getFirstParam() {
        return this.first;
    }

    @Override
    public B getSecondParam() {
        return this.second;
    }
    
     @Override
    public String toString() {
        return "("+this.first + " , " + this.second+")";
    }
    
}
