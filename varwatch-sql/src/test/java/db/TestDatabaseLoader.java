/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.google.inject.Injector;
import com.ikmb.core.data.auth.user.User;
import com.ikmb.core.data.auth.user.UserManager;
import com.ikmb.core.data.dataset.DatasetVW;
import java.util.HashSet;
import org.joda.time.DateTime;

/**
 *
 * @author broder
 */
public class TestDatabaseLoader {

//    Provider<EntityManager> emProvider;
    Injector inj;

    public Injector getInj() {
        return inj;
    }

    public TestDatabaseLoader() {
//        inj = Guice.createInjector(new VarWatchMainModule(),new SQLModule(), new JpaPersistModule("varwatch_test"));
//        VarWatchPersist init = inj.getInstance(VarWatchPersist.class);
        
//        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("varwatch_test");
//        EntityManager em = emFactory.createEntityManager();
//        emProvider = 
    }

//    public Provider<EntityManager> getEmProvider() {
//        return emProvider;
//    }

    public User getTestUser() {
        User user = new User();
        user.setFirstName("Donald");
        user.setLastName("Duck");
        user.setActive(Boolean.TRUE);
        user.setAddress("Schlossallee");
        user.setCity("Entenhausen");
        user.setCountry("Entenhausen");
        user.setInstitution("Enten GmbH");
        user.setMail("test");
        user.setPassword("donald_duck");
        user.setPhone("1234");
        user.setPostalCode("123");
        user.setReportSchedule("DAILY");
        user.setLastReport(new DateTime());
        user.setDatasets(new HashSet<DatasetVW>());
        UserManager um = inj.getInstance(UserManager.class);
        um.save(user);
        return user;
    }

//    public AuthClient getClient() {
//        Client client = new Client();
//        client.setName("testclient");
//        client.setPassword("testpw");
//        ClientManager cm = inj.getInstance(ClientManager.class);
//        cm.save(client);
////        emProvider.get().persist(client);
//        return cm.getClient("testclient");
//    }
}
