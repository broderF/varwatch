/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.utils;

import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bfredrich
 */
public class ConvertHelper {

    public static VWVariant convertVariantFromSQL(VariantSQL curVar) {
        VWVariant newVar = new VWVariant();
        newVar.setAlternateBases(curVar.getAlternateBase());
        newVar.setReferenceBases(curVar.getReferenceBase());
        newVar.setReferenceName(curVar.getChromosomeName());
        newVar.setStart(curVar.getChromosomePos());
        return newVar;
    }

    public static List<VWVariant> convertVariantsFromSQL(List<VariantSQL> variants) {
        List<VWVariant> response = new ArrayList<VWVariant>();
        for (VariantSQL curVar : variants) {
            VWVariant newVar = new VWVariant();
            newVar.setAlternateBases(curVar.getAlternateBase());
            newVar.setReferenceBases(curVar.getReferenceBase());
            newVar.setReferenceName(curVar.getChromosomeName());
            newVar.setStart(curVar.getChromosomePos());
            response.add(newVar);
        }
        return response;
    }

    public static VariantSQL convertVariantToSQL(VWVariant curVar) {
        VariantSQL newVar = new VariantSQL();
        newVar.setAlternateBase(curVar.getAlternateBases());
        newVar.setReferenceBase(curVar.getReferenceBases());
        newVar.setChromosomeName(curVar.getReferenceName());
        newVar.setChromosomePos(curVar.getStart());
        return newVar;
    }
}
