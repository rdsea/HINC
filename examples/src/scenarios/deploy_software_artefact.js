const scenarioCreator = require("../creator_modules/scenario_creator");
const createNodeRed = require('../creator_modules/adapters_and_providers/create_nodered-datatransformer-provider');


scenarioCreator.addAdapterProviderCreator(createNodeRed);
scenarioCreator.createScenario();