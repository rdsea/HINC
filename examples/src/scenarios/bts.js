const scenarioCreator = require("../creator_modules/scenario_creator");
const createSensor = require('../creator_modules/adapters_and_providers/create_sensor-provider');
const createMQTT = require('../creator_modules/adapters_and_providers/create_mqtt-provider');
const createBigQuery = require('../creator_modules/adapters_and_providers/create_bigquery-provider');
const createIngestion = require('../creator_modules/adapters_and_providers/create_ingestion-provider');
const createAMQP = require('../creator_modules/adapters_and_providers/create_amqp-provider');
const createFirewall = require('../creator_modules/adapters_and_providers/create_kube-firewall-provider');

scenarioCreator.addAdapterProviderCreator(createSensor);
scenarioCreator.addAdapterProviderCreator(createMQTT);
scenarioCreator.addAdapterProviderCreator(createBigQuery);
scenarioCreator.addAdapterProviderCreator(createIngestion);
scenarioCreator.addAdapterProviderCreator(createAMQP);
scenarioCreator.addAdapterProviderCreator(createFirewall);
scenarioCreator.createScenario();