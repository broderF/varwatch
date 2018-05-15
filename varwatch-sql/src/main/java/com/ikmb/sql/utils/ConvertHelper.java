/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.sql.utils;

import com.ikmb.core.data.variant.Variant;
import com.ikmb.core.varwatchcommons.entities.VWVariant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bfredrich
 */
public class ConvertHelper {

    public static VWVariant convertVariantFromSQL(Variant curVar) {
        VWVariant newVar = new VWVariant();
        newVar.setAlternateBases(curVar.getAlternateBase());
        newVar.setReferenceBases(curVar.getReferenceBase());
        newVar.setReferenceName(curVar.getChromosomeName());
        newVar.setStart(curVar.getChromosomePos());
        return newVar;
    }

    public static List<VWVariant> convertVariantsFromSQL(List<Variant> variants) {
        List<VWVariant> response = new ArrayList<VWVariant>();
        for (Variant curVar : variants) {
            VWVariant newVar = new VWVariant();
            newVar.setAlternateBases(curVar.getAlternateBase());
            newVar.setReferenceBases(curVar.getReferenceBase());
            newVar.setReferenceName(curVar.getChromosomeName());
            newVar.setStart(curVar.getChromosomePos());
            response.add(newVar);
        }
        return response;
    }

    public static Variant convertVariantToSQL(VWVariant curVar) {
        Variant newVar = new Variant();
        newVar.setAlternateBase(curVar.getAlternateBases());
        newVar.setReferenceBase(curVar.getReferenceBases());
        newVar.setChromosomeName(curVar.getReferenceName());
        newVar.setChromosomePos(curVar.getStart());
        return newVar;
    }
}
