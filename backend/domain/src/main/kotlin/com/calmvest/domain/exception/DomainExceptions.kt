package com.calmvest.domain.exception

/**
 * Thrown when an open banking consent is invalid or expired.
 */
class ConsentInvalidException(consentId: String) :
    IllegalArgumentException("Open banking consent is not active: $consentId")

/**
 * Thrown when a financial operation violates a business rule.
 */
class BusinessRuleViolationException(message: String) : IllegalStateException(message)
