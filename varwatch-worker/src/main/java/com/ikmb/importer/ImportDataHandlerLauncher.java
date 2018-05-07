/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.importer;

import com.ikmb.importer.hgmd.HGMDData;
import com.ikmb.extraction.AssemblyCrossMapper;
import com.ikmb.varwatchsql.entities.DatasetHGMDSQL;
import com.ikmb.varwatchcommons.entities.VWVariant;
import com.ikmb.varwatchsql.entities.VariantEffectSQL;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author broder
 */
public class ImportDataHandlerLauncher {

    //lesen einer jsonarray datei mit mme requests
    //erstellen eines vcfs
    //crossmapping zu aktuellem stand vom angegebenen
    //annotieren mit dem vep
    //setzten des hpo terms (wenn null, dann 118)
    //speichern der Daten in der dataset datenbank
    public static void main(String[] args) {
        File folder = new File("/data/varwatch/data/hgmd/hgmd_2014_3/split_hgmd/");
        File[] listOfFiles = folder.listFiles();
        int count = 0;
        for (File hgmdfile : listOfFiles) {
            ImportDataHandler handler = new ImportDataHandler();
            List<HGMDData> readHgmdFile = handler.readHgmdFile(hgmdfile);
            List<VWVariant> variants = getVariants(readHgmdFile);

            //crossmapping von 37 zu 38
            AssemblyCrossMapper crossMapper = new AssemblyCrossMapper();
            crossMapper.setVariants(variants,"GRCh37");
            crossMapper.setTarget("GRCh38");
            crossMapper.crossmap();
            List<VWVariant> mappedVariants = crossMapper.getMappedVariants();
            
            
            String vcfString = handler.createVcfFromVWVariants(mappedVariants);
            
            readHgmdFile = setNewCoordinates(readHgmdFile,mappedVariants);

            List<VariantEffectSQL> variantEffects = handler.annotate(vcfString.getBytes());
            List<DatasetHGMDSQL> hgmdDatasets = handler.createHgmdDatasets(readHgmdFile, variantEffects);
            System.out.println("size: " + hgmdDatasets.size());

            for (DatasetHGMDSQL hgmdDataset : hgmdDatasets) {
                System.out.println(hgmdDataset.getHgmdAcc());
                System.out.println(hgmdDataset.getTranscript().getName());
                handler.persist(hgmdDataset);
            }
        }
        System.out.println(count);
    }

    private static List<VWVariant> getVariants(List<HGMDData> readHgmdFile) {
        List<VWVariant> variants = new ArrayList<VWVariant>();
        for (HGMDData data : readHgmdFile) {
            VWVariant variant = new VWVariant();
            variant.setAlternateBases(data.getAlt());
            variant.setStart(data.getPos());
            variant.setReferenceBases(data.getRef());
            variant.setReferenceName(data.getChromosome());
            variant.setVepIdentifier(data.getHgmd_acc());
//            variant.setAssembly("GRCh37");
            variants.add(variant);
        }
        return variants;
    }

    private static List<HGMDData> setNewCoordinates(List<HGMDData> readHgmdFile, List<VWVariant> mappedVariants) {
           List<HGMDData> hgmdDatas = new ArrayList<HGMDData>();
           for(HGMDData hgmdData: readHgmdFile){
               for(VWVariant variant: mappedVariants){
                   if(hgmdData.getHgmd_acc().equals(variant.getVepIdentifier())){
                       HGMDData tmphgmdData = new HGMDData();
                       tmphgmdData.setAlt(variant.getAlternateBases());
                       tmphgmdData.setChromosome(variant.getReferenceName());
                       tmphgmdData.setHgmd_acc(hgmdData.getHgmd_acc());
                       tmphgmdData.setHpos(hgmdData.getHpos());
                       tmphgmdData.setPos(variant.getStart());
                       tmphgmdData.setRef(variant.getReferenceBases());
                       tmphgmdData.setUlms(hgmdData.getUlms());
                       hgmdDatas.add(tmphgmdData);
                       break;
                   }
               }
           }
           
           return hgmdDatas;
    }
}
