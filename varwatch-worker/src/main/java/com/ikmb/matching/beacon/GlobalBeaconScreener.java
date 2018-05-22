package com.ikmb.matching.beacon;

import com.google.inject.Inject;
import com.ikmb.core.data.dataset.DatasetVW;
import com.ikmb.core.data.matching.Match;
import com.ikmb.core.data.matching.MatchVariant;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.reference_db.RefDatabase;
import com.ikmb.core.data.variant.Variant;
import com.ikmb.matching.varwatch.VarWatchScreener.MatchType;
import com.ikmb.core.data.variant.VariantStatusManager;
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
public class GlobalBeaconScreener {

    private Connection _conn = null;
    private List<Match> _matches = new ArrayList<>();
    private DatasetVW _dataset;
    private RefDatabase _database;
    private RefDatabase vwDatabase;

    @Inject
    private MatchVariantDataManager matchDataManager;

    @Inject
    private VariantStatusManager variantStatusManager;

    public void initialize(RefDatabase database, DatasetVW dataset) {
        _database = database;
        _dataset = dataset;
    }

    public void setVWDatabase(RefDatabase vwDatabase) {
        this.vwDatabase = vwDatabase;
    }

    public DatasetVW getDataset() {
        return _dataset;
    }

    public RefDatabase getDatabase() {
        return _database;
    }

    public void run() {
//        List<Variant> variants = ConvertHelper.convertVariantsFromSQL(new ArrayList<Variant>(_dataset.getVariants()));
//        if (!_database.getAssembly().equals(VWConfiguration.STANDARD_COORDS)) {
//            variants = CrossmapHelper.mapVariantsFromAssembly(variants, VWConfiguration.STANDARD_COORDS, _database.getAssembly());
//        }
        for (Variant variant : _dataset.getVariants()) {
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

    public List<Match> getMatches() {
        return _matches;
    }

    public static void main(String[] args) {
        GlobalBeaconScreener screener = new GlobalBeaconScreener();
        DatasetVW ds = new DatasetVW();
        Variant variant = new Variant();
        variant.setAlternateBase("T");
        variant.setChromosomeName("1");
        variant.setChromosomePos(1014143);
        variant.setReferenceBase("C");
        Set<Variant> variants = new HashSet<>();
        variants.add(variant);
        ds.setVariants(variants);
        RefDatabase db = new RefDatabase();
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

    private void createMatch(Variant variant) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Beacon MATCH");
        Match match = new Match();
//        match.setEqualGene(true);
//        match.setEqualPath(true);
//        match.setEqualFam(true);
        match.setHpoDist(null);
        match.setMatch_type(MatchType.perfect.name());
//        match.setIdentical(true);
        match.setDatabase(_database);

        MatchVariant beaconVariant = new MatchVariant();
        beaconVariant.setDatabase(_database);
        beaconVariant.setMatch(match);
        beaconVariant.setNotified(false);
        match.getVariants().add(beaconVariant);

        MatchVariant vwVariant = new MatchVariant();
        vwVariant.setDatabase(vwDatabase);
        vwVariant.setUser(_dataset.getUser());
        vwVariant.setVariantId(variant.getId());
        vwVariant.setMatch(match);
        vwVariant.setNotified(false);
        match.getVariants().add(vwVariant);

        _matches.add(match);
    }
}
