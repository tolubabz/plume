package com.librasmart.plume.rider.util;

import com.librasmart.plume.rider.exception.InvalidSortFieldException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class SortValidator {

    private SortValidator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void validate(Pageable pageable, Set<String> allowedFields) {
        for (Sort.Order order : pageable.getSort()) {
            if (!allowedFields.contains(order.getProperty())) {
                throw new InvalidSortFieldException(order.getProperty(), allowedFields);
            }
        }
    }
}
