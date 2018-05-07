/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.varwatchsql.matching;

import com.google.inject.Inject;
import com.ikmb.varwatchcommons.entities.Contact;
import com.ikmb.varwatchcommons.entities.ExternalMatchInformation;
import com.ikmb.varwatchcommons.entities.Gene;
import com.ikmb.varwatchcommons.entities.HPOTerm;
import com.ikmb.varwatchcommons.entities.InternalMatchInformation;
import com.ikmb.varwatchcommons.entities.MatchInformation;
import com.ikmb.varwatchcommons.entities.Variant;
import com.ikmb.varwatchsql.auth.user.UserBuilder;
import com.ikmb.varwatchsql.auth.user.UserSQL;
import com.ikmb.varwatchsql.data.gene.GeneSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantBuilder;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import com.ikmb.varwatchsql.data.gene.GeneBuilder;
import com.ikmb.varwatchsql.data.hpo.HPOTermBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author broder
 */
public class MatchBuilder {

    @Inject
    private VariantBuilder variantBuilder;

    @Inject
    private HPOTermBuilder hpoTermBuilder;

    @Inject
    private GeneBuilder geneBuilder;

    @Inject
    private UserBuilder userBuilder;

    private Long queryVariantId;

    private boolean identicalMatch;
    private Variant matchedVariant;
    private List<HPOTerm> hpoTerms;
    private List<Gene> genes;
    private Contact contact;

    //externalInfos
    private String accIdentifier;
    private String database;
    private Double hpoDist;
    private Long statusId;
    private Long datasetId;
    private Boolean acknowledged = false;
    private Long id;
    private String matching_type;

    public MatchBuilder isIdenticalMatch() {
        identicalMatch = true;
        return this;
    }

    public MatchBuilder isNotIdenticalMatch() {
        identicalMatch = false;
        return this;
    }

    public MatchBuilder withVariantSql(VariantSQL variant) {
        matchedVariant = variantBuilder.withSQLVariant(variant).build();
        return this;
    }

    public MatchBuilder withGenesSql(List<GeneSQL> genes) {
        genes = new ArrayList<>();
        for (GeneSQL curGene : genes) {
            this.genes.add(geneBuilder.withGeneSql(curGene).build());
        }
        return this;
    }

    public MatchBuilder withUserSql(UserSQL user) {
        this.contact = userBuilder.withContactSql(user).buildMatchContact();
        return this;
    }

    public MatchInformation buildInternal() {
        InternalMatchInformation matchInfo = new InternalMatchInformation();
        matchInfo.setDatasetId(datasetId);
        matchInfo.setDatabase("VarWatch");
        matchInfo.setContact(contact);
        matchInfo.setHpoTerms(hpoTerms);
//        matchInfo.setIdenticalMatch(identicalMatch);
        matchInfo.setMatchedVariant(matchedVariant);
        matchInfo.setGenes(genes);
        matchInfo.setQueryVariantId(queryVariantId);
        matchInfo.setHpoDist(hpoDist);
        matchInfo.setId(id);
        matchInfo.setStatusId(statusId);
        matchInfo.setAcknowledged(acknowledged);
        matchInfo.setMatchingType(matching_type);
        return matchInfo;
    }

    public MatchBuilder withQueryVariantId(Long variantId) {
        this.queryVariantId = variantId;
        return this;
    }

    public MatchBuilder withDatabase(String databaseName) {
        database = databaseName;
        return this;
    }

    public MatchBuilder withAccIdentifier(String accIdentifier) {
        this.accIdentifier = accIdentifier;
        return this;
    }

    public MatchInformation buildExternal() {
        ExternalMatchInformation matchInfo = new ExternalMatchInformation();
        matchInfo.setDatasetId(datasetId);
        matchInfo.setDatabase(database);
        matchInfo.setAccIdentifier(accIdentifier);
        matchInfo.setQueryVariantId(queryVariantId);
        matchInfo.setHpoDist(hpoDist);
        matchInfo.setId(id);
        matchInfo.setStatusId(statusId);
        matchInfo.setAcknowledged(acknowledged);
        matchInfo.setIsIdentical(identicalMatch);
        matchInfo.setMatchingType(matching_type);
        return matchInfo;
    }

    public MatchBuilder withGenes(List<Gene> genes) {
        genes = new ArrayList<>();
        this.genes = genes;
        return this;
    }

    public MatchBuilder withHpoTermsSql(List<HPOTerm> phenotypes) {
        this.hpoTerms = new ArrayList<>();
        this.hpoTerms = phenotypes;
        return this;
    }

    public MatchBuilder withMatch(MatchVariantSQL matchVariant) {
        MatchSQL match = matchVariant.getMatch();
//        identicalMatch = match.getIdentical();
        hpoDist = match.getHpoDist();
        this.acknowledged = matchVariant.getNotified();
        Set<MatchVariantSQL> variants = match.getVariants();

        this.statusId = matchVariant.getVariantStatus().getId();
        this.id = matchVariant.getId();
        this.matching_type = match.getMatch_type();
        return this;
    }

    public MatchBuilder withDatasetId(Long id) {
        this.datasetId = id;
        return this;
    }
}
