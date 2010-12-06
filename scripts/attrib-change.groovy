
import fedreg.core.*

 // Changes default attribute population to Shib 2.2 naming scheme for aliases/id

def o = AttributeBase.findWhere(oid:'2.5.4.10')
o.alias='organizationName'
o.save()

def ou =  AttributeBase.findWhere(oid:'2.5.4.11')
ou.friendlyName = 'Organizational Unit'
ou.alias='organizationalUnit'
ou.save()

def eduPersonEntitlement =  AttributeBase.findWhere(oid:'1.3.6.1.4.1.5923.1.1.1.7')
eduPersonEntitlement.friendlyName = 'Entitlement'
eduPersonEntitlement.alias = 'eduPersonEntitlement'
eduPersonEntitlement.save()

def eduPersonPrincipalName =  AttributeBase.findWhere(oid:'1.3.6.1.4.1.5923.1.1.1.6')
eduPersonPrincipalName.friendlyName = 'Principal Name'
eduPersonPrincipalName.alias = 'eduPersonPrincipalName'
eduPersonPrincipalName.save()

def cn =  AttributeBase.findWhere(oid:'2.5.4.3')
cn.friendlyName='Common Name'
cn.alias='commonName'
cn.save()

def mail =  AttributeBase.findWhere(oid:'0.9.2342.19200300.100.1.3')
mail.friendlyName = 'Email'
mail.alias = 'email'
mail.save()

def mobile =  AttributeBase.findWhere(oid:'0.9.2342.19200300.100.1.41')
mobile.friendlyName = 'Mobile phone number'
mobile.alias = 'mobileNumber'
mobile.save()