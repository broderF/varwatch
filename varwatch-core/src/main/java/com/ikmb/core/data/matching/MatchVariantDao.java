/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.matching;

import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.dataset.DatasetHGMD;
import com.ikmb.core.data.variant.Variant;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public interface MatchVariantDao {


    public Match get(Long matchStatusID);

    public List<Variant> getSimilarVariants(Variant queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) ;

    public List<Match> persistMatches(List<Match> matches);

    public List<DatasetHGMD> getSimilarHGMDVariants(Variant queryVariant, boolean geneBool, boolean pathwayBool, boolean familyBool) ;

    public List<DatasetHGMD> getIdenticalHGMDVariants(Variant variant) ;

    public List<Variant> getIdenticalVariants(Variant variant) ;

    public boolean remove(Match match);

    public List<DatasetHGMD> getSimilarHGMDVariantsByGene(Variant variant);

    public List<Variant> getSimilarVWVariantsByGene(Variant variant);

    public List<DatasetHGMD> getSimilarHGMDVariantsByFamily(Variant queryVariant);
    
    public List<Variant> getSimilarVWVariantsByFamily(Variant queryVariant) ;

    public boolean persistVWMatch(Match match);

    public List<MatchVariant> getNotNotifiedMatchesByUser(User user) ;

    public void update(MatchVariant varMatch);
    
    List<MatchVariant> getMatchesInInterval(User user, DateTime lastReport, DateTime nextCreationDate) ;

    MatchVariant getMatchVariant(Long curMatch) ;

    public List<Match> getVarWatchMatchesByVariantId(Long id) ;

    public Match getMatchById(Long matchId) ;

    public List<Variant> getVariantsWithSameAminoAcidExchange(Variant variant) ;

    List<DatasetHGMD> getHgmdVariantsWithSameAminoAcidExchange(Variant variant) ;

    public boolean isDuplicatedMatch(Match curMatch);

    public static String getSetString(boolean bool, Set<Integer> ids, String inIdent, boolean first) {
        String returnString = "";
        if (!ids.isEmpty()) {
            if (!first) {
                returnString += " AND ";
            }
            returnString += inIdent + " " + getInORNotIn(bool) + " (";
            Iterator<Integer> iter = ids.iterator();
            if (iter.hasNext()) {
                Integer id = iter.next();
                returnString += id;
                while (iter.hasNext()) {
                    id = iter.next();
                    returnString += "," + id;
                }
            }
            returnString += ")";
        }
        return returnString;
    }

    public static String getInORNotIn(boolean bool) {
        if (bool) {
            return "IN";
        } else {
            return "NOT IN";
        }
    }
}
