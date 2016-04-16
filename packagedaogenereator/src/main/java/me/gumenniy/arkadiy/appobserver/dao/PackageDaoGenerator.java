package me.gumenniy.arkadiy.appobserver.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class PackageDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "me.gumenniy.arkadiy.appobserver.dao.model");

        Entity person = schema.addEntity(ApplicationEntity.NAME);
        person.addStringProperty(ApplicationEntity.PROPERTY_PACKAGE).unique().primaryKey();
        person.addDateProperty(ApplicationEntity.PROPERTY_SCAN_DATE);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    interface ApplicationEntity {
        String NAME = "App";
        String PROPERTY_PACKAGE = "appPackage";
        String PROPERTY_SCAN_DATE = "lastScanDate";
    }
}
