---
layout: page
title: "Develop plugins"
category: dev
date: 2016-06-23 13:20:12
---


In order to develop new plugin, developer must implement two following interfaces:

 - [Provider Adaptor](https://github.com/SINCConcept/HINC/blob/master/collector/src/main/java/sinc/hinc/abstraction/ResourceDriver/ProviderAdaptor.java): To interface with the provider APIs to collect data and send control to resources.
 - [Informaion Transformer](https://github.com/SINCConcept/HINC/tree/master/collector/src/main/java/sinc/hinc/abstraction/transformer): To extract the capability and transform to HINC information model.

