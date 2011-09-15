/*
FR HOTFIX

DATABASE MIGRATION

This implements changes to the way base attributes are managed within FR per 125 to ensure very strict SAML2 compliance.

Bradley Beddoes
25/8/2011
*/

alter table attribute_base change name legacy_name varchar(255);
alter table attribute_base change alias name varchar(255);
alter table attribute_base drop header_name;
alter table attribute_base add admin_restricted bit not null;
update attribute_base set admin_restricted = false;