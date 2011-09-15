/*
FR HOTFIX

DATABASE MIGRATION

A colum with the name 'archived' with all values set to false will be created for the tables
organization, entity_descriptor and  role_descriptor by executing this script to support FR 1.2 archived flags.

Bradley Beddoes
22/7/2011
*/

ALTER TABLE organization ADD COLUMN archived BIT;
ALTER TABLE entity_descriptor ADD COLUMN archived BIT;
ALTER TABLE role_descriptor ADD COLUMN archived BIT;

update organization set archived = false;
update entity_descriptor set archived = false;
update role_descriptor set archived = false;