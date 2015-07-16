eng.dataStores["mongodb"]={
    host:"localhost",
    port:27017,
    class: "org.semanticwb.datamanager.datastore.DataStoreMongo",
    envhost:"MONGO_PORT_27017_TCP_ADDR",
    envport:"MONGO_PORT_27017_TCP_PORT",
};
eng.dataSources["User"]={
    scls: "Users",
    modelid: "Security",
    dataStore: "mongodb",
};
eng.dataSources["Role"]={
    scls: "Roles",
    modelid: "Security",
    dataStore: "mongodb",
};
