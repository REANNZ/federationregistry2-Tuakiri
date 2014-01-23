import groovy.sql.Sql

sql = Sql.newInstance("jdbc:mysql://localhost:3306/federationregistry2?useUnicode=yes&characterEncoding=UTF-8", "fr", "password", "com.mysql.jdbc.Driver")

def output = new StringBuffer()
def excludedSubjects = []
def excludedRoles = []

output.append("\n-- Subjects\n")

// Subjects
sql.eachRow("select * from profile_base inner join _user on profile_base.id = _user.profile_id", {s ->
  if(s.username != 'internaladministrator' && !s.username.contains('NameIDImpl'))   // exclude internal user and clean up some crappy accounts
    output.append("insert into subject_base (id, enabled, principal, class, cn, contact_id, email) values ($s.id, $s.enabled, '$s.username', 'aaf.fr.identity.Subject', '${s.full_name.replace('\'','')}', $s.contact_id, '$s.email');\n")
  else
    excludedSubjects.add(s.id)
})

output.append("\n-- Roles\n")

// Roles
sql.eachRow("select * from _role", { r ->
  if(!(r.name in ['USER', 'SYSTEM ADMINISTRATOR', 'federation-administrators'])) {
    output.append("insert into role (id, name, description) values($r.id, '${r.name.replace('\'','')}', '${r.description.replace('Global administrators for', 'Administrators of').replace('\'','')}');\n")
  } else {
    excludedRoles.add(r.id) // We don't carry these over to FR 2.
  }
})

output.append("\n\n")

sql.eachRow("select * from _role_users", { r ->
  if(!(r.role_id in excludedRoles) && !(r.user_base_id in excludedSubjects)) 
    output.append("insert into role_subjects (role_id, subject_base_id) values($r.role_id, $r.user_base_id);\n")
})

output.append("\n-- Permissions\n")

//Permissions
output.append("update permission set type='grails.plugins.federatedgrails.WildcardPermission';\n")
output.append("update permission set class='grails.plugins.federatedgrails.Permission';\n")

// Delete from Permissions table all entries pointing to the excluded roles
excludedRoles.each {
    output.append("delete from permission where role_id = $it;\n");
}

output.append("\n\n")

output.append("drop table level_permission_first;\n")
output.append("drop table level_permission_second;\n")
output.append("drop table level_permission_third;\n")
output.append("drop table level_permission_fourth;\n")
output.append("drop table level_permission_fifth;\n")
output.append("drop table level_permission_sixth;\n")

output.append("delete from permission where target regexp 'profile:edit:.*';\n")
output.append("delete from permission where target regexp 'federation:reporting';\n")

output.append("\n\n")

output.append("alter table permission drop column class;\n\n")

output.append("update permission\n")
output.append("set target = concat('federation:management:',target)\n")
output.append("where target regexp 'descriptor:.*:*';\n\n")

output.append("update permission\n")
output.append("set target = concat('federation:management:',target)\n")
output.append("where target regexp 'organization:.*:*';\n")

output.append("\n-- Attributes\n")

// Attributes
output.append("update attribute set class='aaf.fr.foundation.Attribute' where class='fedreg.core.Attribute';\n")
output.append("update attribute set class='aaf.fr.foundation.RequestedAttribute' where class='fedreg.core.RequestedAttribute';\n\n")

output.append("\n-- Contacts\n")

// Contact
sql.eachRow("select * from contact", { ct ->

  x = sql.firstRow("select uri from uri where id=?", ct.email_id)
  if (x)
    output.append("update contact set email='$x.uri' where id=$ct.id;\n")

  if(ct.work_phone_id){
    x = sql.firstRow("select uri from uri where id=?", ct.work_phone_id)
    if(x)
      output.append("update contact set work_phone='$x.uri' where id=$ct.id;\n")
  }

  if(ct.mobile_phone_id) {
    x = sql.firstRow("select uri from uri where id=?", ct.mobile_phone_id)
    if(x)
      output.append("update contact set mobile_phone='$x.uri' where id=$ct.id;\n")
  }

})

output.append("\n-- AdditionalMetadataLocation\n")

// AdditionalMetadataLocation
sql.eachRow("select * from additional_metadata_location", { am ->

  if(am.uri_id){
    x = sql.firstRow("select uri from uri where id=?", am.uri_id)
    if (x)
      output.append("update additional_metadata_location set uri='$x.uri' where id=$am.id;\n")
  }

})

output.append("\n-- SamlURI\n")

// SamlURI
sql.eachRow("select * from samluri", { su ->

  if(su.id){
    x = sql.firstRow("select uri from uri where id=?", su.id)
    if (x.uri)
      output.append("update samluri set uri='$x.uri' where id=$su.id;\n")

    x = sql.firstRow("select description from uri where id=?", su.id)
    if (x.description)
      output.append("update samluri set description='$x.description' where id=$su.id;\n")
  }

})

output.append("\n-- Endpoints\n")

// Endpoints
sql.eachRow("select * from endpoint", { e ->

  if(e.location_id) {
    x = sql.firstRow("select uri from uri where id=?", e.location_id)
    if(x)
      output.append("update endpoint set location='$x.uri' where id=$e.id;\n")
  }

  if(e.response_location_id) {
    x = sql.firstRow("select uri from uri where id=?", e.response_location_id)
    if(x)
      output.append("update endpoint set response_location='$x.uri' where id=$e.id;\n")
  }

})

output.append("\n-- Organisations\n")

// Organisations
sql.eachRow("select * from organization", { o ->
  if(o.url_id) {
    x = sql.firstRow("select uri from uri where id=?", o.url_id)
    if(x.uri)
      output.append("update organization set url='$x.uri' where id=$o.id;\n")
  }
})

output.append("\n-- Role Descriptors\n")

// Role Descriptors
sql.eachRow("select * from role_descriptor", { rd ->
  if(rd.errorurl_id) {
    x = sql.firstRow("select uri from uri where id=?", rd.errorurl_id)
    if(x.uri)
      output.append("update role_descriptor set errorurl='$x.uri' where id=$rd.id;\n")
  }
})

output.append("\n-- Identity Providers\n")

// IdP
output.append("update idpssodescriptor set auto_accept_services = false;\n\n")

output.append("\n-- Audit\n")

// Audit
output.append("delete from audit_log;\n\n")

output.append("\n-- Workflow\n")

// Workflow
output.append("drop table task_instance__user;\n")
output.append("delete from task_instance_subject;\n")
output.append("delete from task_instance;\n")
output.append("delete from process_instance_params;\n")
output.append("delete from process_instance;\n")

output.append("\n-- Apply unique constraints\n")

// Uniques
output.append("ALTER TABLE `contact` ADD UNIQUE `email` USING BTREE (email);\n")
output.append("ALTER TABLE `attribute_base` ADD UNIQUE `name` USING BTREE (`name`);\n")
output.append("ALTER TABLE `organization_type` ADD UNIQUE `display_name` USING BTREE (display_name);\n")

def target = new File('target')
target.mkdir()

def out = new File('target/customDataMigration.sql')
out.write(output.toString())
