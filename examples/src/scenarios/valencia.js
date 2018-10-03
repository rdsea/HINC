const scenarioCreator = require("../creator_modules/scenario_creator");
const createValenciaSensor = require('../creator_modules/adapters_and_providers/create_valencia-sensor-provider');
const createMQTT = require('../creator_modules/adapters_and_providers/create_mqtt-provider');
const createVessel = require('../creator_modules/adapters_and_providers/create_vessel-provider');
const createAlarmClient = require('../creator_modules/adapters_and_providers/create_alarmclient-provider');

scenarioCreator.addAdapterProviderCreator(createValenciaSensor);
scenarioCreator.addAdapterProviderCreator(createMQTT);
scenarioCreator.addAdapterProviderCreator(createVessel);
scenarioCreator.addAdapterProviderCreator(createAlarmClient);
scenarioCreator.createScenario();