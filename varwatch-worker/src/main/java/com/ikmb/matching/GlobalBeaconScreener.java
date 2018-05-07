package com.ikmb.matching;

import com.google.inject.Inject;
import com.ikmb.matching.VarWatchScreenerNew.MatchType;
import com.ikmb.varwatchsql.entities.DatasetVWSQL;
import com.ikmb.varwatchsql.matching.MatchSQL;
import com.ikmb.varwatchsql.data.reference_db.RefDatabaseSQL;
import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
//import com.ikmb.varwatchsql.entities.ExternalVariantSQL;
import com.ikmb.varwatchsql.matching.MatchVariantDataManager;
import com.ikmb.varwatchsql.matching.MatchVariantSQL;
import com.ikmb.varwatchsql.status.variant.VariantStatusManager;
import java.net.URI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.filter.LoggingFilter;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bfredrich
 */
class GlobalBeaconScreener implements DatabaseScreener {

    private Connection _conn = null;
    private List<MatchSQL> _matches = new ArrayList<MatchSQL>();
    private DatasetVWSQL _dataset;
    private RefDatabaseSQL _database;
    private RefDatabaseSQL vwDatabase;

    @Inject
    private MatchVariantDataManager matchDataManager;

    @Inject
    private VariantStatusManager variantStatusManager;

    @Override
    public void initialize(RefDatabaseSQL database, DatasetVWSQL dataset) {
        _database = database;
        _dataset = dataset;
    }

    @Override
    public void setVWDatabase(RefDatabaseSQL vwDatabase) {
        this.vwDatabase = vwDatabase;
    }

    @Override
    public DatasetVWSQL getDataset() {
        return _dataset;
    }

    @Override
    public RefDatabaseSQL getDatabase() {
        return _database;
    }

