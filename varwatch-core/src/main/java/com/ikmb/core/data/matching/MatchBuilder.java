/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.data.matching;

import com.google.inject.Inject;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserBuilder;
import com.ikmb.core.data.gene.Gene;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.varwatchcommons.entities.Contact;
import com.ikmb.core.varwatchcommons.entities.ExternalMatchInformation;
import com.ikmb.core.varwatchcommons.entities.InternalMatchInformation;
import com.ikmb.core.varwatchcommons.entities.MatchInformation;
import com.ikmb.core.data.variant.VariantBuilder;
import com.ikmb.core.data.hpo.HPOTermBuilder;
import com.ikmb.core.data.variant.Variant;
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

//    @Inject
//    private GeneBuilder geneBuilder;
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

    public MatchBuilder withVariantSql(Variant variant) {
        matchedVariant = variantBuilder.withVariant(variant).buildSql();
        return this;
    }

    public MatchBuilder withGenesSql(List<Gene> genes) {
        this.genes = genes;
        return this;
    }

    public MatchBuilder withUserSql(User user) {
        this.contact = userBuilder.withUser(user).buildMatchContact();
        return this;
    }

    public MatchInformation buildInternal() {
        InternalMatchInformation matchInfo = new InternalMatchInformation();
        matchInfo.setDatasetId(datasetId);
        matchInfo.setDatabase("VarWatch");
        matchInfo.setContact(contact);
        matchInfo.setHpoTerms(hpoTerms);
//        matchInfo.setIdenticalMatch(identicalMatch);
        com.ikmb.core.varwatchcommons.entities.Variant variant = new VariantBuilder().withVariant(matchedVariant).build();
        matchInfo.setMatchedVariant(variant);
//        matchInfo.setGenes(genes);
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
//        genes = new ArrayList<>();
        this.genes = genes;
        return this;
    }

    public MatchBuilder withHpoTermsSql(List<HPOTerm> phenotypes) {
        this.hpoTerms = new ArrayList<>();
        this.hpoTerms = phenotypes;
        return this;
    }

    public MatchBuilder withMatch(MatchVariant matchVariant) {
        Match match = matchVariant.getMatch();
//        identicalMatch = match.getIdentical();
        hpoDist = match.getHpoDist();
        this.acknowledged = matchVariant.getNotified();
        Set<MatchVariant> variants = match.getVariants();

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
