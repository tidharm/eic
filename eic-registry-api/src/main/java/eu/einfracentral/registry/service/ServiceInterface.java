package eu.einfracentral.registry.service;


import eu.einfracentral.domain.InfraService;
import eu.einfracentral.domain.RichService;
import eu.openminted.registry.core.domain.FacetFilter;
import eu.openminted.registry.core.domain.Paging;
import eu.openminted.registry.core.exception.ResourceNotFoundException;
import eu.openminted.registry.core.service.SearchService;
import eu.openminted.registry.core.service.TransformerCRUDService;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface ServiceInterface<T, R, U extends Authentication> extends TransformerCRUDService<T, R, U> {

    // TODO: merge with InfraServiceService

    /**
     * @param service, auth
     * @return
     * @throws Exception
     */
    T addService(T service, U auth);

    /**
     * @param service, auth
     * @return
     * @throws ResourceNotFoundException
     * @throws Exception
     */
    T updateService(T service, U auth) throws ResourceNotFoundException;

    /**
     * Returns the Service.
     *
     * @param id      of the Service.
     * @param version of the Service.
     * @return service.
     */
    R get(String id, String version);

    /**
     * Returns the latest Service with the specified id.
     *
     * @param id of the resource in the index.
     * @return service.
     */
    R getLatest(String id) throws ResourceNotFoundException;

    /**
     * Get InfraServices by a specific field.
     *
     * @param field
     * @return
     */
    Map<String, List<T>> getBy(String field) throws NoSuchFieldException;

    /**
     * Get RichServices with the specified ids.
     *
     * @param ids
     * @return
     */
    List<RichService> getByIds(U authentication, String... ids);

    /**
     * @param ff
     * @return
     */
    Paging<RichService> getRichServices(FacetFilter ff, U auth);

    /**
     * @return
     */
    RichService createRichService(InfraService infraService, U auth);

    /**
     * Check if the Service exists.
     *
     * @param ids
     * @return
     */
    boolean exists(SearchService.KeyValue... ids);
}