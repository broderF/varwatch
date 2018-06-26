/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.rest.matching;

import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.dataset.DatasetManager;
import com.ikmb.core.data.hpo.HPOTerm;
import com.ikmb.core.data.matching.MatchVariantDataManager;
import com.ikmb.core.data.variant.VariantDataManager;
import com.ikmb.core.varwatchcommons.entities.MatchInformation;
import com.ikmb.rest.annotation.AnnotationService;
import com.ikmb.rest.util.HTTPVarWatchInputConverter;
import java.util.List;
import javax.ws.rs.core.Response;
import junit.framework.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author broder
 */
public class MatchingServiceTest {

    MatchingServiceImpl matchingService = new MatchingServiceImpl();
    VariantDataManager variantManager = mock(VariantDataManager.class);
    Long matchId = 15l;
    String header = "Bearer 03a558316b6bd15ac4e503f28ce0cf4a";

    @Test
    public void shouldReturnCorrectMatches() {
        HTTPVarWatchInputConverter inputConverter = mock(HTTPVarWatchInputConverter.class);
        matchingService.setInputConverter(inputConverter);
        User user = new User();
        when(inputConverter.getUserFromHeader(header)).thenReturn(user);
        MatchVariantDataManager variantmatchingManager = mock(MatchVariantDataManager.class);
        when(variantmatchingManager.getMatchInformation(matchId, user)).thenReturn(new MatchInformation());
        matchingService.setVariantmatchingManager(variantmatchingManager);
        matchingService.getMatchingDetail(header, matchId);
//        Assert.assertEquals(expectedHpoResponse, datasetAnnotation.getEntity().toString());
    }
}
