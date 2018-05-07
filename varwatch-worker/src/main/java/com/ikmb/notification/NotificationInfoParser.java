/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.notification;

import com.ikmb.varwatchsql.variant_data.variant.VariantSQL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author bfredrich
 */
public class NotificationInfoParser {

//    private List<NotificationInfo> _notificationInfos = new ArrayList<NotificationInfo>();
    private Map<NotificationSubmitter, List<NotificationInfo>> _notificationInfos = new HashMap<NotificationSubmitter, List<NotificationInfo>>();
    private Map<String, String> _notifications = new HashMap<String, String>();

    public NotificationInfoParser(VariantSQL variant) {
//        List<MatchGroupSQL> matchGroups = VariantDatabaseHelper.getMatchGroupsToNotifiedByVariant(variant);
//        for (MatchGroupSQL matchgrp : matchGroups) {
//            String dbName = matchgrp.getDatabase().getName();
//            for (MatchSQL match : matchgrp.getMatches()) {
//                VariantSQL subVariant = match.getVariant();
//                UserSQL submitter = subVariant.getDataset().getUser();
//                String mailtext = "AccNr:" + match.getAccessionNr();
//                if (dbName.equals("VarWatch")) {
//                    VariantSQL variantByID = VariantDatabaseHelper.getVariantByID(Integer.parseInt(match.getAccessionNr()));
//                    String contact = variantByID.getDataset().getUser().getMail();
//                    Double distance = match.getDistance();
//
//                    mailtext += ";" + contact + ";" + distance;
//                    for (HPOTermSQL hpo : variantByID.getDataset().getPhenotypes()) {
//                        mailtext += "," + hpo.getIdentifier();
//                    }
//                    mailtext += "\n";
//                }
//
//                NotificationSubmitter notiSubmitter = new NotificationSubmitter();
//                notiSubmitter.setUser(submitter);
//                notiSubmitter.setVariantQuery(subVariant);
//                NotificationInfo notiinfo = new NotificationInfo();
//                notiinfo.setMatch(mailtext);
//                if (_notificationInfos.containsKey(notiSubmitter)) {
//                    List<NotificationInfo> infos = _notificationInfos.get(notiSubmitter);
//                    infos.add(notiinfo);
//                } else {
//                    List<NotificationInfo> tmpList = new ArrayList<NotificationInfo>();
//                    tmpList.add(notiinfo);
//                    _notificationInfos.put(notiSubmitter, tmpList);
//                }
//            }
//        }

    }

    public Map<NotificationSubmitter, List<NotificationInfo>> getNotificationInfos() {
        return _notificationInfos;
    }

}
