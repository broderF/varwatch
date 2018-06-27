/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.core.tools.variant_parser;

import com.ikmb.core.tools.EnsemblHttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author broder
 */
public class VariantParserFactory {

    private static final Logger logger = LoggerFactory.getLogger(VariantParserFactory.class);

    public static VariantParser getVariantParser(String variantFormatName) {
        VariantFormat variantFormat = VariantFormat.getVariantFormat(variantFormatName);
        switch (variantFormat) {
            case NORMAL:
                logger.info("use parser format: {}", variantFormatName);
                return new JSONVariantParser();
            case HGVS:
                logger.info("use parser format: {}", variantFormatName);
                HGVSVariantParser hgvsVariantParser = new HGVSVariantParser();
                hgvsVariantParser.setEnsemblRequestHandler(new EnsemblHttpRequestHandler());
                return hgvsVariantParser;
            case VCF:
                logger.info("use parser format: {}", variantFormatName);
                return new VCFVariantParser();
            default:
                throw new IllegalArgumentException("Cant find variant parser with format: " + variantFormatName);
        }
    }

    public enum VariantFormat {

        NORMAL, HGVS, VCF;

        public static VariantFormat getVariantFormat(String value) {
            for (VariantFormat v : values()) {
                if (v.toString().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
