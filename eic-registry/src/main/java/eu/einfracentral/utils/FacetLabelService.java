package eu.einfracentral.utils;

import eu.einfracentral.domain.Provider;
import eu.einfracentral.domain.ProviderBundle;
import eu.einfracentral.domain.Vocabulary;
import eu.einfracentral.registry.service.ProviderService;
import eu.einfracentral.registry.service.VocabularyService;
import eu.openminted.registry.core.domain.Facet;
import eu.openminted.registry.core.domain.FacetFilter;
import eu.openminted.registry.core.domain.Value;
import org.apache.commons.collections.list.TreeList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FacetLabelService {

    private static final Logger logger = LogManager.getLogger(FacetLabelService.class);
    private ProviderService<ProviderBundle, Authentication> providerService;
    private VocabularyService vocabularyService;

    @Autowired
    FacetLabelService(ProviderService<ProviderBundle, Authentication> providerService, VocabularyService vocabularyService) {
        this.providerService = providerService;
        this.vocabularyService = vocabularyService;
    }

    String toProperCase(String str, String delimiter, String newDelimiter) {
        return Arrays.stream(str.split(delimiter)).map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(newDelimiter));
    }

    @SuppressWarnings("unchecked")
    public List<Facet> createLabels(List<Facet> facets) {
        List<Facet> enrichedFacets = new TreeList(); // unchecked warning here
        FacetFilter ff = new FacetFilter();
        ff.setQuantity(10000);
        Map<String, String> providerNames = providerService.getAll(ff, null)
                .getResults()
                .stream().collect(Collectors.toMap(ProviderBundle::getId, p -> p.getProvider().getName()));
        Map<String, Vocabulary> allVocabularies = vocabularyService.getVocabulariesMap();

        Facet superCategories;
        Facet categories;
        Facet scientificDomains;

        for (Facet facet : facets) {
            if (facet.getField().equals("subcategories")) {
                categories = createCategoriesFacet(facet);
                superCategories = createSupercategoriesFacet(categories);

                enrichedFacets.add(superCategories);
                enrichedFacets.add(categories);
            }
            if (facet.getField().equals("scientific_subdomains")) {
                scientificDomains = createScientificDomainsFacet(facet);
                enrichedFacets.add(scientificDomains);
            }
            for (Value value : facet.getValues()) {

                switch (facet.getField()) {
                    case "providers":
                        value.setLabel(providerNames.get(value.getValue()));
                        break;

                    default:
                        if (allVocabularies.containsKey(value.getValue())) {
                            value.setLabel(allVocabularies.get(value.getValue()).getName());
                        } else {
                            value.setLabel(toProperCase(toProperCase(value.getValue(), "-", "-"), "_", " "));
                        }
                }
            }
        }
        enrichedFacets.addAll(facets);
        return enrichedFacets;
    }

    Facet createCategoriesFacet(Facet subcategories) {
        List<Value> categoriesValues = new ArrayList<>();

        Map<String, Vocabulary> categoriesMap = new TreeMap<>();

        for (Value value : subcategories.getValues()) {
            Vocabulary parent = vocabularyService.getParent(value.getValue());
            if (parent != null) {
                categoriesMap.putIfAbsent(parent.getId(), parent);
            }
        }

        for (Vocabulary category : categoriesMap.values()) {
            Value value = new Value();
            value.setValue(category.getId());
            value.setLabel(category.getName());
            categoriesValues.add(value);
        }

        Facet categories = new Facet();
        categories.setField("categories");
        categories.setLabel("Categories");
        categories.setValues(categoriesValues);
        return categories;
    }

    Facet createSupercategoriesFacet(Facet categories) {
        List<Value> superCategoriesValues = new ArrayList<>();

        Map<String, Vocabulary> categoriesMap = new TreeMap<>();

        for (Value value : categories.getValues()) {
            Vocabulary parent = vocabularyService.getParent(value.getValue());
            if (parent != null) {
                categoriesMap.putIfAbsent(parent.getId(), parent);
            }
        }

        for (Vocabulary category : categoriesMap.values()) {
            Value value = new Value();
            value.setValue(category.getId());
            value.setLabel(category.getName());
            superCategoriesValues.add(value);
        }

        Facet superCategories = new Facet();
        superCategories.setField("supercategories");
        superCategories.setLabel("Supercategories");
        superCategories.setValues(superCategoriesValues);
        return superCategories;
    }

    Facet createScientificDomainsFacet(Facet scientificSubdomains) {
        List<Value> scientificDomainsValues = new ArrayList<>();

        Map<String, Vocabulary> categoriesMap = new TreeMap<>();

        for (Value value : scientificSubdomains.getValues()) {
            Vocabulary parent = vocabularyService.getParent(value.getValue());
            if (parent != null) {
                categoriesMap.putIfAbsent(parent.getId(), parent);
            }
        }

        for (Vocabulary category : categoriesMap.values()) {
            Value value = new Value();
            value.setValue(category.getId());
            value.setLabel(category.getName());
            scientificDomainsValues.add(value);
        }

        Facet scientificDomains = new Facet();
        scientificDomains.setField("scientific_domains");
        scientificDomains.setLabel("Scientific Domains");
        scientificDomains.setValues(scientificDomainsValues);
        return scientificDomains;
    }
}
