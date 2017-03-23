# README - Version 2 of Federation Registry

- (c) Australian Access Federation
- Author: Bradley Beddoes, Australian Access Federation
- Project License: Apache 2.0, utilised software per their licensing, see NOTICE for more.
- See more details and documentation at http://wiki.aaf.edu.au/federationregistry/

## Overview
The Federation Registry project provides an extensible, open web application that provides a central point of registration, management and reporting for identity and service providers participating in a standards compliant SAML 2 identity federation.

## Key Features
- A focus on Organisations as the key building block for the federation
- Management for all aspects of SAML 2 compliant Identity and Service Providers
- SAML 2.x compliant metadata generation
- Additional assistance for Shibboleth IDP and SP administrators including automated Attribute Filter generation
- Public registration for Organisations, Identity Providers and Service Providers that are new to the federation
- A personalised dashboard view of the federation for all users
- A cross browser (including mobile devices) HTML5 compliant user interface which can be branded for deploying organisations.
- Multilingual capable
- A fully customisable workflow engine to handle registrations and other critical federation changes
- In-depth reporting to gain insight to the workings of the entire federation
- Federation integrated, automatically provisioned user accounts with fine grained access control

## Local Dependencies
As of FR 2.7.0 (March 2017) there is an extra step required to build the AAF
patched version of Groovy for dependency resolution purposes.

To build AAF Groovy:

1. Be on Java 7
1. Be in the root directory of the federationregistry project checked out from
Github
1. `git submodule init`
1. `git submodule update`
1. cd aaf-patched-groovy
1. ./gradlew clean dist

To build FR WAR file:

1. Return to ../app/federationregisty and use `grails war` as normal.

To develop FR code:

1. Return to ../app/plugin/X and use `grails Y` as normal.

## License Notice
This product includes software developed at Highcharts - http://www.highcharts.com 

This is used by the AAF under the Highcharts 'Non-commercial - Free' license
defined by http://creativecommons.org/licenses/by-nc/3.0/ as the Australian Access Federation Inc is both a not for profit and Australian University sector organisation. Other organisations deploying Federation Registry must decide how licensing for Highcharts applies to them at http://shop.highsoft.com/highcharts.html and obtain any licenses that are necessary.
