Dear Registration Team,

<#if state == "pending initial approval">
A new application by [${user.name}] – [${user.email}] has been received for registering [${providerBundle.provider.id}] – [${providerBundle.provider.name}] as a new service provider in ${project}.
You can review the application at ${endpoint}/serviceProvidersList and approve or reject it.
</#if>
<#if state == "pending service template submission">
The application by [${user.name}] – [${user.email}] for registering [${providerBundle.provider.id}] has been accepted.
You can view the application status ${endpoint}/serviceProvidersList.
</#if>
<#if state == "pending service template approval">
Information about the new service: [${service.id}] has been provided by [${user.name}] – [${user.email}].
You can review the information ${endpoint}/service/${service.id} and approve or reject it.
</#if>
<#if state == "approved">
    <#if providerBundle.status == "published">
The service: [${service.id}] provided by [${user.name}] – [${user.email}] has been accepted.
You can view the application status ${endpoint}/serviceProvidersList.
    <#else>
The service provider [${providerBundle.provider.name}] has been set to inactive.
You can view the application status here ${endpoint}/serviceProvidersList.
    </#if>
</#if>
<#if state == "rejected service template">
The service: [${service.id}] provided by [${user.name}] – [${user.email}] has been rejected.
You can view the application status ${endpoint}/serviceProvidersList.
</#if>
<#if state == "rejected">
The application by [${user.name} ${user.surname}] – [${user.email}] for registering [${providerBundle.provider.name}] has been rejected.
You can view the application status here ${endpoint}/serviceProvidersList.
</#if>

Best Regards,
the ${project} Team
