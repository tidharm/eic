Dear ${user.name},

<#if state == "pending initial approval">
You have applied for registering your organization [${providerBundle.provider.id}] - [${providerBundle.provider.name}] as a new service provider in ${project}.
Your application will be reviewed by the administration team and you will be notified on its approval or rejection, as well as for any further steps you can follow for registering successfully your organization.
</#if>
<#if state == "pending service template submission">
You have applied for registering your organization [${providerBundle.provider.id}] - [${providerBundle.provider.name}] as a new service provider in ${project}.
Your application has been approved and you may proceed with providing one of your services ${endpoint}/newServiceProvider to complete the registration process.
The service should be described according to the ${project}’s Service Description Template (SDT), which has been adopted by flagship initiatives such as the EOSC-hub (EGI, EUDAT), GÉANT, OpenAIRE(-Advance) and PRACE, as the standard with which EOSC portal will be populated.
</#if>
<#if state == "pending service template approval">
You have provided a new service [${service.id}] – [${service.name}] in ${project}.
The information provided will be reviewed by the administration team and you will be notified on its approval or rejection, as well as for any further steps you can follow for registering successfully your services.
</#if>
<#if state == "approved">
    <#if providerBundle.status == "published">
The information for the service [${service.id}] – [${service.name}] has been reviewed and has been successfully added to the ${project} Catalogue. You can view the new service ${endpoint}/service/${service.id}. You may now login and proceed with providing more services for [${providerBundle.provider.id}].
    <#else>
Your service provider [${providerBundle.provider.name}] has been set to inactive. For any further clarifications please contact registration office of ${project} at ${registrationEmail}.
    </#if>
</#if>
<#if state == "rejected service template">
The information for the service [${service.id}] – [${service.name}] has been reviewed and unfortunately does not comply with the SDT and the type of services being published in ${project} Catalogue. For any further clarifications please contact registration office of ${project} at ${registrationEmail}.
</#if>
<#if state == "rejected">
You have applied for registering your organization [${providerBundle.provider.id}] - [${providerBundle.provider.name}] as a new service provider in ${project}.
Your application has been rejected as your organization does not comply with the rules of participations in ${project}. For any further clarifications please contact registration office of ${project} at ${registrationEmail}.
</#if>

Thank you for your interest in becoming a member of ${project}.

Best Regards,
the ${project} Team