    @Override
    public void run() {
//        List<VariantSQL> variants = ConvertHelper.convertVariantsFromSQL(new ArrayList<VariantSQL>(_dataset.getVariants()));
//        if (!_database.getAssembly().equals(VWConfiguration.STANDARD_COORDS)) {
//            variants = CrossmapHelper.mapVariantsFromAssembly(variants, VWConfiguration.STANDARD_COORDS, _database.getAssembly());
//        }
        for (VariantSQL variant : _dataset.getVariants()) {
            String chromosome = variant.getChromosomeName();
//            Integer position = variant.getChromosomePos() - 1; //beacons are zero based
            Integer position = variant.getChromosomePos(); //beacons are zero based
            String dataset = _database.getPath();
            String altBase = variant.getAlternateBase();
            String refBase = variant.getReferenceBase();

            try {
//                if (!_database.getAssembly().equals(VWConfiguration.STANDARD_COORDS)) {
//                    List<VWVariant> tmpVariants = CrossmapHelper.mapVariant(ConvertHelper.convertVariantFromSQL(variant), _database.getAssembly());
//                    if (!tmpVariants.isEmpty()) {
//                        VWVariant tmpVar = tmpVariants.get(0);
//                        position = tmpVar.getStart();
//                        chromosome = tmpVar.getReferenceName();
//                        altBase = tmpVar.getAlternateBases();
//                    } else {
//                        continue;
//                    }
//                }
                System.out.println("------------------------------");
                for (int i = 0; i < 1; i++) {
                    URI build = UriBuilder.fromUri("https://beacon-network.org/api/responses").build();
                    Logger logger = Logger.getLogger(getClass().getName());

                    JerseyClient client = JerseyClientBuilder.createClient();

                    if (altBase.equals("-")) {
                        altBase = "D";
                    }
                    client.register(new LoggingFilter());
                    System.out.println("chrom=" + chromosome + ";pos=" + position + ";assembly=GrCh38;beacon=" + _database.getPath() + "allele=" + altBase);
//                    Response entity = target.queryParam("chrom", chromosome).queryParam("pos", position).queryParam("reference", "GRCh38").queryParam("beacon", _database.getPath()).queryParam("allele", altBase).request().get();
                    JerseyWebTarget target = client.target(build).queryParam("chrom", chromosome).queryParam("pos", position).queryParam("ref", "GRCh38").queryParam("beacon", _database.getPath()).queryParam("allele", altBase).queryParam("referenceAllele", refBase);
                    System.out.println(target.getUri().toString());
                    Response entity = target.request().get();
                    String response = entity.readEntity(String.class);
                    System.out.println(response);
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObj = jsonArray.getJSONObject(0);
                    if (jsonObj.isNull("response")) {
                        try {
                            Thread.sleep(1000);                 //1000 milliseconds is one second.
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    } else if (jsonObj.getBoolean("response")) {
                        createMatch(variant);
                        break;
                    }
                    break;
                }
                System.out.println("------------------------------");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

//        matchDataManager.persistMatches(getMatches());
//        variantStatusManager.addMatches(getMatches(), VariantStatusBuilder.VariantStatusMessage.MATCHED_TO_BEACON);
    }

    @Override
    public List<MatchSQL> getMatches() {
        return _matches;
    }

    public static void main(String[] args) {
        GlobalBeaconScreener screener = new GlobalBeaconScreener();
        DatasetVWSQL ds = new DatasetVWSQL();
        VariantSQL variant = new VariantSQL();
        variant.setAlternateBase("T");
        variant.setChromosomeName("1");
        variant.setChromosomePos(1014143);
        variant.setReferenceBase("C");
        Set<VariantSQL> variants = new HashSet<>();
        variants.add(variant);
        ds.setVariants(variants);
        RefDatabaseSQL db = new RefDatabaseSQL();
        db.setPath("tsri-clinvar");
        db.setAssembly("GRCh38");
        screener.initialize(db, ds);
//        for (int i = 0; i < 50; i++) {
        screener.run();
//        }
    }
//    public static void main(String[] args) {
//        String jsonTest = "[{\"beacon\":{\"id\":\"solvebio-304\",\"name\":\"SolveBio - ClinVar/3.6.1-2015-11-06/ClinVar\",\"url\":null,\"organization\":\"SolveBio\",\"description\":\"Main ClinVar dataset of RCV accession records with associated genotype to phenotype data. \\r\\nGenomic coordinates are in agreement in HGVS notation. Data source is ClinVar's XML file.\",\"homePage\":null,\"email\":null,\"aggregator\":false,\"enabled\":false,\"visible\":false,\"createdDate\":\"2015-07-25\",\"supportedReferences\":[\"HG38\",\"HG19\"],\"aggregatedBeacons\":null},\"query\":{\"chromosome\":\"CHR1\",\"position\":11794418,\"allele\":\"G\",\"reference\":null},\"response\":true,\"frequency\":null,\"externalUrl\":null,\"info\":null}]";
//        System.out.println(jsonTest);
//        JSONArray jsonArray = new JSONArray(jsonTest);
//        JSONObject jsonObj = jsonArray.getJSONObject(0);
////        JSONObject beacon = jsonObj.getJSONObject("beacon");
//        Object tmp = jsonObj.get("response");
//        Boolean isNull = jsonObj.isNull("reponse");
//        Boolean response = jsonObj.getBoolean("response");
//        if (!jsonObj.isNull("reponse") && jsonObj.getBoolean("response")) {
//            System.out.println("match");
//        }
//
//    }

    private void createMatch(VariantSQL variant) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Beacon MATCH");
        MatchSQL match = new MatchSQL();
//        match.setEqualGene(true);
//        match.setEqualPath(true);
//        match.setEqualFam(true);
        match.setHpoDist(null);
        match.setMatch_type(MatchType.perfect.name());
//        match.setIdentical(true);
        match.setDatabase(_database);

        MatchVariantSQL beaconVariant = new MatchVariantSQL();
        beaconVariant.setDatabase(_database);
        beaconVariant.setMatch(match);
        beaconVariant.setNotified(false);
        match.getVariants().add(beaconVariant);

        MatchVariantSQL vwVariant = new MatchVariantSQL();
        vwVariant.setDatabase(vwDatabase);
        vwVariant.setUser(_dataset.getUser());
        vwVariant.setVariantId(variant.getId());
        vwVariant.setMatch(match);
        vwVariant.setNotified(false);
        match.getVariants().add(vwVariant);

        _matches.add(match);
    }
}
