package kql.exceptions

class CannotSubtractAndAddFieldsException : Exception(
        "Can only include fields (+) or exclude fields (-) not both, in a single field projection."
)