/*
FR HOTFIX

DATABASE MIGRATION - Works with indexedendpoint-hotfix.groovy

A colum with the name 'samlmd_index' with all values set to 0 will be created for the table
indexed_endpoint by executing this script.

Bradley Beddoes
7/1/2011
*/

ALTER TABLE indexed_endpoint ADD COLUMN samlmd_index int(11) NOT NULL;

UPDATE indexed_endpoint SET samlmd_index = 0;